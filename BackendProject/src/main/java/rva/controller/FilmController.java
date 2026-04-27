package rva.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rva.implementation.FilmServiceImpl;
import rva.model.Film;

@RestController
public class FilmController {
	@Autowired
	private FilmServiceImpl service;
	
	@GetMapping("/filmovi")
	public ResponseEntity<?> getFilmovi()
	{
		List<Film> filmovi = service.getAll();
		
		if(filmovi.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema filmova");
		} else {
			return ResponseEntity.ok(filmovi);
		}
	}
}
