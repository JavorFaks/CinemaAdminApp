package rva.controller;

import java.net.URI;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import rva.service.RezervacijaService;
import rva.model.Rezervacija;


@RestController
public class RezervacijaController {
	@Autowired
	private RezervacijaService service;
	
	
	@GetMapping("/rezervacija")
	public ResponseEntity<List<Rezervacija>> getRezervacija(@RequestParam(required=false) Long id)
	{
		List<Rezervacija> r = service.getAll();
		return ResponseEntity.ok(r);
	}
	
	@GetMapping("/rezervacija/{id}")
	public ResponseEntity<Rezervacija> getRezervacijaById(@PathVariable Long id)
	{
		Rezervacija rezervacija = service.findById(id);
		return ResponseEntity.ok(rezervacija);
	}
	

	@GetMapping("rezervacija/film")
	public ResponseEntity<List<Rezervacija>> getRezervacijaByFilm(@RequestParam Long filmId)
	{
		List<Rezervacija> rezervacije = service.getRezervacijeByFilm(filmId);
		
		return ResponseEntity.ok(rezervacije);
	}
	
	@GetMapping("/rezervacija/sala")
	public ResponseEntity<List<Rezervacija>> getRezervacijaBySala(@RequestParam Long salaId)
	{
		List<Rezervacija> rezervacije = service.getRezervacijeBySala(salaId);
		
		return ResponseEntity.ok(rezervacije);
		
	}
	
	
	@GetMapping("/rezervacija/neplacene")
	public ResponseEntity<List<Rezervacija>> getNeplaceneRezervacije()
	{
		List<Rezervacija> rezervacije = service.getNeplaceneRezervacije();
		
		return ResponseEntity.ok(rezervacije);
	}
	
	@GetMapping("/rezervacija/datum")
	public ResponseEntity<List<Rezervacija>> getRezervacijeZaDatum(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date datum)
	{
		List<Rezervacija> rezervacije = service.getRezervacijeZaDatum(datum);
		
		return ResponseEntity.ok(rezervacije);
	}
	
	
	@GetMapping("/rezervacija/predstojece")
	public ResponseEntity<List<Rezervacija>> getPredstojeceRezervacije(
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date datum) {
	    
	    List<Rezervacija> rezervacije = service.getPredstojeceRezervacije(datum);
	   
	    return ResponseEntity.ok(rezervacije);
	}
	
	@GetMapping("/rezervacija/istorija")
	public ResponseEntity<List<Rezervacija>> getIstorijaRezervacije(
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date datum) {
	    
	    List<Rezervacija> rezervacije = service.getIstorijaRezervacija(datum);
	    
	    return ResponseEntity.ok(rezervacije);
	}
	
	//Dodavanje rezervacije
	@PostMapping("/rezervacija")
	public ResponseEntity<?> createRezervacija(@Valid @RequestBody Rezervacija rezervacija){
		Rezervacija createdRezervacija = service.create(rezervacija);
		
		//Lokacija novo kreirane stavke
		URI uri = URI.create(String.format("/rezervacija/%s", createdRezervacija.getId()));
		
		return ResponseEntity.created(uri).body(createdRezervacija);
	}
	
	
	//Modifikacije vec postojece rezervacije
	@PutMapping("/rezervacija/{id}")
	public ResponseEntity<Rezervacija> updateRezervacija(@Valid @RequestBody Rezervacija rezervacija, @PathVariable Long id)
	{
		Rezervacija updatedRezervacija = service.update(rezervacija, id);
		
		return ResponseEntity.ok(updatedRezervacija);
	}
	
	
	//Brisanje rezervacije
	@DeleteMapping("/rezervacija/{id}")
	public ResponseEntity<Void> deleteRezervacija(@PathVariable Long id)
	{
		service.delete(id);
		return ResponseEntity.status(204).body(null);
	
	}
}
