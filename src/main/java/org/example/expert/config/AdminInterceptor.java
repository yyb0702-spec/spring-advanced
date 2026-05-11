package org.example.expert.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        //Object 에서 다운캐스팅
        String userRole = (String) request.getAttribute("userRole");
        Long userId = (Long) request.getAttribute("userId");

        if (!UserRole.ADMIN.name().equals(userRole)) {
            throw new AuthException("접근 권한이 없습니다.");
        }

        log.info("Admin: userId={}, time={}, url={}",
                userId,
                LocalDateTime.now(),
                request.getRequestURI());

        return true;
    }
}
