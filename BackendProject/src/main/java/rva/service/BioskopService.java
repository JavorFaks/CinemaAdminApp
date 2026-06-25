package rva.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rva.model.Bioskop;

@Service
public interface BioskopService extends CrudService<Bioskop> {
	Page<Bioskop> getBioskopPoGradu(String grad, Pageable page);
	
	Page<Bioskop> naprednaPretraga(String naziv, String adresa, Pageable page);
}
