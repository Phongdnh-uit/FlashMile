package com.uit.se356.core.application.authentication.strategies;

import com.uit.se356.core.application.authentication.port.VerificationSender;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;
import com.uit.se356.core.domain.vo.authentication.VerificationChannel;
import java.util.List;

public interface SendVerificationStrategy {

  // Kiểm tra chiến lược có hỗ trợ mục đích mã không
  // Ví dụ: Xác minh điện thoại, Đặt lại mật khẩu, v.v.
  boolean support(CodePurpose purpose);

  // Gửi mã xác minh đến người nhận dựa trên mục đích
  // VerificationSender được sử dụng để gửi mã qua kênh cụ thể (ví dụ: SMS, Email)
  // Note: truyền bao nhiêu VerificationSender thì sẽ gửi bấy nhiêu kênh (nếu hỗ trợ)
  void send(
      String recipient,
      CodePurpose purpose,
      VerificationChannel channel,
      List<VerificationSender> verificationSender);
}
