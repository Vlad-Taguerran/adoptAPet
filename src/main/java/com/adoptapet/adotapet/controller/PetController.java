package com.adoptapet.adotapet.controller;

import com.adoptapet.adotapet.dto.PetDto;
import com.adoptapet.adotapet.entity.PetEntity;
import com.adoptapet.adotapet.services.PetImplementServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/pet")
public class PetController {
    @Autowired
    PetImplementServices petService;
    @GetMapping
    public ResponseEntity<PetDto> getPet(@PathVariable UUID id) {
       return petService.getPetById(id);
    }
    @GetMapping("/search")
    public ResponseEntity<Set<PetEntity>> searchPet(@RequestParam String name, @RequestParam Integer age, @RequestParam String category) {
        return petService.searchPet(name, age, category);
    }
    @PostMapping
    public ResponseEntity<PetEntity> createPet(@RequestBody PetDto petDto) {
        return petService.createPet(petDto);
    }
    @PatchMapping
    public ResponseEntity<PetEntity> updatePet(@RequestBody PetDto petDto, @PathVariable UUID id) {
        return petService.updatePet(id,petDto);
    }
    @DeleteMapping
    public ResponseEntity<String> deletePet(@PathVariable UUID id) {
        return petService.deletePet(id);
    }

}
