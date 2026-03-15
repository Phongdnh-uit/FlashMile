package com.uit.se356.core.application.depot.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.depot.command.UpdateDepotCommand;
import com.uit.se356.core.application.depot.port.DepotRepository;
import com.uit.se356.core.application.depot.result.DepotResult;
import com.uit.se356.core.domain.entities.depot.Depot;
import com.uit.se356.core.domain.exception.DepotErrorCode;
import com.uit.se356.core.domain.vo.area.Coordinate;

public class UpdateDepotHandler implements CommandHandler<UpdateDepotCommand, DepotResult> {
  private final DepotRepository depotRepository;

  public UpdateDepotHandler(DepotRepository depotRepository) {
    this.depotRepository = depotRepository;
  }

  @Override
  public DepotResult handle(UpdateDepotCommand command) {
    Depot depot =
        depotRepository
            .findById(command.id())
            .orElseThrow(() -> new AppException(DepotErrorCode.DEPOT_NOT_FOUND));

    Coordinate coordinate = new Coordinate(command.lat(), command.lng());
    depot.update(command.name(), command.type(), coordinate);

    Depot updatedDepot = depotRepository.update(depot);
    return DepotResult.fromEntity(updatedDepot);
  }
}
