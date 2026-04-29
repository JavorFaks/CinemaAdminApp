package rva.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import rva.model.Film;
import rva.model.Rezervacija;
import rva.model.Sala;


@Service
public interface RezervacijaService extends CrudService<Rezervacija> {
	
	//Rezervacije za odredjeni film
	List<Rezervacija> getRezervacijeByFilm(Film film);

	//Rezervacije za datu salu
	List<Rezervacija> getRezervacijeBySala(Sala sala);
	
	
	//Rezervacije za odredjeni datum
	List<Rezervacija> getRezervacijeZaDatum(Date datum);
		
	//Za istoriju rezervacija ili za predstojece rezervacije
	List<Rezervacija> getIstorijaRezervacija(Date datum);
	List<Rezervacija> getPredstojeceRezervacije(Date datum);
	
	
	List<Rezervacija> getNeplaceneRezervacije();
}
