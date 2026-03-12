package com.roamly.service;

import com.roamly.dto.request.ItineraryItemRequest;
import com.roamly.dto.request.ItineraryRequest;
import com.roamly.dto.response.ItineraryItemResponse;
import com.roamly.dto.response.ItineraryResponse;
import com.roamly.entity.Itinerary;
import com.roamly.entity.ItineraryItem;
import com.roamly.entity.User;
import com.roamly.exception.ResourceNotFoundException;
import com.roamly.repository.ItineraryItemRepository;
import com.roamly.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final ItineraryItemRepository itineraryItemRepository;

    public Page<ItineraryResponse> getMyItineraries(User user, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        return itineraryRepository.findByOwner(user, pageable)
                .map(this::mapToResponse);
    }

    public ItineraryResponse getMyItineraryById(User user, Long id) {
        Itinerary itinerary = itineraryRepository.findByOwnerAndId(user, id)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary", id));
        return mapToResponse(itinerary);
    }

    @Transactional
    public ItineraryResponse createItinerary(User user, ItineraryRequest request) {
        Itinerary itinerary = Itinerary.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .owner(user)
                .build();

        Itinerary saved = itineraryRepository.save(itinerary);
        saveItems(request, saved);

        return mapToResponse(itineraryRepository.findById(saved.getId()).orElseThrow());
    }

    @Transactional
    public ItineraryResponse updateItinerary(User user, Long id, ItineraryRequest request) {
        Itinerary itinerary = itineraryRepository.findByOwnerAndId(user, id)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary", id));

        itinerary.setTitle(request.getTitle());
        itinerary.setDescription(request.getDescription());
        if (request.getIsPublic() != null) {
            itinerary.setIsPublic(request.getIsPublic());
        }

        itineraryItemRepository.deleteByItinerary(itinerary);
        itineraryRepository.save(itinerary);
        saveItems(request, itinerary);

        return mapToResponse(itineraryRepository.findById(id).orElseThrow());
    }

    @Transactional
    public void deleteItinerary(User user, Long id) {
        Itinerary itinerary = itineraryRepository.findByOwnerAndId(user, id)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary", id));
        itineraryItemRepository.deleteByItinerary(itinerary);
        itineraryRepository.delete(itinerary);
    }

    private void saveItems(ItineraryRequest request, Itinerary itinerary) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            return;
        }
        List<ItineraryItem> items = request.getItems().stream()
                .map(itemReq -> buildItem(itemReq, itinerary))
                .toList();
        itineraryItemRepository.saveAll(items);
    }

    private ItineraryItem buildItem(ItineraryItemRequest req, Itinerary itinerary) {
        return ItineraryItem.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .location(req.getLocation())
                .googleMapsUrl(req.getGoogleMapsUrl())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .orderIndex(req.getOrderIndex())
                .itinerary(itinerary)
                .build();
    }

    private ItineraryResponse mapToResponse(Itinerary itinerary) {
        List<ItineraryItem> items = itineraryItemRepository
                .findByItineraryOrderByOrderIndex(itinerary);

        List<ItineraryItemResponse> itemResponses = items.stream()
                .map(this::mapItemToResponse)
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

    private ItineraryItemResponse mapItemToResponse(ItineraryItem item) {
        return ItineraryItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .location(item.getLocation())
                .googleMapsUrl(item.getGoogleMapsUrl())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .orderIndex(item.getOrderIndex())
                .build();
    }
}
