package rva.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rva.model.Film;
import rva.model.Rezervacija;
import rva.model.Sala;
import rva.repository.RezervacijaRepository;
import rva.service.RezervacijaService;




@Component
public class RezervacijaServiceImpl implements RezervacijaService {
	
	@Autowired
	private RezervacijaRepository repo;

	@Override
	public List<Rezervacija> getAll() {
		return repo.findAll();
	}

	@Override
	public boolean existsById(long id) {
		return repo.existsById(id);
	}

	@Override
	public Optional<Rezervacija> findById(long id) {
		return repo.findById(id);
	}

	@Override
	public Rezervacija create(Rezervacija body) {
		return repo.save(body);
	}

	@Override
	public Optional<Rezervacija> update(Rezervacija body, long id) {
		if(repo.existsById(id)) {
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
	public List<Rezervacija> getRezervacijeByCena(double cena) {
		return repo.findByCenaKarteLessThan(cena);
	}

	@Override
	public List<Rezervacija> getRezervacijeByFilm(Film film) {
		return repo.findByFilm(film);
	}
	
	@Override
	public List<Rezervacija> getRezervacijeBySala(Sala sala) {
		return repo.findBySala(sala);
	}

}