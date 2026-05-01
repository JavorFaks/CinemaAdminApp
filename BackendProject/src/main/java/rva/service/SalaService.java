package rva.service;

import java.util.List;

import org.springframework.stereotype.Service;

import rva.model.Bioskop;
import rva.model.Sala;

@Service
public interface SalaService extends CrudService<Sala> {
	List<Sala> getSaleByBioskop(Bioskop bioskop);
	
	List<Sala> naprednaPretraga(Integer kapacitet, Integer brojRedova, Long bioskopId);
}
