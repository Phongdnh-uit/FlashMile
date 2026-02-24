package com.uit.se356.core.application.internal.command;

import com.uit.se356.common.dto.Command;

public record SyncPermissionCommand(String packageName) implements Command<Void> {}
