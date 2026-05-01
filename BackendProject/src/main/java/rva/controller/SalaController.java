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
import rva.implementation.SalaServiceImpl;
import rva.model.Bioskop;
import rva.model.Film;
import rva.model.Rezervacija;
import rva.model.Sala;
import rva.service.BioskopService;
import rva.service.SalaService;

@RestController
public class SalaController {
	
	@Autowired
	private SalaService salaService;
	
	@Autowired
	private BioskopService bioskopService;
	
	@GetMapping("/sala")
	public ResponseEntity<?> getSala() {
		List<Sala> sale = salaService.getAll();
		if(sale.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sale found");
		else 
			return ResponseEntity.ok(sale);
	}
	
	@GetMapping("/sala/{id}")
	public ResponseEntity<?> getSalaById(@PathVariable Long id) {
		Optional<Sala> sala = salaService.findById(id);
		
		if(sala.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sala sa ID-jem " + id + " ne postoji.");
		}
			
		return ResponseEntity.ok(sala.get());
	}
	
	@GetMapping("/sala/bioskop")
	public ResponseEntity<?> getSalaByBioskop(@RequestParam Long bioskopId)
	{
		Optional<Bioskop> bioskop = bioskopService.findById(bioskopId);
		if(bioskop.isEmpty()) {
			return ResponseEntity.status(404).body(String.format("Bioskop with ID %s does not exist", bioskopId));
		}
		
		List<Sala> sale = salaService.getSaleByBioskop(bioskop.get());
		
		if(sale.isEmpty()) {
			return ResponseEntity.status(404).body("Sale for this film does not exist");
		}
		
		return ResponseEntity.ok(sale);
	}
	
	
	@GetMapping("/sala/pretraga")
	public ResponseEntity<?> naprednaPretraga(
	        @RequestParam(required = false) Integer kapacitet,
	        @RequestParam(required = false) Integer brojRedova,
	        @RequestParam(required = false) Long bioskopId
	) {
		
		if(bioskopId!=null) {
			Optional<Bioskop> bioskop = bioskopService.findById(bioskopId);
			if(bioskop.isEmpty()) {
				return ResponseEntity.status(404).body(String.format("Bioskop with ID %s does not exist", bioskopId));
			}
		}
		
		
	    List<Sala> sale = salaService.naprednaPretraga(kapacitet, brojRedova, bioskopId);
	    
	    if(sale.isEmpty()) {
	        return ResponseEntity.status(404).body("Nema sali sa ovim kriterijumima!");
	    }
	    
	    return ResponseEntity.ok(sale);
	}
	
	//Dodavanje nove sale
	@PostMapping("/sala")
	public ResponseEntity<?> createFilm(@Valid @RequestBody Sala sala){
		Sala createdSala = salaService.create(sala);
		
		//Lokacija novo kreirane stavke
		URI uri = URI.create(String.format("/sala/%s", createdSala.getId()));
		
		return ResponseEntity.created(uri).body(createdSala);
	}
	
	//Modifikacija
	@PutMapping("/sala/{id}")
	public ResponseEntity<?> updateSala(@Valid @RequestBody Sala sala, @PathVariable Long id)
	{
		Optional<Sala> updatedSala = salaService.update(sala, id);
		
		if(updatedSala.isEmpty())
			return ResponseEntity.status(404).body(String.format("Sala with ID %s does not exist", id));
			
		return ResponseEntity.ok(updatedSala.get());
	}
		
		
	//Brisanje filma
	@DeleteMapping("/sala/{id}")
	public ResponseEntity<?> deleteSala(@PathVariable Long id)
	{
		if(salaService.existsById(id)) {
			salaService.delete(id);
			return ResponseEntity.status(204).body(null);
		}
			
		return ResponseEntity.status(404).body(String.format("Sala with ID %s does not exist", id));
	}	
	
	
	
}