package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import com.github.jferrater.opadatafilterjpaspringbootstarter.config.PersistenceConfig;
import com.github.jferrater.opadatafilterjpaspringbootstarter.entity.PetEntity;
import opa.datafilter.core.ast.db.query.exception.PartialEvauationException;
import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author joffryferrater
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {
                PersistenceConfig.class,
                MyJpaConfig.class,
                OpaGenericDataFilterDao.class,
                MyRepository.class,
                TestApplication.class
        }
)
@ActiveProfiles("test")
class OpaGenericDataFilterDaoTest {

    private static final String QUERY = "SELECT * FROM pets WHERE (pets.owner = 'alice' AND pets.name = 'fluffy') OR (pets.veterinarian = 'alice' AND pets.clinic = 'SOMA' AND pets.name = 'fluffy');";

    @Autowired
    private MyRepository target;
    @MockBean
    private OpaClientService opaClientService;

    @DisplayName(
            "Given a classpath:application-test.yml profile"
    )
    @Test
    void shouldFilterData() {
        PartialRequest partialRequest = mock(PartialRequest.class);
        when(opaClientService.getExecutableSqlStatements(partialRequest)).thenReturn(QUERY);

        List<PetEntity> results = target.filterData(partialRequest);

        assertThat(results.size(), is(1));
        PetEntity pet = results.get(0);
        assertThat(pet.getName(), is("fluffy"));
        assertThat(pet.getOwner(), is("alice"));
        assertThat(pet.getVeterinarian(), is("alice"));
        assertThat(pet.getClinic(), is("SOMA"));
    }

    @DisplayName(
            "Given a classpath:application-test.yml profile"
    )
    @Test
    void shouldFilterDataUsingDefaultPartialRequest() {
        when(opaClientService.getExecutableSqlStatements()).thenReturn(QUERY);

        List<PetEntity> results = target.filterData();

        assertThat(results.size(), is(1));
        PetEntity pet = results.get(0);
        assertThat(pet.getName(), is("fluffy"));
        assertThat(pet.getOwner(), is("alice"));
        assertThat(pet.getVeterinarian(), is("alice"));
        assertThat(pet.getClinic(), is("SOMA"));
    }

    @DisplayName(
            "Given a classpath:application-test.yml profile"
    )
    @Test
    void shouldThrowPartialEvaluationExceptionDefaultPartialRequest() {
        when(opaClientService.getExecutableSqlStatements()).thenReturn("SELECT * FROM pets;");

        assertThrows(PartialEvauationException.class, () -> target.filterData());
    }

    @DisplayName(
            "Given a classpath:application-test.yml profile"
    )
    @Test
    void shouldThrowPartialEvaluationException() {
        PartialRequest partialRequest = mock(PartialRequest.class);
        when(opaClientService.getExecutableSqlStatements(partialRequest)).thenReturn("SELECT * FROM pets;");

        assertThrows(PartialEvauationException.class, () -> target.filterData(partialRequest));
    }
}