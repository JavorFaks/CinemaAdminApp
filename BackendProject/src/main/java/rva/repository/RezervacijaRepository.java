package rva.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import rva.model.Rezervacija;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long>,  JpaSpecificationExecutor<Rezervacija>{

	//pretraga po stranom ključu
	Page<Rezervacija> findByFilm_Id(Long filmId, Pageable pageable);
	
	
	//pretraga po stranom ključu
	Page<Rezervacija> findBySala_Id(Long salaId, Pageable pageable);
	
	
}