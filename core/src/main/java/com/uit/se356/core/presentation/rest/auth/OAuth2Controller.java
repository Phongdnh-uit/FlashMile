package com.uit.se356.core.presentation.rest.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth2")
@RequestMapping("/oauth2")
@RestController
public class OAuth2Controller {
  @Operation(
      summary = "Endpoint giúp đăng nhập bằng OAuth2. Hiện tại hỗ trợ Google",
      description =
          "Nếu sử dụng swagger thì sẽ không được do bị chặn bởi CORS. Vui lòng click vào link kế"
              + " bên để đăng nhập bằng Google."
              + "[/oauth2/authorize/google](/oauth2/authorize/google)")
  @GetMapping("/authorize/{provider}")
  public void redirectToProvider(@PathVariable("provider") String provider) {}
}
