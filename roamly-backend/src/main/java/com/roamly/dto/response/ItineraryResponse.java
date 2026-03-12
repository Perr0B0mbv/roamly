package com.roamly.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItineraryResponse {

    private Long id;
    private String title;
    private String description;
    private Boolean isPublic;
    private Double averageRating;
    private Integer ratingCount;
    private LocalDateTime createdAt;
    private String ownerUsername;
    private List<ItineraryItemResponse> items;
}
