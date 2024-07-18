package com.adoptapet.adotapet.pet;

import com.adoptapet.adotapet.configure.FileStorageConfig;
import com.adoptapet.adotapet.dto.PetDto;
import com.adoptapet.adotapet.entity.pet.Category;
import com.adoptapet.adotapet.entity.pet.PetEntity;
import com.adoptapet.adotapet.entity.pet.Status;
import com.adoptapet.adotapet.ropository.PetRepository;
import com.adoptapet.adotapet.services.PetImplementServices;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class PetControllerTest {

    @Mock
    private PetRepository repository;

    @Mock
    private FileStorageConfig fileStorageConfig;


    private PetImplementServices petService;

    private Path tempDir;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        tempDir = Files.createTempDirectory("test-images");
        when(fileStorageConfig.getUploadDir()).thenReturn(tempDir.toString());

        petService = new PetImplementServices(fileStorageConfig, repository);

    }

    @Test
    @DisplayName("Create a Pet")
    void testCreateAPetWithImageSaved() throws Exception {

        Path tempFile = tempDir.resolve("test.jpg");
        Files.write(tempFile, "test image content".getBytes());
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                Files.readAllBytes(tempFile)
        );


        PetEntity petEntity = new PetEntity();
        petEntity.setName("Test Pet");
        petEntity.setBornIn(new SimpleDateFormat("dd/MM/yyyy").parse("20/01/1998"));
        petEntity.setCategory(Category.Cachorro);
        petEntity.setStatus(Status.Disponivel);
        petEntity.setDescription("Test");
        petEntity.setUrlImage("http://localhost/images/test.jpg");
        when(repository.save(any(PetEntity.class))).thenReturn(petEntity);


        PetDto petDto = new PetDto();
        petDto.setName("DOG");
        petDto.setCategory(Category.Cachorro);
        petDto.setStatus(Status.Disponivel);
        petDto.setBornIn(new SimpleDateFormat("dd/MM/yyyy").parse("20/01/1998"));
        petDto.setDescription("Test");


        ResponseEntity<PetEntity> response = petService.createPet(petDto, file);


        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Pet", response.getBody().getName());


        verify(repository, times(1)).save(any(PetEntity.class));
    }

    @Test
    @DisplayName("Update a pet")
    void  testeUpdateAPet()throws Exception{

        UUID petId = UUID.randomUUID();

        PetEntity existingPet = new PetEntity();
        existingPet.setId(petId);  // Simula um pet existente com o ID gerado
        existingPet.setName("Existing Pet");
        existingPet.setBornIn(new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2015"));
        existingPet.setCategory(Category.Gato);
        existingPet.setStatus(Status.Adotado);
        existingPet.setDescription("Existing Description");
        existingPet.setUrlImage("http://localhost/images/existing.jpg");


        when(repository.findById(petId)).thenReturn(java.util.Optional.of(existingPet));


        PetDto updatedPetDto = new PetDto();
        updatedPetDto.setName("Updated Pet");
        updatedPetDto.setCategory(Category.Cachorro);
        updatedPetDto.setStatus(Status.Disponivel);
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
}