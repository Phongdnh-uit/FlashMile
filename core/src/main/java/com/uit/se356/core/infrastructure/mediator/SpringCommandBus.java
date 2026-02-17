package com.uit.se356.core.infrastructure.mediator;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.CommandBus;
import com.uit.se356.common.services.CommandHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringCommandBus implements CommandBus {
  private final Map<Class<? extends Command<?>>, CommandHandler<?, ?>> handlers;

  @SuppressWarnings("unchecked")
  public SpringCommandBus(List<CommandHandler<?, ?>> commandHandlers) {
    this.handlers = new HashMap<>();
    commandHandlers.forEach(
        handler -> {
          Class<?> targetClass = AopUtils.getTargetClass(handler);
          ResolvableType resolvableType =
              ResolvableType.forClass(targetClass).as(CommandHandler.class);
          Class<?> commandType = resolvableType.getGeneric(0).resolve();
          if (commandType != null && Command.class.isAssignableFrom(commandType)) {
            handlers.put((Class<? extends Command<?>>) commandType, handler);
          }
        });
  }

  @SuppressWarnings("unchecked")
  @Override
  public <R> R dispatch(Command<R> command) {
    CommandHandler<?, ?> handler = handlers.get(command.getClass());
    if (handler == null) {
      log.error("No handler found for command: {}", command.getClass().getName());
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
    try {
      return (R) ((CommandHandler<Command<R>, R>) handler).handle(command);
    } catch (ClassCastException e) {
      log.error("Handler type mismatch for command: {}", command.getClass().getName(), e);
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
  }
}
