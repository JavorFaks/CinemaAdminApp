package rva.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rva.model.Rezervacija;


@Service
public interface RezervacijaService extends CrudService<Rezervacija> {
	
	//Rezervacije za odredjeni film
	Page<Rezervacija> getRezervacijeByFilm(Long filmId, Pageable pageable);

	//Rezervacije za datu salu
	Page<Rezervacija> getRezervacijeBySala(Long salaId, Pageable pageable);
	
	
	//Napredna pretraga
	Page<Rezervacija> naprednaPretraga(Long filmId, Long salaId, Boolean placeno, LocalDate datumOd, LocalDate datumDo, Pageable pageable);
}
