package com.roamly.repository;

import com.roamly.entity.Comment;
import com.roamly.entity.Itinerary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByItineraryOrderByCreatedAtDesc(Itinerary itinerary, Pageable pageable);
}
