package rva.service;

import java.util.List;

import org.springframework.stereotype.Service;

import rva.model.Film;
import rva.model.Rezervacija;
import rva.model.Sala;


@Service
public interface RezervacijaService extends CrudService<Rezervacija> {
	List<Rezervacija> getRezervacijeByCena(double cena);
	
	List<Rezervacija> getRezervacijeByFilm(Film film);

	List<Rezervacija> getRezervacijeBySala(Sala sala);
}
