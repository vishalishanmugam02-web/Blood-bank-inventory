package com.college.bloodbank.repository;
import com.college.bloodbank.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long> {
    List<BloodUnit> findByStatusAndBloodGroupAndExpiryDateAfterOrderByExpiryDateAsc(BloodUnitStatus status, BloodGroup bloodGroup, LocalDate date);
    List<BloodUnit> findByStatus(BloodUnitStatus status);
    long countByBloodGroupAndStatus(BloodGroup bloodGroup, BloodUnitStatus status);
    @Query("select b from BloodUnit b where b.status = :status and b.expiryDate <= :date")
    List<BloodUnit> findByStatusAndExpiryDateLessThanEqual(BloodUnitStatus status, LocalDate date);
    @Query("select b from BloodUnit b where b.status = :status and b.expiryDate > :today and b.expiryDate <= :soon")
    List<BloodUnit> findExpiringSoon(BloodUnitStatus status, LocalDate today, LocalDate soon);
}
