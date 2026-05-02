package rva.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import rva.model.Bioskop;
import rva.repository.BioskopRepository;
import rva.service.BioskopService;


@Component
public class BioskopServiceImpl implements BioskopService {

	@Autowired
	private BioskopRepository repo;
	
	@Override
	public List<Bioskop> getAll() {
		List<Bioskop> bioskopi = repo.findAll();
		
		if(bioskopi.isEmpty()) {
			throw new EntityNotFoundException("Trenutno nema nijednog bioskopa.");
		}
		return bioskopi;
	}

	@Override
	public boolean existsById(long id) {
		return repo.existsById(id);
	}

	@Override
	public Bioskop findById(long id) {
		Optional<Bioskop> bioskop = repo.findById(id);
		
		if(bioskop.isEmpty()) {
			throw new EntityNotFoundException("Bioskop sa ID-jem " + id + " ne postoji.");
		}
		return bioskop.get();
	}

	@Override
	public Bioskop create(Bioskop body) {
		return repo.save(body);
	}

	@Override
	public Bioskop update(Bioskop body, long id) {
		if(existsById(id)) {
			body.setId(id);
			return repo.save(body);
		} else{
			throw new EntityNotFoundException("Bioskop sa ID-jem " + id + " ne postoji.");
		}
	}

	@Override
	public void delete(long id) {
		if(existsById(id)) {
			repo.deleteById(id);
		} else {
			throw new EntityNotFoundException("Bioskop sa ID-jem " + id + " ne postoji.");
		}
		
	}
	
	@Override
	public List<Bioskop> getBioskopPoGradu(String grad)
	{
		List<Bioskop> bioskopi = repo.findByAdresaContainingIgnoreCase(grad);
		
		if(bioskopi.isEmpty()) {
			throw new EntityNotFoundException("Bioskop iz ovog grada ne postoje.");
		}
		
		return bioskopi;
	}

}
