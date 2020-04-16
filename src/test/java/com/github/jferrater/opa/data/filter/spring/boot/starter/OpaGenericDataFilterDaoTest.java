package com.github.jferrater.opa.data.filter.spring.boot.starter;

import com.github.jferrater.opa.ast.db.query.model.request.PartialRequest;
import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {
                PersistenceConfig.class,
                OpaGenericDataFilterDao.class,
                MyRepository.class
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
    @Transactional
    void shouldFilterData() {
        PartialRequest partialRequest = mock(PartialRequest.class);
        when(opaClientService.getExecutableSqlStatements(partialRequest)).thenReturn(QUERY);

        List<Pet> results = target.filterData(partialRequest);

        assertThat(results.size(), is(1));
        Pet pet = results.get(0);
        assertThat(pet.getName(), is("fluffy"));
        assertThat(pet.getOwner(), is("alice"));
        assertThat(pet.getVeterinarian(), is("alice"));
        assertThat(pet.getClinic(), is("SOMA"));
    }

    @DisplayName(
            "Given a classpath:application-test.yml profile"
    )
    @Test
    @Transactional
    void shouldFilterDataUsingDefaultPartialRequest() {
        when(opaClientService.getExecutableSqlStatements()).thenReturn(QUERY);

        List<Pet> results = target.filterData();

        assertThat(results.size(), is(1));
        Pet pet = results.get(0);
        assertThat(pet.getName(), is("fluffy"));
        assertThat(pet.getOwner(), is("alice"));
        assertThat(pet.getVeterinarian(), is("alice"));
        assertThat(pet.getClinic(), is("SOMA"));
    }
}