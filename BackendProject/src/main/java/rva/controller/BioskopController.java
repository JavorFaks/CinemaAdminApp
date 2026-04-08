package rva.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rva.implementation.BioskopServiceImpl;
import rva.model.Bioskop;

@RestController
public class BioskopController {
	
	@Autowired
	private BioskopServiceImpl service;
	
	@GetMapping("/bioskopi")
	public ResponseEntity<?> getBioskopi()
	{
		List<Bioskop> bioskopi = service.getAll();
		
		if(bioskopi.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema bioskopa");
		} else {
			return ResponseEntity.ok(bioskopi);
		}
	}
}
