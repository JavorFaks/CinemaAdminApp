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
import rva.implementation.BioskopServiceImpl;
import rva.model.Bioskop;

@RestController
public class BioskopController {
	
	@Autowired
	private BioskopServiceImpl service;
	
	@GetMapping("/bioskop")
	public ResponseEntity<?> getBioskopi()
	{
		//Ako nije unesen id onda trazi sve bioskope
		List<Bioskop> bioskopi = service.getAll();
			
		if(bioskopi.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema bioskopa");
		} else {
			return ResponseEntity.ok(bioskopi);
		}
	}
		
		
	
	
	// Vracanje samo jednog bioskopa
	@GetMapping("/bioskop/{id}")
	public ResponseEntity<?> getFilmById(@PathVariable Long id) {
		Optional<Bioskop> bioskop = service.findById(id);
		
		if(bioskop.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bioskop sa ID-jem " + id + " ne postoji.");
		}
			
		return ResponseEntity.ok(bioskop.get());
	}
	
	@GetMapping("/bioskop/grad")
	public ResponseEntity<?> getBioskopiPoGradu(@RequestParam String grad)
	{
		List<Bioskop> bioskopi = service.getBioskopPoGradu(grad);
		
		if(bioskopi.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema bioskopa u ovom gradu");
		} else {
			return ResponseEntity.ok(bioskopi);
		}
	}
	
	
	@PostMapping("/bioskop")
	public ResponseEntity<?> createBioskop(@Valid @RequestBody Bioskop bioskop){
		Bioskop createdBioskop = service.create(bioskop);
		
		//Lokacija novo kreirane stavke
		URI uri = URI.create(String.format("/bioskop/%s", createdBioskop.getId()));
		
		return ResponseEntity.created(uri).body(createdBioskop);
	}
	
	
	//Modifikacije vec postojece rezervacije
	@PutMapping("/bioskop/{id}")
	public ResponseEntity<?> updateBioskop(@Valid @RequestBody Bioskop bioskop, @PathVariable Long id)
	{
		Optional<Bioskop> updatedBioskop = service.update(bioskop, id);
		
		if(updatedBioskop.isEmpty())
			return ResponseEntity.status(404).body(String.format("Bioskop with ID %s does not exist", id));
			
		return ResponseEntity.ok(updatedBioskop.get());
	}
		
		
	//Brisanje rezervacije
	@DeleteMapping("/bioskop/{id}")
	public ResponseEntity<?> deleteBioskop(@PathVariable Long id)
	{
		if(service.existsById(id)) {
			service.delete(id);
			return ResponseEntity.status(204).body(null);
		}
			
		return ResponseEntity.status(404).body(String.format("Bioskop with ID %s does not exist", id));
	}
}
