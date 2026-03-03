package com.uit.se356.core.application.upload.command;

import com.uit.se356.common.dto.Command;

public record ConfirmUploadCommand(String storageKey, long size, String contentType)
    implements Command<Void> {}
