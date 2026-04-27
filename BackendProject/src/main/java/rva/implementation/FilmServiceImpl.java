package rva.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rva.model.Film;
import rva.repository.FilmRepository;
import rva.service.FilmService;

@Component
public class FilmServiceImpl implements FilmService{
	
	@Autowired 
	private FilmRepository repo;
	
	@Override
	public List<Film> getAll() {
		return repo.findAll();
	}

	@Override
	public boolean existsById(long id) {
		return repo.existsById(id);
	}

	@Override
	public Optional<Film> findById(long id) {
		return repo.findById(id);
	}

	@Override
	public Film create(Film body) {
		return repo.save(body);
	}

	@Override
	public Optional<Film> update(Film body, long id) {
		if(existsById(id)) {
			body.setId(id);
			return Optional.of(repo.save(body));
		} 
		return Optional.empty();
	}

	@Override
	public void delete(long id) {
		repo.deleteById(id);

	}

	@Override
	public List<Film> getFilmoviByNaziv(String naziv) {
		return repo.findByNazivContainingIgnoreCase(naziv);
	}
}
