package hr.abysalto.flight_search.repository;

import hr.abysalto.flight_search.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long>, JpaSpecificationExecutor<Flight> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Flight f WHERE f.createdOn < :expiryThreshold")
    int deleteExpired(@Param("expiryThreshold") LocalDateTime expiryThreshold);
}
