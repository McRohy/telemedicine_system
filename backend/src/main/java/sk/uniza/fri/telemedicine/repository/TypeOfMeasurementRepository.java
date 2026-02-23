package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeOfMeasurementRepository extends JpaRepository<TypeOfMeasurement, Integer> {

    @Query(value = "SELECT t.typeName FROM TypeOfMeasurement t ORDER BY t.typeName ASC")
    List<TypeOfMeasurement> findAllTypeNames();

}
