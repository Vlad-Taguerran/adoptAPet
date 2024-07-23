package com.adoptapet.adotapet.controller;

import com.adoptapet.adotapet.dto.PetDto;
import com.adoptapet.adotapet.entity.pet.PetEntity;
import com.adoptapet.adotapet.services.PetImplementServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/pet")
@Validated
public class PetController {
    @Autowired
    PetImplementServices petService;
    @GetMapping()
    public ResponseEntity<List<PetEntity>> getPet() {
        return petService.getPets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPet(@PathVariable UUID id) {
       return petService.getPetById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Set<PetEntity>> searchPet(@RequestParam(required = false) String name, @RequestParam(required = false) String age, @RequestParam(required = false) String category) {
        return petService.searchPet(name, age, category);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PetEntity> createPet(@Valid @ModelAttribute PetDto petDto, @RequestParam(required = false,name = "image")MultipartFile petImage) {

        return petService.createPet(petDto, petImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetEntity> updatePet(@Valid @RequestBody PetDto petDto, @PathVariable UUID id) {
        return petService.updatePet(id,petDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePet(@PathVariable UUID id) {
        return petService.deletePet(id);
    }

}
