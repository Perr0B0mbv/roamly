package com.roamly.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommunityItineraryResponse {

    private Long id;
    private String title;
    private String description;
    private Double averageRating;
    private Integer ratingCount;
    private String ownerUsername;
    private LocalDateTime createdAt;
    private Integer itemCount;
}
