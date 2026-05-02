package rva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import rva.model.Rezervacija;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long>,  JpaSpecificationExecutor<Rezervacija>{

	//pretraga po stranom ključu
	List<Rezervacija> findByFilm_Id(Long filmId);
	
	
	//pretraga po stranom ključu
	List<Rezervacija> findBySala_Id(Long salaId);
	
	
}