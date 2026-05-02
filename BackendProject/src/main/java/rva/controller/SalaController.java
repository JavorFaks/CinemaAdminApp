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
import rva.model.Sala;
import rva.service.SalaService;

@RestController
public class SalaController {
	
	@Autowired
	private SalaService salaService;
	
	@GetMapping("/sala")
	public ResponseEntity<List<Sala>> getSala() {
		List<Sala> sale = salaService.getAll();
		return ResponseEntity.ok(sale);
	}
	
	@GetMapping("/sala/{id}")
	public ResponseEntity<Sala> getSalaById(@PathVariable Long id) {
		Sala sala = salaService.findById(id);
		return ResponseEntity.ok(sala);
	}
	
	@GetMapping("/sala/bioskop")
	public ResponseEntity<List<Sala>> getSalaByBioskop(@RequestParam Long bioskopId)
	{
		List<Sala> sale = salaService.getSaleByBioskop(bioskopId);
		
		return ResponseEntity.ok(sale);
	}
	
	
	@GetMapping("/sala/pretraga")
	public ResponseEntity<List<Sala>> naprednaPretraga(
	        @RequestParam(required = false) Integer kapacitet,
	        @RequestParam(required = false) Integer brojRedova,
	        @RequestParam(required = false) Long bioskopId
	) {
		
		
	    List<Sala> sale = salaService.naprednaPretraga(kapacitet, brojRedova, bioskopId);
	   
	    
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