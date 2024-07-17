package com.adoptapet.adotapet.pet;

import com.adoptapet.adotapet.configure.FileStorageConfig;
import com.adoptapet.adotapet.dto.PetDto;
import com.adoptapet.adotapet.entity.pet.Category;
import com.adoptapet.adotapet.entity.pet.PetEntity;
import com.adoptapet.adotapet.entity.pet.Status;
import com.adoptapet.adotapet.ropository.PetRepository;
import com.adoptapet.adotapet.services.PetImplementServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetTest {
    @Mock
   private FileStorageConfig fileStorageConfig;

    //@InjectMocks
   // PetImplementServices petService;


    @Mock
    PetRepository repository;

    PetEntity petEntity;
    PetDto petDto;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);


        when(fileStorageConfig.getUploadDir()).thenReturn("Image");
        System.out.println("TESTE DE CAMINHO"+fileStorageConfig.getUploadDir());
       // petService = new PetImplementServices(fileStorageConfig);
    }
    @Test
     void testCreateAPet(){
        // Registrar o valor de retorno de getUploadDir()
        System.out.println("Valor de uploadDir: " + fileStorageConfig.getUploadDir());
//
//        petDto = new PetDto();
//        petDto.setName("DOG");
//        petDto.setCategory(Category.Cachorro);
//        petDto.setStatus(Status.Disponivel);
//        petDto.setBornIn(new Date());
//        petDto.setDescription("Test");
//
//        MockMultipartFile file = new MockMultipartFile(
//                "image",
//                "test.jpg",
//                "image/jpeg",
//                "test image content".getBytes()
//        );
//        PetEntity petEntity = new PetEntity();
//        petEntity.setName("Test Pet");
//        petEntity.setBornIn(new Date());
//        petEntity.setCategory(Category.Cachorro);
//        petEntity.setStatus(Status.Disponivel);
//        petEntity.setDescription("Test");
//        petEntity.setUrlImage("http://localhost/images/test.jpg");
//
//
//        when(repository.save(petEntity)).thenReturn(petEntity);
//
//        ResponseEntity<PetEntity> response = petService.createPet(petDto, file);
//
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Test Pet", response.getBody().getName());
//        verify(repository, times(1)).save(any(PetEntity.class));
    }
}
