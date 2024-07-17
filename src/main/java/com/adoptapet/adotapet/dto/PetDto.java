package com.adoptapet.adotapet.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link com.adoptapet.adotapet.entity.PetEntity}
 */
@Data
public class PetDto implements Serializable {
    UUID id;
    String name;
    int age;
    String description;
    String category;
    Date bornIn;
    String urlImage;
}