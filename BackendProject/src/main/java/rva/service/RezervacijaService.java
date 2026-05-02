package rva.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import rva.model.Rezervacija;


@Service
public interface RezervacijaService extends CrudService<Rezervacija> {
	
	//Rezervacije za odredjeni film
	List<Rezervacija> getRezervacijeByFilm(Long filmId);

	//Rezervacije za datu salu
	List<Rezervacija> getRezervacijeBySala(Long salaId);
	
	
	//Rezervacije za odredjeni datum
	List<Rezervacija> getRezervacijeZaDatum(Date datum);
		
	//Za istoriju rezervacija ili za predstojece rezervacije
	List<Rezervacija> getIstorijaRezervacija(Date datum);
	List<Rezervacija> getPredstojeceRezervacije(Date datum);
	
	
	List<Rezervacija> getNeplaceneRezervacije();
}
