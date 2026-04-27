package rva.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rva.implementation.RezervacijaServiceImpl;
import rva.model.Rezervacija;


@RestController
public class RezervacijaController {
	@Autowired
	private RezervacijaServiceImpl service;
	
	@GetMapping("/rezervacije")
	public ResponseEntity<?> getRezervacije()
	{
		List<Rezervacija> rezervacije = service.getAll();
		
		if(rezervacije.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema rezervacija");
		} else {
			return ResponseEntity.ok(rezervacije);
		}
	}
}
