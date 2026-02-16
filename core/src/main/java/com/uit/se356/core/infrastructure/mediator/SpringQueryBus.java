package com.uit.se356.core.infrastructure.mediator;

import com.uit.se356.common.dto.Query;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.QueryBus;
import com.uit.se356.common.services.QueryHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

@Component
public class SpringQueryBus implements QueryBus {
  private final Map<Class<? extends Query<?>>, QueryHandler<?, ?>> handlers;

  @SuppressWarnings("unchecked")
  public SpringQueryBus(List<QueryHandler<?, ?>> queryHandlers) {
    this.handlers = new HashMap<>();
    queryHandlers.forEach(
        handler -> {
          Class<?> targetClass = AopUtils.getTargetClass(handler);
          Class<?> queryType =
              GenericTypeResolver.resolveTypeArgument(targetClass, QueryHandler.class);
          if (queryType != null && Query.class.isAssignableFrom(queryType)) {
            handlers.put((Class<? extends Query<?>>) queryType, handler);
          }
        });
  }

  @SuppressWarnings("unchecked")
  @Override
  public <R> R dispatch(Query<R> query) {
    QueryHandler<?, ?> handler = handlers.get(query.getClass());
    return Optional.ofNullable(handler)
        .map(
            h -> {
              try {
                // Ép kiểu handler về CommandHandler<Command<R>, R>
                return (R) ((QueryHandler<Query<R>, R>) h).handle(query);
              } catch (ClassCastException e) {
                throw new AppException(CommonErrorCode.INTERNAL_ERROR);
              }
            })
        .orElseThrow(() -> new AppException(CommonErrorCode.INTERNAL_ERROR));
  }
}
