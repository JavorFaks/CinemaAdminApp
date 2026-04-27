package rva.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rva.model.Bioskop;
import rva.model.Sala;
import rva.repository.SalaRepository;
import rva.service.SalaService;


@Component
public class SalaServiceImpl implements SalaService {
	
	@Autowired
	private SalaRepository repo;

	@Override
	public List<Sala> getAll() {
		return repo.findAll();
	}

	@Override
	public boolean existsById(long id) {
		return repo.existsById(id);
	}

	@Override
	public Optional<Sala> findById(long id) {
		return repo.findById(id);
	}

	@Override
	public Sala create(Sala body) {
		return repo.save(body);
	}

	@Override
	public Optional<Sala> update(Sala body, long id) {
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
	public List<Sala> getSaleByKapacitet(int kapacitet) {
		return repo.findByKapacitetLessThan(kapacitet);
	}

	@Override
	public List<Sala> getSaleByBioskop(Bioskop bioskop) {
		return repo.findByBioskop(bioskop);
	}

}