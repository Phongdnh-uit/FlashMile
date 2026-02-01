package com.uit.se356.common.utils;

import java.util.function.Supplier;

public class TransactionUtil {
  public static void runInTransaction(Runnable action) {
    try {
      action.run();
      // Commit hành động
    } catch (Exception e) {
      // Rollback
      throw e;
    }
  }

  public static <T> T runInTransactionWithResult(Supplier<T> action) {
    try {
      T result = action.get();
      // Commit hành động
      return result;
    } catch (Exception e) {
      // Rollback
      throw e;
    }
  }
}
