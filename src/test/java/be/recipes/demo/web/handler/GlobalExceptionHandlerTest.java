package be.recipes.demo.web.handler;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleException() {
        EntityNotFoundException exception = new EntityNotFoundException("some message");
        ResponseEntity<Object> response = exceptionHandler.handleException(exception);
        Map<String, Object> body = new HashMap<>();
        body.put("message", exception.getMessage());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(body);
    }
}