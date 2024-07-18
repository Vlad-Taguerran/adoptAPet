package com.adoptapet.adotapet.dto;

import com.adoptapet.adotapet.entity.pet.Category;
import com.adoptapet.adotapet.entity.pet.PetEntity;
import com.adoptapet.adotapet.entity.pet.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Nome é requirido")
    @Size(min = 3,message = "Nome deve conter no minimo 3 letras")
    String name;
    String description;
    @NotNull(message = "Categoria requerida")
    Category category;
    Status status;
    Date bornIn;
    String urlImage;
}