package rva.service;

import java.util.List;

import org.springframework.stereotype.Service;

import rva.model.Film;

@Service
public interface FilmService extends CrudService<Film> {
	List<Film> getFilmoviByNaziv(String naziv);
}
