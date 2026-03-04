package com.uit.se356.core.application.area.dto;

public record UpdateProvinceRequest(
    String code, String name, Double minLat, Double minLng, Double maxLat, Double maxLng) {}
