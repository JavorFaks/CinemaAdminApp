package rva.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rva.implementation.FilmServiceImpl;
import rva.model.Film;

@RestController
public class FilmController {
	@Autowired
	private FilmServiceImpl service;
	
	@GetMapping("/film")
	public ResponseEntity<?> getFilm(@RequestParam(required=false) Long id)
	{
		List<Film> filmovi = service.getAll();
		
		if(filmovi.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema filmova");
		} 
		
		return ResponseEntity.ok(filmovi);
	}
	
	// Vracanje samo jednog filma
	@GetMapping("/film/{id}")
	public ResponseEntity<?> getFilmById(@PathVariable Long id) {
		Optional<Film> film = service.findById(id);
		
		if(film.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Film sa ID-jem " + id + " ne postoji.");
		}
			
		return ResponseEntity.ok(film.get());
	}
	
	@GetMapping("/film/pretraga")
	public ResponseEntity<?> naprednaPretraga(
	        @RequestParam(required = false) String naziv,
	        @RequestParam(required = false) String zanr,
	        @RequestParam(required = false) Integer maxMinuta
	) {
	    List<Film> filmovi = service.naprednaPretraga(naziv, zanr, maxMinuta);
	    
	    if(filmovi.isEmpty()) {
	        return ResponseEntity.status(404).body("Nema filmova sa ovim kriterijumima!");
	    }
	    
	    return ResponseEntity.ok(filmovi);
	}
	
	//Dodavanje novog filma
	@PostMapping("/film")
	public ResponseEntity<?> createFilm(@Valid @RequestBody Film film){
		Film createdFilm = service.create(film);
		
		//Lokacija novo kreirane stavke
		URI uri = URI.create(String.format("/film/%s", createdFilm.getId()));
		
		return ResponseEntity.created(uri).body(createdFilm);
	}
	
	
	//Modifikacija
	@PutMapping("/film/{id}")
	public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film, @PathVariable Long id)
	{
		Optional<Film> updatedFilm = service.update(film, id);
		
		if(updatedFilm.isEmpty())
			return ResponseEntity.status(404).body(String.format("Film with ID %s does not exist", id));
			
		return ResponseEntity.ok(updatedFilm.get());
	}
		
		
	//Brisanje filma
	@DeleteMapping("/film/{id}")
	public ResponseEntity<?> deleteFilm(@PathVariable Long id)
	{
		if(service.existsById(id)) {
			service.delete(id);
			return ResponseEntity.status(204).body(null);
		}
			
		return ResponseEntity.status(404).body(String.format("Film with ID %s does not exist", id));
	}
}
