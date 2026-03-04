package com.uit.se356.core.application.area.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.area.command.CreateProvinceCommand;
import com.uit.se356.core.application.area.port.ProvinceRepository;
import com.uit.se356.core.application.area.result.ProvinceResult;
import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.exception.AreaErrorCode;

public class CreateProvinceHandler
    implements CommandHandler<CreateProvinceCommand, ProvinceResult> {
  private final ProvinceRepository provinceRepository;

  public CreateProvinceHandler(ProvinceRepository provinceRepository) {
    this.provinceRepository = provinceRepository;
  }

  @Override
  public ProvinceResult handle(CreateProvinceCommand command) {
    if (provinceRepository.existsByCode(command.code())) {
      throw new AppException(AreaErrorCode.DUPLICATE_PROVINCE_CODE);
    }

    Province newProvince =
        Province.createNewProvince(command.code(), command.name(), command.boundingBox());

    Province savedProvince = provinceRepository.create(newProvince);
    return ProvinceResult.fromEntity(savedProvince);
  }
}
