package com.roamly.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItineraryItemResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String googleMapsUrl;
    private Double latitude;
    private Double longitude;
    private Integer orderIndex;
}
