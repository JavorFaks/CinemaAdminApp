package rva.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rva.model.Sala;
import rva.service.SalaService;

@CrossOrigin
@RestController
public class SalaController {
	
	@Autowired
	private SalaService salaService;
	
	@GetMapping("/sala")
	public ResponseEntity<Page<Sala>> getSala(Pageable pageable) {
		Page<Sala> sale = salaService.getAll(pageable);
		return ResponseEntity.ok(sale);
	}
	
	@GetMapping("/sala/{id}")
	public ResponseEntity<Sala> getSalaById(@PathVariable Long id) {
		Sala sala = salaService.findById(id);
		return ResponseEntity.ok(sala);
	}
	
	@GetMapping("/sala/bioskop")
	public ResponseEntity<Page<Sala>> getSalaByBioskop(@RequestParam Long bioskopId, Pageable pageable)
	{
		Page<Sala> sale = salaService.getSaleByBioskop(bioskopId, pageable);
		
		return ResponseEntity.ok(sale);
	}
	
	
	@GetMapping("/sala/pretraga")
	public ResponseEntity<Page<Sala>> naprednaPretraga(
	        @RequestParam(required = false) Integer kapacitet,
	        @RequestParam(required = false) Integer brojRedova,
	        @RequestParam(required = false) Long bioskopId,
	        Pageable pageable
	) {
		
		
	    Page<Sala> sale = salaService.naprednaPretraga(kapacitet, brojRedova, bioskopId, pageable);
	   
	    
	    return ResponseEntity.ok(sale);
	}
	
	//Dodavanje nove sale
	@PostMapping("/sala")
	public ResponseEntity<?> createSala(@Valid @RequestBody Sala sala){
		Sala createdSala = salaService.create(sala);
		
		//Lokacija novo kreirane stavke
		URI uri = URI.create(String.format("/sala/%s", createdSala.getId()));
		
		return ResponseEntity.created(uri).body(createdSala);
	}
	
	//Modifikacija
	@PutMapping("/sala/{id}")
	public ResponseEntity<Sala> updateSala(@Valid @RequestBody Sala sala, @PathVariable Long id)
	{
		Sala updatedSala = salaService.update(sala, id);
	
		return ResponseEntity.ok(updatedSala);
	}
		
		
	//Brisanje filma
	@DeleteMapping("/sala/{id}")
	public ResponseEntity<Void> deleteSala(@PathVariable Long id)
	{
		salaService.delete(id);
		
		return ResponseEntity.status(204).body(null);
	}	
	
	
	
}