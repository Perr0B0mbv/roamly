package com.roamly.controller;

import com.roamly.dto.request.ItineraryRequest;
import com.roamly.dto.response.ItineraryResponse;
import com.roamly.entity.User;
import com.roamly.exception.ResourceNotFoundException;
import com.roamly.repository.UserRepository;
import com.roamly.security.UserDetailsImpl;
import com.roamly.service.ItineraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<ItineraryResponse>> getMyItineraries(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {

        User user = resolveUser(userDetails);
        return ResponseEntity.ok(itineraryService.getMyItineraries(user, page, size, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItineraryResponse> getMyItineraryById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {

        User user = resolveUser(userDetails);
        return ResponseEntity.ok(itineraryService.getMyItineraryById(user, id));
    }

    @PostMapping
    public ResponseEntity<ItineraryResponse> createItinerary(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ItineraryRequest request) {

        User user = resolveUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itineraryService.createItinerary(user, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItineraryResponse> updateItinerary(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @Valid @RequestBody ItineraryRequest request) {

        User user = resolveUser(userDetails);
        return ResponseEntity.ok(itineraryService.updateItinerary(user, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItinerary(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {

        User user = resolveUser(userDetails);
        itineraryService.deleteItinerary(user, id);
        return ResponseEntity.noContent().build();
    }

    private User resolveUser(UserDetailsImpl userDetails) {
        return userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", null));
    }
}
