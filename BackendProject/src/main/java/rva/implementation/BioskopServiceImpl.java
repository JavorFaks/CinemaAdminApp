package rva.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rva.model.Bioskop;
import rva.repository.BioskopRepository;
import rva.service.BioskopService;


@Component
public class BioskopServiceImpl implements BioskopService {

	@Autowired
	private BioskopRepository repo;
	
	@Override
	public List<Bioskop> getAll() {
		return repo.findAll();
	}

	@Override
	public boolean existsById(long id) {
		return repo.existsById(id);
	}

	@Override
	public Optional<Bioskop> findById(long id) {
		return repo.findById(id);
	}

	@Override
	public Bioskop create(Bioskop body) {
		return repo.save(body);
	}

	@Override
	public Optional<Bioskop> update(Bioskop body, long id) {
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
	public List<Bioskop> getBioskopPoGradu(String grad)
	{
		return repo.findByAdresaContainingIgnoreCase(grad);
	}

}
