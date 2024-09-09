package vn.hoidanit.jobhunter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import vn.hoidanit.jobhunter.domain.RestResponse;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint base = new BearerTokenAuthenticationEntryPoint();

    private final ObjectMapper mapper;

    public JwtAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.base.commence(request, response, authException);
        response.setContentType("application/json;charset=UTF-8");

        // Tạo một đối tượng RestResponse để chứa thông tin lỗi
        RestResponse<Object> restResponse = new RestResponse<>();


        restResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = Optional.ofNullable(authException.getCause())
                .map(throwable -> throwable.getMessage())
                .orElse(authException.getMessage()); // If a value is present, returns the value, otherwise returns other.

        restResponse.setError(errorMessage);
        restResponse.setMessage("Token không hợp lệ");

//        response.getWriter().write(mapper.writeValueAsString(restResponse));
        // Ghi JSON vào phản hồi
        mapper.writeValue(response.getWriter(), restResponse);
        response.flushBuffer();
    }
}
