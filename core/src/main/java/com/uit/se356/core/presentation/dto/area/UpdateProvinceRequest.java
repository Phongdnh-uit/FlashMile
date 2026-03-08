package com.uit.se356.core.presentation.dto.area;

import com.uit.se356.core.domain.vo.area.ProvinceType;

public record UpdateProvinceRequest(String code, String name, ProvinceType type) {}
