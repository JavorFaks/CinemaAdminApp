package rva.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rva.model.Film;

@Service
public interface FilmService extends CrudService<Film> {
	Page<Film> naprednaPretraga(String naziv, String zanr, Integer maxMinuta, Pageable pageable);
	
	List<String> getDistinctZanrovi();
}
