package com.adoptapet.adotapet.services;

import com.adoptapet.adotapet.configure.FileStorageConfig;
import com.adoptapet.adotapet.configure.exceptions.PetImageExeption;
import com.adoptapet.adotapet.configure.exceptions.PetNotFound;
import com.adoptapet.adotapet.dto.PetDto;
import com.adoptapet.adotapet.entity.pet.Category;
import com.adoptapet.adotapet.entity.pet.PetEntity;
import com.adoptapet.adotapet.entity.pet.Status;
import com.adoptapet.adotapet.ropository.PetRepository;
import com.adoptapet.adotapet.services.interfaces.PetService;
import jakarta.transaction.Transactional;
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
@Transactional
public class PetImplementServices implements PetService {
    protected PetEntity petEntity = new PetEntity();
    private final Path storageLocation;
    @Autowired
    SseService sseService;

    @Autowired
    public PetImplementServices(FileStorageConfig storageLocation, PetRepository repository) {
        this.storageLocation = Paths.get(storageLocation.getUploadDir()).toAbsolutePath().normalize();
        this.repository = repository;
    }

    @Autowired
    private PetRepository repository;

    @Override
    public ResponseEntity<PetEntity> createPet(PetDto petDto, MultipartFile petImage) {
        BeanUtils.copyProperties(petDto, petEntity);

        if(petImage != null){
         String fileUrl = saveImage(petImage);
            petDto.setUrlImage(fileUrl);
            BeanUtils.copyProperties(petDto, petEntity);
            PetEntity saved = repository.save(petEntity);
            sseService.sendUpdate(petsStream());
            return ResponseEntity.ok().body(saved);

        }
        PetEntity saved = repository.save(petEntity);
        sseService.sendUpdate(petsStream());
        return ResponseEntity.ok().body(saved);

    }

    @Override
    public ResponseEntity<PetEntity> updatePet(UUID id, PetDto pet) {

        PetEntity petFind = repository.findByActive(id).orElseThrow(()->new PetNotFound("Pet não encontrado"));
        if (!petFind.getUrlImage().isEmpty()) {

        }
        BeanUtils.copyProperties(pet, petFind, "id");
        PetEntity saved = repository.save(petFind);
        return ResponseEntity.ok().body(saved);
    }

    @Override
    public ResponseEntity<String> deletePet(UUID id) {
        Optional<PetEntity> petFind = repository.findByActive(id);
        if (petFind.isPresent()) {
            repository.deletePet(petFind.get().getId());
            return ResponseEntity.ok().body("Pet deletado com sucesso !");
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PetDto> getPetById(UUID id) {
        PetDto dto = new PetDto();
        Optional<PetEntity> pet = repository.findByActive(id);
        if (pet.isPresent()) {
            BeanUtils.copyProperties(pet.get(), dto);
            return ResponseEntity.ok().body(dto);
        }
        throw new PetNotFound("Pet não encontrado");
    }

    @Override
    public PetEntity getPetByName(String petName) {
        return null;
    }


    public ResponseEntity<Set<PetEntity>> searchPet(String petName, String status, String category) {

        Category categoryEnum = null;
        if (category != null && !category.isEmpty()) {
            try {
                categoryEnum = Category.valueOf(category);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        Status statusEnum = null;
        if (status != null && !status.isEmpty()) {
            try {
                statusEnum = Status.valueOf(status);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build(); // Retorna erro 400 se o status não for válido
            }
        }

        Set<PetEntity> resultSet;
        if (categoryEnum != null && statusEnum != null) {
            resultSet = new HashSet<>(repository.searchPets(petName, categoryEnum, statusEnum));
        } else if (categoryEnum != null) {
            resultSet = new HashSet<>(repository.searchPets(petName, categoryEnum, null));
        } else if (statusEnum != null) {
            resultSet = new HashSet<>(repository.searchPets(petName, null, statusEnum));
        } else {
            resultSet = new HashSet<>(repository.searchPets(petName, null, null));
        }
        if(resultSet.isEmpty()){
            throw new PetNotFound("Nem um pet encontrado");
        }
        return ResponseEntity.ok().body(resultSet);

    }

    @Override
    public ResponseEntity<List<PetEntity>> getPets() {
       List<PetEntity> pets = repository.findAllPets();
       return ResponseEntity.ok().body(pets);

    }
    public List<PetDto> petsStream(){
        List<PetEntity> pets = repository.findAll();
        return pets.stream().map(this::convertToDto).toList();
    }
    private PetDto convertToDto(PetEntity petEntity) {
        PetDto petDto = new PetDto();
        BeanUtils.copyProperties(petEntity, petDto);
        return petDto;
    }

    private String saveImage(MultipartFile petImage){
        if(petImage != null){
            String fileName = StringUtils.getFilename(Objects.requireNonNull(petImage.getOriginalFilename()));
            try {
                Path targetLocation = storageLocation.resolve(fileName);
                petImage.transferTo(targetLocation);
                String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/images/")
                        .path(fileName)
                        .toUriString();
                return fileUrl;
            } catch (Exception e) {

                throw new PetImageExeption("Erro ao salvar imagem");
            }

        }
        return null;
    }
}
