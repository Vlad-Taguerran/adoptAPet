package com.adoptapet.adotapet.ropository;

import com.adoptapet.adotapet.entity.pet.Category;
import com.adoptapet.adotapet.entity.pet.PetEntity;
import com.adoptapet.adotapet.entity.pet.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface PetRepository extends JpaRepository<PetEntity, UUID>, JpaSpecificationExecutor<PetEntity> {
    @Query("SELECT p FROM PetEntity p WHERE p.active = true")
    List<PetEntity> findAllPets();

    @Modifying
    @Query("UPDATE PetEntity p SET p.active = false WHERE p.id = :id ")
    void deletePet(@Param("id") UUID id);

    @Query("SELECT p FROM PetEntity p WHERE p.id = :id AND p.active = true")
    Optional<PetEntity> findByActive(@Param("id") UUID id);

    @Query("SELECT p FROM PetEntity p WHERE p.active = true " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:category IS NULL OR p.category = :category) "+ "AND (:status IS NULL OR p.status = :status )" )
    List<PetEntity> searchPets(@Param("name") String name,
                               @Param("category") Category category,
                               @Param("status")Status status);
}