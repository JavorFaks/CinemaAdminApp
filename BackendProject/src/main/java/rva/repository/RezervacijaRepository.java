package rva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rva.model.Film;
import rva.model.Rezervacija;
import rva.model.Sala;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long>{
	
	//pretraga po decimalnom broju
	List<Rezervacija> findByCenaKarteLessThan(double cena);

	//pretraga po stranom ključu
	List<Rezervacija> findByFilm(Film film);
	
	
	//pretraga po stranom ključu
	List<Rezervacija> findBySala(Sala sala);
}