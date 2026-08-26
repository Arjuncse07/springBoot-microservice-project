package com.arjun.library_service.bookstore.library.copy.dto;

import com.arjun.library_service.bookstore.library.domain.CopyStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateCopyStatusRequest(@NotNull CopyStatus status) {}
