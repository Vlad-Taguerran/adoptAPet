package com.adoptapet.adotapet.pet;

import com.adoptapet.adotapet.configure.FileStorageConfig;
import com.adoptapet.adotapet.dto.PetDto;
import com.adoptapet.adotapet.entity.pet.Category;
import com.adoptapet.adotapet.entity.pet.PetEntity;
import com.adoptapet.adotapet.entity.pet.Status;
import com.adoptapet.adotapet.ropository.PetRepository;
import com.adoptapet.adotapet.services.PetImplementServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PetServicesTest {

    @Mock
    private PetRepository repository;

    @Mock
    private FileStorageConfig fileStorageConfig;


    private PetImplementServices petService;

    private Path tempDir;

    PetDto petDto;
    PetEntity petEntity;
    UUID petId;

    @BeforeEach
    public void setUp() throws IOException, ParseException {
        MockitoAnnotations.openMocks(this);
         UUID.randomUUID();

        tempDir = Files.createTempDirectory("test-images");
        when(fileStorageConfig.getUploadDir()).thenReturn(tempDir.toString());

        petService = new PetImplementServices(fileStorageConfig, repository);


        petEntity = new PetEntity();
        petEntity.setName("Test Pet");
        petEntity.setBornIn(new SimpleDateFormat("dd/MM/yyyy").parse("20/01/1998"));
        petEntity.setCategory(Category.Cachorro);
        petEntity.setStatus(Status.Disponível);
        petEntity.setDescription("Test");
        petEntity.setUrlImage("http://localhost/images/test.jpg");



        petDto = new PetDto();
        petDto.setName("DOG");
        petDto.setCategory(Category.Cachorro);
        petDto.setStatus(Status.Disponível);
        petDto.setBornIn(new SimpleDateFormat("dd/MM/yyyy").parse("20/01/1998"));
        petDto.setDescription("Test");

    }

    @Test
    @DisplayName("Shoud create a Pet")
    void testCreateAPetWithImageSaved() throws Exception {

        Path tempFile = tempDir.resolve("test.jpg");
        Files.write(tempFile, "test image content".getBytes());
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                Files.readAllBytes(tempFile)
        );

        when(repository.save(any(PetEntity.class))).thenReturn(petEntity);
        ResponseEntity<PetEntity> response = petService.createPet(petDto, file);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Pet", response.getBody().getName());


        verify(repository, times(1)).save(any(PetEntity.class));
    }

    @Test
    @DisplayName("Update a pet")
    void  testeUpdateAPet()throws Exception{



        PetEntity existingPet =  petEntity;
        existingPet.setId(petId);  // Simula um pet existente com o ID gerado
        existingPet.setName("Existing Pet");
        existingPet.setBornIn(new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2015"));
        existingPet.setCategory(Category.Gato);
        existingPet.setStatus(Status.Adotado);
        existingPet.setDescription("Existing Description");
        existingPet.setUrlImage("http://localhost/images/existing.jpg");


        when(repository.findById(petId)).thenReturn(java.util.Optional.of(existingPet));


        PetDto updatedPetDto = petDto;
        updatedPetDto.setName("Updated Pet");
        updatedPetDto.setCategory(Category.Cachorro);
        updatedPetDto.setStatus(Status.Disponível);
        updatedPetDto.setBornIn(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2022"));
        updatedPetDto.setDescription("Updated Description");


        PetEntity updatedPetEntity = new PetEntity();
        updatedPetEntity.setId(existingPet.getId());
        updatedPetEntity.setName(updatedPetDto.getName());
        updatedPetEntity.setBornIn(updatedPetDto.getBornIn());
        updatedPetEntity.setCategory(updatedPetDto.getCategory());
        updatedPetEntity.setStatus(updatedPetDto.getStatus());
        updatedPetEntity.setDescription(updatedPetDto.getDescription());
        updatedPetEntity.setUrlImage(existingPet.getUrlImage());
        when(repository.save(any(PetEntity.class))).thenReturn(updatedPetEntity);


        ResponseEntity<PetEntity> response = petService.updatePet(petId, updatedPetDto);


        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedPetDto.getName(), response.getBody().getName());
        assertEquals(updatedPetDto.getDescription(), response.getBody().getDescription());

        verify(repository, times(1)).findById(petId);

        verify(repository, times(1)).save(any(PetEntity.class));
    }

    @Test
    @DisplayName("Shoud get a pet by ID")
    void testeGetAPetById() throws Exception{
       PetEntity existing = petEntity;
       existing.setId(petId);

       when(repository.findById(petId)).thenReturn(Optional.of(existing));
       ResponseEntity<PetDto> response = petService.getPetById(petId);
       assertEquals(petId,response.getBody().getId());
       verify(repository, times(1)).findById(petId);
    }

    @Test
    @DisplayName("Shoud return a list whith pets")
    void getPets() throws Exception{
        when(repository.findAll()).thenReturn(Collections.singletonList(petEntity));

       ResponseEntity<List<PetEntity>> response = petService.getPets();
       List<PetEntity> pets = response.getBody();

       assertEquals(Collections.singletonList(petEntity), pets);
       verify(repository).findAll();
       verifyNoMoreInteractions(repository);

    }
}