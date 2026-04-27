package rva.service;

import java.util.List;

import org.springframework.stereotype.Service;

import rva.model.Bioskop;
import rva.model.Sala;

@Service
public interface SalaService extends CrudService<Sala> {
	List<Sala> getSaleByKapacitet(int kapacitet);
	
	List<Sala> getSaleByBioskop(Bioskop bioskop);

}
