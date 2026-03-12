package com.roamly.service;

import com.roamly.dto.request.CommentRequest;
import com.roamly.dto.request.RatingRequest;
import com.roamly.dto.response.CommentResponse;
import com.roamly.dto.response.CommunityItineraryResponse;
import com.roamly.dto.response.ItineraryResponse;
import com.roamly.entity.*;
import com.roamly.exception.ResourceNotFoundException;
import com.roamly.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final ItineraryRepository itineraryRepository;
    private final ItineraryItemRepository itineraryItemRepository;
    private final ItineraryRatingRepository itineraryRatingRepository;
    private final CommentRepository commentRepository;

    public Page<CommunityItineraryResponse> searchItineraries(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "averageRating"));

        Page<Itinerary> results = StringUtils.hasText(title)
                ? itineraryRepository.findByIsPublicTrueAndTitleContainingIgnoreCase(title, pageable)
                : itineraryRepository.findByIsPublicTrue(pageable);

        return results.map(this::mapToCommunityResponse);
    }

    @Transactional
    public ItineraryResponse cloneItinerary(User user, Long id) {
        Itinerary original = itineraryRepository.findById(id)
                .filter(Itinerary::getIsPublic)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary", id));

        Itinerary clone = Itinerary.builder()
                .title(original.getTitle() + " (copia)")
                .description(original.getDescription())
                .isPublic(false)
                .owner(user)
                .build();

        Itinerary savedClone = itineraryRepository.save(clone);

        List<ItineraryItem> originalItems = itineraryItemRepository
                .findByItineraryOrderByOrderIndex(original);

        List<ItineraryItem> clonedItems = originalItems.stream()
                .map(item -> ItineraryItem.builder()
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .location(item.getLocation())
                        .googleMapsUrl(item.getGoogleMapsUrl())
                        .latitude(item.getLatitude())
                        .longitude(item.getLongitude())
                        .orderIndex(item.getOrderIndex())
                        .itinerary(savedClone)
                        .build())
                .toList();

        itineraryItemRepository.saveAll(clonedItems);

        return mapToItineraryResponse(itineraryRepository.findById(savedClone.getId()).orElseThrow(), clonedItems);
    }

    @Transactional
    public CommunityItineraryResponse rateItinerary(User user, Long id, RatingRequest request) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .filter(Itinerary::getIsPublic)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary", id));

        ItineraryRating rating = ItineraryRating.builder()
                .rating(request.getRating())
                .user(user)
                .itinerary(itinerary)
                .build();

        itineraryRatingRepository.save(rating);

        double newAverage = ((itinerary.getAverageRating() * itinerary.getRatingCount()) + request.getRating())
                / (itinerary.getRatingCount() + 1);

        itinerary.setAverageRating(Math.round(newAverage * 10.0) / 10.0);
        itinerary.setRatingCount(itinerary.getRatingCount() + 1);

        return mapToCommunityResponse(itineraryRepository.save(itinerary));
    }

    @Transactional
    public CommentResponse addComment(User user, Long id, CommentRequest request) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .filter(Itinerary::getIsPublic)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary", id));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(user)
                .itinerary(itinerary)
                .build();

        return mapToCommentResponse(commentRepository.save(comment));
    }

    public Page<CommentResponse> getComments(Long id, int page, int size) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .filter(Itinerary::getIsPublic)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary", id));

        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findByItineraryOrderByCreatedAtDesc(itinerary, pageable)
                .map(this::mapToCommentResponse);
    }

    private CommunityItineraryResponse mapToCommunityResponse(Itinerary itinerary) {
        int itemCount = itineraryItemRepository.findByItineraryOrderByOrderIndex(itinerary).size();
        return CommunityItineraryResponse.builder()
                .id(itinerary.getId())
                .title(itinerary.getTitle())
                .description(itinerary.getDescription())
                .averageRating(itinerary.getAverageRating())
                .ratingCount(itinerary.getRatingCount())
                .ownerUsername(itinerary.getOwner().getUsername())
                .createdAt(itinerary.getCreatedAt())
                .itemCount(itemCount)
                .build();
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorUsername(comment.getAuthor().getUsername())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private ItineraryResponse mapToItineraryResponse(Itinerary itinerary, List<ItineraryItem> items) {
        List<com.roamly.dto.response.ItineraryItemResponse> itemResponses = items.stream()
                .map(item -> com.roamly.dto.response.ItineraryItemResponse.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .location(item.getLocation())
                        .googleMapsUrl(item.getGoogleMapsUrl())
                        .latitude(item.getLatitude())
                        .longitude(item.getLongitude())
                        .orderIndex(item.getOrderIndex())
                        .build())
                .toList();

        return ItineraryResponse.builder()
                .id(itinerary.getId())
                .title(itinerary.getTitle())
                .description(itinerary.getDescription())
                .isPublic(itinerary.getIsPublic())
                .averageRating(itinerary.getAverageRating())
                .ratingCount(itinerary.getRatingCount())
                .createdAt(itinerary.getCreatedAt())
                .ownerUsername(itinerary.getOwner().getUsername())
                .items(itemResponses)
                .build();
    }
}
