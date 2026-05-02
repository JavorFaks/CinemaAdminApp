package rva.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public ResponseEntity<List<Film>> getFilm()
	{
		List<Film> filmovi = service.getAll();
		return ResponseEntity.ok(filmovi);
	}
	
	// Vracanje samo jednog filma
	@GetMapping("/film/{id}")
	public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
		Film film = service.findById(id);
		return ResponseEntity.ok(film);
	}
	
	@GetMapping("/film/pretraga")
	public ResponseEntity<List<Film>> naprednaPretraga(
	        @RequestParam(required = false) String naziv,
	        @RequestParam(required = false) String zanr,
	        @RequestParam(required = false) Integer maxMinuta
	) {
	    List<Film> filmovi = service.naprednaPretraga(naziv, zanr, maxMinuta);
	    
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
	public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film, @PathVariable Long id)
	{
		Film updatedFilm = service.update(film, id);
		
		return ResponseEntity.ok(updatedFilm);
	}
		
		
	//Brisanje filma
	@DeleteMapping("/film/{id}")
	public ResponseEntity<Void> deleteFilm(@PathVariable Long id)
	{
		service.delete(id);
		return ResponseEntity.status(204).body(null);
	}
}
