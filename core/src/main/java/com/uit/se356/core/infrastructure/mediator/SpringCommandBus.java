package com.uit.se356.core.infrastructure.mediator;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.CommandBus;
import com.uit.se356.common.services.CommandHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

@Component
public class SpringCommandBus implements CommandBus {
  private final Map<Class<? extends Command<?>>, CommandHandler<?, ?>> handlers;

  @SuppressWarnings("unchecked")
  public SpringCommandBus(List<CommandHandler<?, ?>> commandHandlers) {
    this.handlers = new HashMap<>();
    commandHandlers.forEach(
        handler -> {
          Class<?> targetClass = AopUtils.getTargetClass(handler);
          Class<?> commandType =
              GenericTypeResolver.resolveTypeArgument(targetClass, CommandHandler.class);
          if (commandType != null && Command.class.isAssignableFrom(commandType)) {
            handlers.put((Class<? extends Command<?>>) commandType, handler);
          }
        });
  }

  @SuppressWarnings("unchecked")
  @Override
  public <R> R dispatch(Command<R> command) {
    CommandHandler<?, ?> handler = handlers.get(command.getClass());
    return Optional.ofNullable(handler)
        .map(
            h -> {
              try {
                // Ép kiểu handler về CommandHandler<Command<R>, R>
                return (R) ((CommandHandler<Command<R>, R>) h).handle(command);
              } catch (ClassCastException e) {
                throw new AppException(CommonErrorCode.INTERNAL_ERROR);
              }
            })
        .orElseThrow(() -> new AppException(CommonErrorCode.INTERNAL_ERROR));
  }
}
