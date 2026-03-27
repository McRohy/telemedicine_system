package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.telemedicine.entity.TypeOfMeasurement;

/**
 * Repository for accessing type of measurement data.
 */
public interface TypeOfMeasurementRepository extends JpaRepository<TypeOfMeasurement, Long> {

    @Query("SELECT COUNT(t) > 0 FROM TypeOfMeasurement t WHERE t.typeName = :typeName")
    boolean existsByTypeName(String typeName);

    @Query("SELECT t FROM TypeOfMeasurement t WHERE LOWER(t.typeName) LIKE LOWER(CONCAT(:searchTypeName, '%'))")
    Page<TypeOfMeasurement> findByTypeNameStartingWithIgnoreCase(String searchTypeName, Pageable pageable);

}
