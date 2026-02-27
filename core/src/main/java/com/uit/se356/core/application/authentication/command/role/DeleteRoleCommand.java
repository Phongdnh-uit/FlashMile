package com.uit.se356.core.application.authentication.command.role;

import com.uit.se356.common.dto.Command;
import com.uit.se356.core.domain.vo.authentication.RoleId;

public record DeleteRoleCommand(RoleId id) implements Command<Void> {}
