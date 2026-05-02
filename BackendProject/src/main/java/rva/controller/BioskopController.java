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
import rva.implementation.BioskopServiceImpl;
import rva.model.Bioskop;

@RestController
public class BioskopController {
	
	@Autowired
	private BioskopServiceImpl service;
	
	@GetMapping("/bioskop")
	public ResponseEntity<List<Bioskop>> getBioskopi()
	{
		List<Bioskop> bioskopi = service.getAll();
		return ResponseEntity.ok(bioskopi);
	}
		
		
	// Vracanje samo jednog bioskopa
	@GetMapping("/bioskop/{id}")
	public ResponseEntity<Bioskop> getBioskopById(@PathVariable Long id) {
		Bioskop bioskop = service.findById(id);
			
		return ResponseEntity.ok(bioskop);
	}
	
	@GetMapping("/bioskop/grad")
	public ResponseEntity<List<Bioskop>> getBioskopiPoGradu(@RequestParam String grad)
	{
		List<Bioskop> bioskopi = service.getBioskopPoGradu(grad);
		
		return ResponseEntity.ok(bioskopi);

	}
	
	
	@PostMapping("/bioskop")
	public ResponseEntity<?> createBioskop(@Valid @RequestBody Bioskop bioskop){
		Bioskop createdBioskop = service.create(bioskop);
		
		//Lokacija novo kreirane stavke
		URI uri = URI.create(String.format("/bioskop/%s", createdBioskop.getId()));
		
		return ResponseEntity.created(uri).body(createdBioskop);
	}
	
	
	//Modifikacije vec postojecg bioskopa
	@PutMapping("/bioskop/{id}")
	public ResponseEntity<Bioskop> updateBioskop(@Valid @RequestBody Bioskop bioskop, @PathVariable Long id)
	{
		Bioskop updatedBioskop = service.update(bioskop, id);
			
		return ResponseEntity.ok(updatedBioskop);
	}
		
		
	//Brisanje bioskopa
	@DeleteMapping("/bioskop/{id}")
	public ResponseEntity<Void> deleteBioskop(@PathVariable Long id)
	{
		service.delete(id);
		return ResponseEntity.status(204).body(null);
	}
}
