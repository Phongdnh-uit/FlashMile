package com.uit.se356.core.application.area.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.area.command.UpdateProvinceCommand;
import com.uit.se356.core.application.area.port.ProvinceRepository;
import com.uit.se356.core.application.area.result.ProvinceResult;
import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.exception.AreaErrorCode;
import java.util.Optional;

public class UpdateProvinceHandler
    implements CommandHandler<UpdateProvinceCommand, ProvinceResult> {
  private final ProvinceRepository provinceRepository;

  public UpdateProvinceHandler(ProvinceRepository provinceRepository) {
    this.provinceRepository = provinceRepository;
  }

  @Override
  public ProvinceResult handle(UpdateProvinceCommand command) {
    Optional<Province> provinceOpt = provinceRepository.findById(command.id());
    if (provinceOpt.isEmpty()) {
      throw new AppException(AreaErrorCode.PROVINCE_NOT_FOUND);
    }

    Province existingProvince = provinceOpt.get();
    existingProvince.updateProvince(command.code(), command.name(), command.boundingBox());
    Province updatedProvince = provinceRepository.update(existingProvince);

    return ProvinceResult.fromEntity(updatedProvince);
  }
}
