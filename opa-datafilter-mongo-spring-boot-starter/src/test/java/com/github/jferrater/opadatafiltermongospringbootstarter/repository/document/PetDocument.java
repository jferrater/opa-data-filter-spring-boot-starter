package com.github.jferrater.opadatafiltermongospringbootstarter.repository.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDocument {

    @MongoId
    private String id;
    @Indexed
    private String name;
    @Indexed
    private String veterinarian;
    @Indexed
    private String owner;
    @Indexed
    private String clinic;
}