package com.uit.se356.common.services;

import com.uit.se356.common.dto.Command;

/** Bus cho các Command */
public interface CommandBus {
  <R> R dispatch(Command<R> command);
}
