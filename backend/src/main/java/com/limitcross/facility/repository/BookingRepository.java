package com.limitcross.facility.repository;

import com.limitcross.facility.domain.Booking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findAllByOwnerLoginOrderByDateDesc(String ownerLogin);
    Optional<Booking> findOneByIdAndOwnerLogin(String id, String ownerLogin);
}