package rva.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.validation.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//Catch za neispravan unos parametara
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex)
	{
		//Daje ime parametra
		String imeParametra = ex.getName();
		
		String poruka = "Neispravan unos za parametar '" + imeParametra + "'";
		
		//Vraca 400 bad request
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(poruka);
	}
	
	//Exception za validiranje podataka koje se unose u bazu
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex)
	{
		StringBuilder greske = new StringBuilder("Greske u validaciji podataka:\n");
		
		//uzmanje svih gresaka i prolazak kroz njih
		ex.getBindingResult().getAllErrors().forEach((error) ->{
			String imePolja = ((FieldError) error).getField(); //ime polja
			
			String poruka = error.getDefaultMessage(); //dobija se poruka koje je napisana u modelu
			
			greske.append("-").append(imePolja).append(": ").append(poruka).append("\n"); //buildovanje greske
		});
		
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(greske.toString());
	}
	
	// Hvata EntityNotFoundException iz servisa
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	// Hvata SQL gresku ako provera u servisu zakaze
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleSQLConstraints(DataIntegrityViolationException ex) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body("Greška: Narušen integritet podataka (verovatno nepostojeći ID bioskopa).");
	}
	
	//Za sve ostale greske na serveru
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleAllOtherExceptions(Exception ex) {
		
		ex.printStackTrace();
		
	    String poruka = "Došlo je do neočekivane greške na serveru. Naš tim radi na rešavanju problema.";
	    
	    // Vraća status 500
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(poruka);
	}
}
