package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import com.github.jferrater.opadatafilterjpaspringbootstarter.entity.PetEntity;
import com.github.jferrater.opadatafilterjpaspringbootstarter.query.QueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpaDataFilterRepositoryImplTest {

    @Mock
    QueryService queryService;
    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder builder;
    @Mock
    CriteriaQuery<PetEntity> criteriaQuery;
    @Mock
    CriteriaQuery<Long> countCriteriaQuery;
    @Mock
    TypedQuery<PetEntity> query;
    @Mock
    TypedQuery<Long> countQuery;
    @Mock
    JpaEntityInformation<PetEntity, Integer> information;
    @Mock
    CrudMethodMetadata metadata;

    OpaDataFilterRepositoryImpl<PetEntity, Long> target;

    @BeforeEach
    void setUp() {
        when(entityManager.getDelegate()).thenReturn(entityManager);
        when(information.getJavaType()).thenReturn(PetEntity.class);

        target = new OpaDataFilterRepositoryImpl<>(information, entityManager, queryService);
        target.setRepositoryMethodMetadata(metadata);
    }

    @Test
    void shouldListData() {
        PetEntity petEntity = new PetEntity();
        petEntity.setName("browny");
        petEntity.setOwner("dodong");
        when(queryService.getTypedQuery(PetEntity.class, Sort.unsorted(), entityManager)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(petEntity));

        List<PetEntity> result = target.findAll();

        assertThat(result.size(), is(1));
    }

    @Test
    void shouldListSortedData() {
        PetEntity petEntity1 = new PetEntity();
        petEntity1.setName("browny");
        petEntity1.setOwner("dodong");
        PetEntity petEntity2 = new PetEntity();
        petEntity2.setName("asol");
        petEntity2.setOwner("gerry");
        when(queryService.getTypedQuery(PetEntity.class, Sort.by("name"), entityManager)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(petEntity2, petEntity1));

        List<PetEntity> result = target.findAll(Sort.by("name"));

        assertThat(result.size(), is(2));
        assertThat(result.get(0).getName(), is("asol"));
    }

    @Test
    public void retrieveObjectsForPageableOutOfRange() {
        PetEntity petEntity1 = new PetEntity();
        petEntity1.setName("browny");
        petEntity1.setOwner("dodong");
        PetEntity petEntity2 = new PetEntity();
        petEntity2.setName("asol");
        petEntity2.setOwner("gerry");
        when(queryService.getTypedQuery(PetEntity.class, Sort.sort(PetEntity.class), entityManager)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(petEntity2, petEntity1));

        Page<PetEntity> all = target.findAll(PageRequest.of(2, 10));

        assertThat(all, is(notNullValue()));
        assertThat(all.getSize(), is(10));
    }
}