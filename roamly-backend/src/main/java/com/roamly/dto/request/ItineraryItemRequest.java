package com.roamly.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ItineraryItemRequest {

    @NotBlank
    private String title;

    private String description;

    private String location;

    private String googleMapsUrl;

    private Double latitude;

    private Double longitude;

    private Integer orderIndex;
}
