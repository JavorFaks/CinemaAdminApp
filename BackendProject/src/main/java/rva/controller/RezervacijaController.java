package rva.controller;

import java.net.URI;
import java.time.LocalDate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public ResponseEntity<Page<Rezervacija>> getRezervacija(Pageable pageable)
	{
		Page<Rezervacija> r = service.getAll(pageable);
		return ResponseEntity.ok(r);
	}
	
	@GetMapping("/rezervacija/{id}")
	public ResponseEntity<Rezervacija> getRezervacijaById(@PathVariable Long id)
	{
		Rezervacija rezervacija = service.findById(id);
		return ResponseEntity.ok(rezervacija);
	}
	

	@GetMapping("rezervacija/film")
	public ResponseEntity<Page<Rezervacija>> getRezervacijaByFilm(@RequestParam Long filmId, Pageable pageable)
	{
		Page<Rezervacija> rezervacije = service.getRezervacijeByFilm(filmId, pageable);
		
		return ResponseEntity.ok(rezervacije);
	}
	
	@GetMapping("/rezervacija/sala")
	public ResponseEntity<Page<Rezervacija>> getRezervacijaBySala(@RequestParam Long salaId, Pageable pageable)
	{
		Page<Rezervacija> rezervacije = service.getRezervacijeBySala(salaId, pageable);
		
		return ResponseEntity.ok(rezervacije);
		
	}
	
	@GetMapping("/rezervacija/pretraga")
	public ResponseEntity<Page<Rezervacija>> naprednaPretraga(@RequestParam(required = false) Long filmId,
	        @RequestParam(required = false) Long salaId,
	        @RequestParam(required = false) Boolean placeno, 
	        @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate datumOd,
	        @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate datumDo,
	        Pageable pageable)
	{
		Page<Rezervacija> rezervacije = service.naprednaPretraga(filmId, salaId, placeno, datumOd, datumDo, pageable);
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
