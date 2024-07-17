package com.adoptapet.adotapet.dto;

import com.adoptapet.adotapet.entity.pet.Category;
import com.adoptapet.adotapet.entity.pet.PetEntity;
import com.adoptapet.adotapet.entity.pet.Status;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link PetEntity}
 */
@Data
public class PetDto implements Serializable {
    UUID id;
    String name;
    int age;
    String description;
    Category category;
    Status status;
    Date bornIn;
    String urlImage;
}