package rva.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rva.implementation.SalaServiceImpl;
import rva.model.Sala;

@RestController
public class SalaController {
	
	@Autowired
	private SalaServiceImpl service;
	
	@GetMapping("/sale")
	public ResponseEntity<?> getSale() {
		List<Sala> sale = service.getAll();
		if(sale.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sale found");
		else 
			return ResponseEntity.ok(sale);
	}

}