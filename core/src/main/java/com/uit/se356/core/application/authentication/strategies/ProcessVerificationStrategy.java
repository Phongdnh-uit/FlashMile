package com.uit.se356.core.application.authentication.strategies;

import com.uit.se356.core.application.authentication.result.VerificationResult;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;

public interface ProcessVerificationStrategy {
  // Kiểm tra chiến lược có hỗ trợ mục đích mã không
  // Ví dụ: Xác minh điện thoại, Đặt lại mật khẩu, v.v.
  boolean support(CodePurpose purpose);

  // Xử lý quy trình xác minh dựa trên mục đích mã
  // Ví dụ: Gửi mã qua SMS, Email, v.v.
  VerificationResult process(CodePurpose purpose, String recipient, String code);
}
