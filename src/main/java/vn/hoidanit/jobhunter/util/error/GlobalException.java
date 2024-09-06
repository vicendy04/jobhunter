package vn.hoidanit.jobhunter.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.hoidanit.jobhunter.domain.RestResponse;

import java.util.List;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleBlogAlreadyExistsException(IdInvalidException exception) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError("CALL API FAILED");
        restResponse.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleValidationException(MethodArgumentNotValidException exception) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError("CALL API FAILED");

//        restResponse.setMessage(exception.getFieldError().getDefaultMessage());
        List<String> errorMessages = exception.getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage()).toList();
        restResponse.setData(errorMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
}
