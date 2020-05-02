package com.github.jferrater.opadatafilterjpaspringbootstarter.query;

import com.github.jferrater.opadatafilterjpaspringbootstarter.entity.PetEntity;
import com.github.jferrater.opadatafilterjpaspringbootstarter.repository.MyJpaConfig;
import com.github.jferrater.opadatafilterjpaspringbootstarter.repository.TestApplication;
import opa.datafilter.core.ast.db.query.elements.SqlPredicate;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
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

import static com.github.jferrater.opadatafilterjpaspringbootstarter.repository.TestBase.opaCompilerResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author joffryferrater
 */
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

    private TypedQueryBuilder<PetEntity> target;

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
        target = new TypedQueryBuilder<>(opaCompilerResponse, entityManager);
    }

    @Test
    void shouldReturnCriteria() {
        TypedQuery<PetEntity> result = target.getTypedQuery(PetEntity.class, Sort.unsorted());
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateEqualOperatorStringType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", "alice");

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateEqualOperatorBooleanType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", true);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateEqualOperatorIntegerType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", 5);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateEqualOperatorLongType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", 5L);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateEqualOperatorDoubleType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", 3.145);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateLTOperatorWithIntegerType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "<", 6);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateLTOperatorWithLongType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "<", 6L);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateLTOperatorWithDoubleType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "<", 4.12);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateLTEOperatorWithIntegerType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "<=", 6);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateLTEOperatorWithLongType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "<=", 2L);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateLTEOperatorWithDoubleType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "<=", 3.1354695);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateGTOperatorWithIntegerType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", ">", 405);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateGTOperatorWithLongType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", ">", 405L);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateGTOperatorWithDoubleType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", ">", 4.34);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateGTEOperatorIntegerType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", ">=", 3);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateGTEOperatorWithLongType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", ">=", 3L);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldCreatePredicateGTEOperatorWithDoubleType() {
        Root<PetEntity> root = getPetEntityRoot();
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", ">=", 21.4535);

        Predicate result = target.createPredicate(sqlPredicate1, root);
        assertThat(result, is(notNullValue()));
    }

    private Root<PetEntity> getPetEntityRoot() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PetEntity> criteriaQuery = criteriaBuilder.createQuery(PetEntity.class);
        return criteriaQuery.from(PetEntity.class);
    }
}