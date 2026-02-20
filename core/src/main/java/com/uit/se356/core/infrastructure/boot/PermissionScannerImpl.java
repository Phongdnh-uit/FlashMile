package com.uit.se356.core.infrastructure.boot;

import com.uit.se356.common.security.HasPermission;
import com.uit.se356.common.security.PermissionScanner;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionScannerImpl implements PermissionScanner {
  private final ApplicationContext applicationContext;

  @Override
  public List<String> scan(String packageName) {

    List<String> permissions = new ArrayList<>();

    // Lấy tất cả tên bean mà Spring đã đăng ký
    for (String beanName : applicationContext.getBeanDefinitionNames()) {

      // Lấy class gốc, tránh các class proxy do Spring tạo ra (cho @Transactional, @Async,...)
      Class<?> targetClass = applicationContext.getType(beanName);

      // Nếu không lấy được class (ví dụ bean là một FactoryBean), bỏ qua
      if (targetClass == null) continue;

      // Chỉ quét các bean thuộc package, bỏ qua các bean của framework
      if (targetClass.getPackage() != null
          && targetClass.getPackage().getName().startsWith(packageName)) {

        // Quét annotation trên class
        HasPermission classAnno = targetClass.getAnnotation(HasPermission.class);

        if (classAnno != null) {
          permissions.add(classAnno.value());
        }

        // Quét annotation trên các method
        for (Method method : targetClass.getMethods()) {

          HasPermission methodAnno = method.getAnnotation(HasPermission.class);

          if (methodAnno != null) {
            permissions.add(methodAnno.value());
          }
        }
      }
    }

    return permissions;
  }
}
