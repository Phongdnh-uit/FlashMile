package com.uit.se356.core.presentation.dto.area;

import com.uit.se356.core.domain.vo.area.Polygon;
import com.uit.se356.core.domain.vo.area.WardType;

public record UpdateWardRequest(
    String code, String name, String provinceId, WardType type, Polygon polygon) {}
