package pl.sda.poznan.spring.petclinic.aop;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.sda.poznan.spring.petclinic.exception.OwnerNotFoundException;

@ControllerAdvice
public class ApplicationErrorHandler {

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity handleNumberFormatException(){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity handleOwnerNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
