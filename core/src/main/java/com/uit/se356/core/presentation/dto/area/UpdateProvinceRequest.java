package com.uit.se356.core.presentation.dto.area;

public record UpdateProvinceRequest(
    String code, String name, Double minLat, Double minLng, Double maxLat, Double maxLng) {}
