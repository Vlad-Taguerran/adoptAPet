package com.adoptapet.adotapet.entity.pet;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_pet")
public class PetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Category category;

    @Column
    private Status status;

    @Column
    private Date bornIn;

    @Column
    private String urlImage;


}