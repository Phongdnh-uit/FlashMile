package com.uit.se356.core.application.depot.handler;

import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.depot.command.CreateDepotCommand;
import com.uit.se356.core.application.depot.port.DepotRepository;
import com.uit.se356.core.application.depot.result.DepotResult;
import com.uit.se356.core.domain.entities.depot.Depot;
import com.uit.se356.core.domain.vo.area.Coordinate;
import com.uit.se356.core.domain.vo.depot.DepotId;

public class CreateDepotHandler implements CommandHandler<CreateDepotCommand, DepotResult> {
  private final DepotRepository depotRepository;
  private final IdGenerator idGenerator;

  public CreateDepotHandler(DepotRepository depotRepository, IdGenerator idGenerator) {
    this.depotRepository = depotRepository;
    this.idGenerator = idGenerator;
  }

  @Override
  public DepotResult handle(CreateDepotCommand command) {
    String newId = idGenerator.generate().toString();
    Coordinate coordinate = new Coordinate(command.lat(), command.lng());

    Depot depot = Depot.create(new DepotId(newId), command.name(), command.type(), coordinate);
    Depot savedDepot = depotRepository.create(depot);

    return DepotResult.fromEntity(savedDepot);
  }
}
