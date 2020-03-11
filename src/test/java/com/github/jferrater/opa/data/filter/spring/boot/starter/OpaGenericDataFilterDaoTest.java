package com.github.jferrater.opa.data.filter.spring.boot.starter;

import static org.hamcrest.CoreMatchers.is;

import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {
                OpaGenericDataFilterDaoTest.MyRepository.class,
                OpaGenericDataFilterDaoTest.Pet.class,
                PersistenceConfig.class
        }
)
class OpaGenericDataFilterDaoTest {

    @Autowired
    private MyRepository target;

    @Test
    void shouldFilterData() {
        PartialRequest partialRequest = new PartialRequest();
        List<Pet> results = target.filterData(partialRequest);

        assertThat(results.size(), is(1));
    }

    @Repository
    class MyRepository extends OpaGenericDataFilterDao<Pet> {

        public MyRepository() {
            setClazz(Pet.class);
        }
    }

    @Entity
    @Table(name = "pets")
    class Pet {

        @Id
        private Long id;
        private String name;
        private String veterinarian;
        private String owner;
        private String clinic;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVeterinarian() {
            return veterinarian;
        }

        public void setVeterinarian(String veterinarian) {
            this.veterinarian = veterinarian;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getClinic() {
            return clinic;
        }

        public void setClinic(String clinic) {
            this.clinic = clinic;
        }
    }
}