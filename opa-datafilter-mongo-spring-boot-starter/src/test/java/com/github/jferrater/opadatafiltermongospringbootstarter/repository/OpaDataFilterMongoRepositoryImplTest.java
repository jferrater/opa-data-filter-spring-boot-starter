package com.github.jferrater.opadatafiltermongospringbootstarter.repository;

import com.github.jferrater.opadatafiltermongospringbootstarter.query.MongoQueryService;
import com.github.jferrater.opadatafiltermongospringbootstarter.repository.document.PetDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpaDataFilterMongoRepositoryImplTest {

    @Mock
    MongoOperations mongoOperations;
    @Mock
    MongoEntityInformation<PetDocument, String> entityInformation;
    @Mock
    MongoQueryService<PetDocument> queryService;

    OpaDataFilterMongoRepositoryImpl<PetDocument, String> target;

    @BeforeEach
    void setUp() {
        target = new OpaDataFilterMongoRepositoryImpl<>(entityInformation, mongoOperations, queryService);
    }

    @Test
    void shouldFindAll() {
        Query query = new Query();
        when(queryService.getMongoDBQuery()).thenReturn(query);
        when(entityInformation.getJavaType()).thenReturn(PetDocument.class);
        when(entityInformation.getCollectionName()).thenReturn("pets");
        when(mongoOperations.find(query, PetDocument.class, "pets")).thenReturn(List.of(new PetDocument()));

        List<PetDocument> result = target.findAll();

        assertThat(result.size(), is(1));
    }

    @Test
    void shouldFindAllWithSort() {
        Sort sort = Sort.by("name");
        PetDocument petDocument1 = new PetDocument();
        petDocument1.setName("fluffy");
        PetDocument petDocument2 = new PetDocument();
        petDocument2.setName("browny");
        Query query = new Query();
        when(queryService.getMongoDBQuery()).thenReturn(query);
        when(entityInformation.getJavaType()).thenReturn(PetDocument.class);
        when(entityInformation.getCollectionName()).thenReturn("pets");
        when(mongoOperations.find(query.with(sort), PetDocument.class, "pets")).thenReturn(List.of(petDocument2, petDocument1));

        List<PetDocument> result = target.findAll(sort);

        assertThat(result.size(), is(2));
        assertThat(result.get(0).getName(), is("browny"));
    }

    @Test
    void shouldFindAllWithPageable() {
        Sort sort = Sort.by("name");
        PetDocument petDocument1 = new PetDocument();
        petDocument1.setName("fluffy");
        PetDocument petDocument2 = new PetDocument();
        petDocument2.setName("browny");
        Query query = new Query();
        when(queryService.getMongoDBQuery()).thenReturn(query);
        when(entityInformation.getJavaType()).thenReturn(PetDocument.class);
        when(entityInformation.getCollectionName()).thenReturn("pets");
        when(mongoOperations.find(query.with(sort), PetDocument.class, "pets")).thenReturn(List.of(petDocument2, petDocument1));

        Page<PetDocument> result = target.findAll(PageRequest.of(1, 1, Sort.by("name")));

        assertThat(result, is(notNullValue()));
        assertThat(result.getSize(), is(1));
    }
}