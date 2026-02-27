package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeOfMeasurementRepository extends JpaRepository<TypeOfMeasurement, Integer> {

    @Query("SELECT COUNT(t) > 0 FROM TypeOfMeasurement t WHERE t.typeName = :typeName")
    boolean existsByTypeName(String typeName);
}
