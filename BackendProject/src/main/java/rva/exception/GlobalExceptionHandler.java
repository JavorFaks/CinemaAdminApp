package rva.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//Catch za neispravan unos parametara
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request)
	{
		String message = String.format("Parametar '%s' treba da bude tipa %s", ex.getName(), ex.getRequiredType().getSimpleName());

		ApiError apiError = new ApiError(
				HttpStatus.BAD_REQUEST.value(),
				"Type Mismatch",
				message,
				request.getRequestURI()
				);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	//Exception za validiranje podataka koje se unose u bazu
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> details = ex.getBindingResult() //uzima objekat koji sadrzi rezultate validacije
                .getFieldErrors() //uzima samo greske iz objekta
                .stream() //pretvara listu u traku ide jedna po jedan iza druge
                .map(err -> err.getField() + ": " + err.getDefaultMessage()) //transformise, pretvara u string greska:poruka
                .collect(Collectors.toList()); //na kraju sve te greske stavlja u listu details

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Podaci nisu ispravni",
                request.getRequestURI()
        );
        apiError.setDetails(details); // Ovde koristimo listu detalja

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
	
	//Objekat nije pronadjen/nepostoji
	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

	//Kad klijent posalje nepotpune/neispravne podatke
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
	    ApiError apiError = new ApiError(
	            HttpStatus.BAD_REQUEST.value(),
	            "Bad Request",
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}
	
	//Hvata greske baze (npr nepostojeci strani kljuc)
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleSQLConstraints(DataIntegrityViolationException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Database Constraint Violation",
                "Greška u bazi: Narušen integritet podataka (proverite strane ključeve).",
                request.getRequestURI()
        );
        
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
	
	//Za sve nepredvidjene greske koje se mogu desiti
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllOtherExceptions(Exception ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Došlo je do neočekivane greške na serveru.",
                request.getRequestURI()
        );
        
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
