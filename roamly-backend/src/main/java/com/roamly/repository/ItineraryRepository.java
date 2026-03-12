package com.roamly.repository;

import com.roamly.entity.Itinerary;
import com.roamly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    Page<Itinerary> findByOwner(User owner, Pageable pageable);

    Optional<Itinerary> findByOwnerAndId(User owner, Long id);

    Page<Itinerary> findByIsPublicTrue(Pageable pageable);

    Page<Itinerary> findByIsPublicTrueAndTitleContainingIgnoreCase(String title, Pageable pageable);
}
