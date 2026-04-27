package rva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rva.model.Sala;
import rva.model.Bioskop;

public interface SalaRepository extends JpaRepository<Sala, Long>{
	
	//pretraga po integeru
	List<Sala> findByKapacitetLessThan(int kapacitet);

	//pretraga po stranom ključu
	List<Sala> findByBioskop(Bioskop bioskop);
	
}