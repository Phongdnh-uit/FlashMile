package com.uit.se356.core.presentation.dto.area;

public record UpdateWardRequest(
    String code,
    String name,
    String provinceId,
    Double minLat,
    Double minLng,
    Double maxLat,
    Double maxLng) {}
