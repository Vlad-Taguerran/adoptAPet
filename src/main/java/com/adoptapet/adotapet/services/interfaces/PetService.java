package com.adoptapet.adotapet.services.interfaces;

import com.adoptapet.adotapet.dto.PetDto;
import com.adoptapet.adotapet.entity.PetEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PetService {
    ResponseEntity<PetEntity>  createPet(PetDto pet);
    ResponseEntity<PetEntity> updatePet(UUID id, PetDto pet);
    ResponseEntity<String> deletePet(UUID id);
    ResponseEntity<PetDto> getPetById(UUID id);
    PetEntity getPetByName(String petName);
    List<PetEntity> getPets();

}
