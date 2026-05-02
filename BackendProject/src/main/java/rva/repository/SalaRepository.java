package rva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import rva.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long>, JpaSpecificationExecutor<Sala>{
	//pretraga po stranom ključu
	List<Sala> findByBioskop_Id(Long bioskopId);
	
	
}