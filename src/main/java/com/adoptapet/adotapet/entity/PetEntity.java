package com.adoptapet.adotapet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
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
    private int age;

    @Column
    private String description;

    @Column
    private String category;

    @Column
    private Date bornIn;

    @Column
    private String urlImage;


}