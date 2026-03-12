package com.roamly.repository;

import com.roamly.entity.Itinerary;
import com.roamly.entity.ItineraryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ItineraryItemRepository extends JpaRepository<ItineraryItem, Long> {

    List<ItineraryItem> findByItineraryOrderByOrderIndex(Itinerary itinerary);

    @Modifying
    void deleteByItinerary(Itinerary itinerary);
}
