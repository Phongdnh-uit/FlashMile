package com.uit.se356.common.utils;

public class ErrorUtil {
  private ErrorUtil() {
    // Chặn khởi tạo đối tượng
  }

  public static Throwable getRootCause(Throwable throwable) {
    Throwable cause = throwable;
    while (cause.getCause() != null) {
      cause = cause.getCause();
    }
    return cause;
  }

  public static String getRootCauseMessage(Throwable throwable) {
    return getRootCause(throwable).getMessage();
  }
}
