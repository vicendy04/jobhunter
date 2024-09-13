package vn.hoidanit.jobhunter.util.error;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.hoidanit.jobhunter.domain.response.RestResponse;

import java.util.List;

//chỉ nhận lỗi ở tầng controller
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class, IdInvalidException.class})
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception exception) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(exception.getMessage());
        restResponse.setMessage("CALL API FAILED");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(NoResourceFoundException exception) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        restResponse.setError(exception.getMessage());
        restResponse.setMessage("404 Not Found. URL may not exist...");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleValidationException(MethodArgumentNotValidException exception) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError("CALL API FAILED");

//        restResponse.setMessage(exception.getFieldError().getDefaultMessage());
        List<String> errorMessages = exception.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        restResponse.setData(errorMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
}
