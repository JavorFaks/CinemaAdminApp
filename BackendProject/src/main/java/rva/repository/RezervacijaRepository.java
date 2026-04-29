package rva.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rva.model.Film;
import rva.model.Rezervacija;
import rva.model.Sala;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long>{

	//pretraga po stranom ključu
	List<Rezervacija> findByFilm(Film film);
	
	
	//pretraga po stranom ključu
	List<Rezervacija> findBySala(Sala sala);
	
	
	//Sve neplacene rezervacije
	List<Rezervacija> findByPlacenoFalse();
	
	
	//Rezervacije za odredjeni datum
	List<Rezervacija> findByDatum(Date datum);
	
	//Za istoriju rezervacija ili za predstojece rezervacije
	List<Rezervacija> findByDatumBefore(Date datum);
	List<Rezervacija> findByDatumAfter(Date datum);
}