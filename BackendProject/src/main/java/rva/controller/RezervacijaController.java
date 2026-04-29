package rva.controller;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import rva.service.FilmService;
import rva.service.RezervacijaService;
import rva.service.SalaService;
import rva.model.Film;
import rva.model.Rezervacija;
import rva.model.Sala;


@RestController
public class RezervacijaController {
	@Autowired
	private RezervacijaService service;
	
	@Autowired
	private FilmService filmService;
	
	@Autowired
	private SalaService salaService;
	
	@GetMapping("/rezervacija")
	public ResponseEntity<?> getRezervacija(@RequestParam(required=false) Long id)
	{
		if (id!=null) {
			//get by id
			Optional<Rezervacija> rezervacija = service.findById(id);
			if(rezervacija.isEmpty())
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rezevrvacije not found");
			else
				return ResponseEntity.ok(rezervacija.get());
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
	
	
	@GetMapping("/rezervacija/neplacene")
	public ResponseEntity<?> getNeplaceneRezervacije()
	{
		List<Rezervacija> rezervacije = service.getNeplaceneRezervacije();
		
		if(rezervacije.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no neplacene rezervacije");
		}
		
		return ResponseEntity.ok(rezervacije);
	}
	
	@GetMapping("/rezervacija/datum")
	public ResponseEntity<?> getRezervacijeZaDatum(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date datum)
	{
		List<Rezervacija> rezervacije = service.getRezervacijeZaDatum(datum);
		
		if(rezervacije.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema rezervacija za izabrani datum.");
	    }
		
		return ResponseEntity.ok(rezervacije);
	}
	
	
	@GetMapping("/rezervacija/predstojece")
	public ResponseEntity<?> getPredstojeceRezervacije(
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date datum) {
	    
	    List<Rezervacija> rezervacije = service.getPredstojeceRezervacije(datum);
	    if(rezervacije.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema predstojecih rezervacija.");
	    }
	    return ResponseEntity.ok(rezervacije);
	}
	
	@GetMapping("/rezervacija/istorija")
	public ResponseEntity<?> getIstorijaRezervacije(
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date datum) {
	    
	    List<Rezervacija> rezervacije = service.getIstorijaRezervacija(datum);
	    if(rezervacije.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema istorije rezervacija.");
	    }
	    return ResponseEntity.ok(rezervacije);
	}
	
	//Dodavanje rezervacije
	@PostMapping("/rezervacija")
	public ResponseEntity<?> createRezervacija(@RequestBody Rezervacija rezervacija){
		Rezervacija createdRezervacija = service.create(rezervacija);
		
		//Lokacija novo kreirane stavke
		URI uri = URI.create(String.format("/rezervacija/%s", createdRezervacija.getId()));
		
		return ResponseEntity.created(uri).body(createdRezervacija);
	}
	
	
	//Modifikacije vec postojece rezervacije
	@PutMapping("/rezervacija/{id}")
	public ResponseEntity<?> updateRezervacija(@RequestBody Rezervacija rezervacija, @PathVariable Long id)
	{
		Optional<Rezervacija> updatedRezervacija = service.update(rezervacija, id);
		
		if(updatedRezervacija.isEmpty())
			return ResponseEntity.status(404).body(String.format("Rezervacija with ID %s does not exist", id));
		
		return ResponseEntity.ok(updatedRezervacija.get());
	}
	
	
	//Brisanje rezervacije
	@DeleteMapping("/rezervacija/{id}")
	public ResponseEntity<?> deleteRezervacija(@PathVariable Long id)
	{
		if(service.existsById(id)) {
			service.delete(id);
			return ResponseEntity.status(204).body(null);
		}
		
		return ResponseEntity.status(404).body(String.format("Rezervacija with ID %s does not exist", id));
	}
}
