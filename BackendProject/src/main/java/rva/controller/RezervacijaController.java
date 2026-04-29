package rva.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rva.implementation.FilmServiceImpl;
import rva.implementation.RezervacijaServiceImpl;
import rva.implementation.SalaServiceImpl;
import rva.model.Film;
import rva.model.Rezervacija;
import rva.model.Sala;


@RestController
public class RezervacijaController {
	@Autowired
	private RezervacijaServiceImpl service;
	
	@Autowired
	private FilmServiceImpl filmService;
	
	@Autowired
	private SalaServiceImpl salaService;
	
	@GetMapping("/rezervacija")
	public ResponseEntity<?> getRezervacija(@RequestParam(required=false) Double cenaKarte, @RequestParam(required=false) Long id)
	{
		if(cenaKarte!=null && id==null) {
			//get by cena
			List<Rezervacija> rezervacije = service.getRezervacijeByCena(cenaKarte);
			if(rezervacije.isEmpty())
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rezevrvacije not found");
			else 
				return ResponseEntity.ok(rezervacije);
		} else if (cenaKarte==null && id!=null) {
			//get by id
			Optional<Rezervacija> rezervacija = service.findById(id);
			if(rezervacija.isEmpty())
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rezevrvacije not found");
			else
				return ResponseEntity.ok(rezervacija.get());
		} else if(cenaKarte!=null && id!=null) {
			//greska
			return ResponseEntity.badRequest().body("Request can only accept either ID or cenaKarte");
		} 
		
		//get all
		return ResponseEntity.ok(service.getAll());
	}
	

	@GetMapping("rezervacija/film")
	public ResponseEntity<?> getRezervacijaByFilm(@RequestParam Long filmId)
	{
		Optional<Film> film = filmService.findById(filmId);
		if(film.isEmpty()) {
			return ResponseEntity.status(404).body(String.format("Film with ID %s does not exist", filmId));
		}
		
		List<Rezervacija> rezervacije = service.getRezervacijeByFilm(film.get());
		
		if(rezervacije.isEmpty()) {
			return ResponseEntity.status(404).body("Rezervacija for this film does not exist");
		}
		
		return ResponseEntity.ok(rezervacije);
	}
	
	@GetMapping("/rezervacija/sala")
	public ResponseEntity<?> getRezervacijaBySala(@RequestParam Long salaId)
	{
		Optional<Sala> sala = salaService.findById(salaId);
		if(sala.isEmpty())
			return ResponseEntity.status(404).body(String.format("Sala with ID %s does not exist", salaId));
		
		List<Rezervacija> rezervacije = service.getRezervacijeBySala(sala.get());
		
		if(rezervacije.isEmpty())
			return ResponseEntity.status(404).body("Rezervacija for this sala does not exist");
		
		return ResponseEntity.ok(rezervacije);
		
	}
	
	@PostMapping("/rezervacija")
	public ResponseEntity<?> createRezervacija(@RequestBody Rezervacija rezervacija){
		Rezervacija createdRezervacija = service.create(rezervacija);
		
		//Lokacija novo kreirane stavke
		URI uri = URI.create(String.format("/rezervacija?id=%s", createdRezervacija.getId()));
		
		return ResponseEntity.created(uri).body(createdRezervacija);
	}
	
	@PutMapping("/rezervacija")
	public ResponseEntity<?> updateRezervacija(@RequestBody Rezervacija rezervacija, @RequestParam Long id)
	{
		Optional<Rezervacija> updatedRezervacija = service.update(rezervacija, id);
		
		if(updatedRezervacija.isEmpty())
			return ResponseEntity.status(404).body(String.format("Rezervacija with ID %s does not exist", id));
		
		return ResponseEntity.ok(updatedRezervacija.get());
	}
	
	@DeleteMapping("/rezervacija")
	public ResponseEntity<?> deleteRezervacija(@RequestParam Long id)
	{
		if(service.existsById(id)) {
			service.delete(id);
			return ResponseEntity.status(204).body(null);
		}
		
		return ResponseEntity.status(404).body(String.format("Rezervacija with ID %s does not exist", id));
	}
}
