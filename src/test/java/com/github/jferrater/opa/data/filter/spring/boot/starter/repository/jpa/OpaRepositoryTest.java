package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.entity.PetEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {
                PetEntity.class,
                MyJpaConfig.class,
                TestApplication.class
        }
)
@ActiveProfiles("test")
@Disabled
public class OpaRepositoryTest {

    private static final String QUERY = "SELECT * FROM pets WHERE (pets.owner = 'alice' AND pets.name = 'fluffy') OR (pets.veterinarian = 'alice' AND pets.clinic = 'SOMA' AND pets.name = 'fluffy');";

    @Autowired
    private MyService target;

    @MockBean
    private OpaClientService opaClientService;

    @Test
    @Transactional
    void shouldListFilteredData() {
        when(opaClientService.getExecutableSqlStatements()).thenReturn(QUERY);

        List<PetEntity> results = target.getPets();

        assertThat(results.size(), is(1));
    }

}
