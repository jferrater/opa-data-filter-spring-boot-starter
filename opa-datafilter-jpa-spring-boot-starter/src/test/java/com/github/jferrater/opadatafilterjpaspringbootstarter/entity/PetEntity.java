package com.github.jferrater.opadatafilterjpaspringbootstarter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pets")
public class PetEntity {

    @Id
    private Long id;
    private String name;
    private String veterinarian;
    private String owner;
    private String clinic;

    public PetEntity() {
    }

    public PetEntity(String name, String veterinarian, String owner, String clinic) {
        this();
        this.name = name;
        this.veterinarian = veterinarian;
        this.owner = owner;
        this.clinic = clinic;
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
