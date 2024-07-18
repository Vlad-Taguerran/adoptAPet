package com.adoptapet.adotapet.services;

import com.adoptapet.adotapet.configure.FileStorageConfig;
import com.adoptapet.adotapet.dto.PetDto;
import com.adoptapet.adotapet.entity.pet.PetEntity;
import com.adoptapet.adotapet.ropository.PetRepository;
import com.adoptapet.adotapet.services.interfaces.PetService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class PetImplementServices implements PetService {
    protected PetEntity petEntity = new PetEntity();
    private final Path storageLocation;

    @Autowired
    public PetImplementServices(FileStorageConfig storageLocation, PetRepository repository) {
        this.storageLocation = Paths.get(storageLocation.getUploadDir()).toAbsolutePath().normalize();
        System.out.println("Initializing PetImplementServices with storage location: " + this.storageLocation);
        this.repository = repository;
    }
    @Autowired
    private PetRepository repository;

    @Override
    public ResponseEntity<PetEntity> createPet(PetDto petDto, MultipartFile petImage) {
        String fileName = StringUtils.getFilename(Objects.requireNonNull(petImage.getOriginalFilename()));
        try{
            System.out.println("Creating pet: " + storageLocation);
            Path targetLocation = storageLocation.resolve(fileName);
            System.out.println("TESTE"+targetLocation);
            petImage.transferTo(targetLocation);
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(fileName)
                    .toUriString();
            petDto.setUrlImage(fileUrl);
            BeanUtils.copyProperties(petDto, petEntity);
            PetEntity saved = repository.save(petEntity);
            return ResponseEntity.ok().body(saved);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }

    }

    @Override
    public ResponseEntity<PetEntity> updatePet(UUID id, PetDto pet) {
       Optional<PetEntity> petFind = repository.findById(id);
       if (petFind.isPresent()) {
           BeanUtils.copyProperties(pet, petFind.get(), "id");
           PetEntity saved = repository.save(petFind.get());
           return ResponseEntity.ok().body(saved);
       }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> deletePet(UUID id) {
        Optional<PetEntity> petFind = repository.findById(id);
        if(petFind.isPresent()) {
            repository.delete(petFind.get());
            return ResponseEntity.ok().body("Pet deletado com sucesso !");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<PetDto> getPetById(UUID id) {
      PetDto  dto = new PetDto();
        Optional<PetEntity> pet = repository.findById(id);
        if(pet.isPresent()) {
            BeanUtils.copyProperties(pet.get(),dto);
            return ResponseEntity.ok().body(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public PetEntity getPetByName(String petName) {
        return null;
    }


    public ResponseEntity<Set<PetEntity>> searchPet(String petName, Integer age, String category) {
        Set<PetEntity> resultSet = new HashSet<PetEntity>(repository.findAll((Root<PetEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->{
             Set<Predicate> predicates_ = new HashSet<>();
             if(petName != null) {
                 predicates_.add( criteriaBuilder.equal(root.get("petName"), petName));
             }
             if(age != null) {
                 predicates_.add((Predicate) criteriaBuilder.equal(root.get("age"), age));
             }
             if(category != null) {
                 predicates_.add((Predicate) criteriaBuilder.equal(root.get("category"), category));
             }
             return criteriaBuilder.and(predicates_.toArray(new Predicate[0]));
         }));
        return ResponseEntity.ok().body(resultSet);
    }


    @Override
    public ResponseEntity<List<PetEntity>> getPets() {
       List<PetEntity> pets = repository.findAll();
       return ResponseEntity.ok().body(pets);

    }
}
