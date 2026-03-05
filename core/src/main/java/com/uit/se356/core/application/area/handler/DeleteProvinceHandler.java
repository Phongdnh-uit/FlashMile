package com.uit.se356.core.application.area.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.area.command.DeleteProvinceCommand;
import com.uit.se356.core.application.area.port.ProvinceRepository;
import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.exception.AreaErrorCode;

public class DeleteProvinceHandler implements CommandHandler<DeleteProvinceCommand, Void> {
  private final ProvinceRepository provinceRepository;

  public DeleteProvinceHandler(ProvinceRepository provinceRepository) {
    this.provinceRepository = provinceRepository;
  }

  @Override
  public Void handle(DeleteProvinceCommand command) {
    Province existingProvince =
        provinceRepository
            .findById(command.id())
            .orElseThrow(() -> new AppException(AreaErrorCode.PROVINCE_NOT_FOUND, command.id()));
    provinceRepository.deleteById(command.id());
    return null;
  }
}
