package rva.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rva.model.Sala;

@Service
public interface SalaService extends CrudService<Sala> {
	Page<Sala> getSaleByBioskop(Long bioskopId, Pageable pageable);
	
	Page<Sala> naprednaPretraga(Integer kapacitet, Integer brojRedova, Long bioskopId, Pageable pageable);
}
