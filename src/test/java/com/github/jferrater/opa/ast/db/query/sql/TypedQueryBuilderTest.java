package com.github.jferrater.opa.ast.db.query.sql;

import com.github.jferrater.opa.ast.db.query.core.elements.SqlPredicate;
import com.github.jferrater.opa.ast.db.query.model.response.OpaCompilerResponse;
import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.MyJpaConfig;
import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.TestApplication;
import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.entity.PetEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;

import static com.github.jferrater.opa.ast.db.query.core.TestBase.opaCompilerResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {
                PetEntity.class,
                MyJpaConfig.class,
                TestApplication.class
        }
)
@ActiveProfiles("test")
class TypedQueryBuilderTest {

    private TypedQueryBuilder target;

    @Autowired
    EntityManager entityManager;

    private static OpaCompilerResponse opaCompilerResponse;

    @DisplayName("Given a sample Open Policy Agent compiler API response")
    @BeforeAll
    static void init() throws IOException {
        opaCompilerResponse = opaCompilerResponse();
    }

    @BeforeEach
    void setUp() {
        target = new TypedQueryBuilder<PetEntity>(opaCompilerResponse, entityManager);
    }

    @Test
    void shouldReturnCriteria() {
        TypedQuery<PetEntity> result = target.getTypedQuery(PetEntity.class, Sort.unsorted());
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicate() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PetEntity> criteriaQuery = criteriaBuilder.createQuery(PetEntity.class);
        Root<PetEntity> root = criteriaQuery.from(PetEntity.class);
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", "alice");

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }
}