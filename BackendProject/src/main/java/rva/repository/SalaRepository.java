package rva.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import rva.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long>, JpaSpecificationExecutor<Sala>{
	//pretraga po stranom ključu
	Page<Sala> findByBioskop_Id(Long bioskopId, Pageable pageable);
	
	
}