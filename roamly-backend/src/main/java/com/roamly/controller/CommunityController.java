package com.roamly.controller;

import com.roamly.dto.request.CommentRequest;
import com.roamly.dto.request.RatingRequest;
import com.roamly.dto.response.CommentResponse;
import com.roamly.dto.response.CommunityItineraryResponse;
import com.roamly.dto.response.ItineraryResponse;
import com.roamly.entity.User;
import com.roamly.exception.ResourceNotFoundException;
import com.roamly.repository.UserRepository;
import com.roamly.security.UserDetailsImpl;
import com.roamly.service.CommunityService;
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
public class CommunityController {

    private final CommunityService communityService;
    private final UserRepository userRepository;

    @GetMapping("/community/search")
    public ResponseEntity<Page<CommunityItineraryResponse>> searchItineraries(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(communityService.searchItineraries(title, page, size));
    }

    @PostMapping("/{id}/clone")
    public ResponseEntity<ItineraryResponse> cloneItinerary(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {

        User user = resolveUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communityService.cloneItinerary(user, id));
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<CommunityItineraryResponse> rateItinerary(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @Valid @RequestBody RatingRequest request) {

        User user = resolveUser(userDetails);
        return ResponseEntity.ok(communityService.rateItinerary(user, id, request));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {

        User user = resolveUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communityService.addComment(user, id, request));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentResponse>> getComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(communityService.getComments(id, page, size));
    }

    private User resolveUser(UserDetailsImpl userDetails) {
        return userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", null));
    }
}
