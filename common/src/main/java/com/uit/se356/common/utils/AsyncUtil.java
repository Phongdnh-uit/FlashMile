package com.uit.se356.common.utils;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class AsyncUtil {
  public static void sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public static <T> CompletableFuture<T> runAsync(Supplier<T> supplier) {
    return CompletableFuture.supplyAsync(supplier);
  }
}
