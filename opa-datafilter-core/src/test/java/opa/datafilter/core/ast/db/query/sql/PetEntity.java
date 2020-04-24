package opa.datafilter.core.ast.db.query.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author joffryferrater
 */
@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetEntity {

    @Id
    private Long id;
    private String name;
    private String veterinarian;
    private String owner;
    private String clinic;
}
