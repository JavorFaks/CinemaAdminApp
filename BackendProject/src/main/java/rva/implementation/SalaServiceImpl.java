package rva.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import rva.model.Bioskop;
import rva.model.Sala;
import rva.repository.SalaRepository;
import rva.service.BioskopService;
import rva.service.SalaService;


@Component
public class SalaServiceImpl implements SalaService {
	
	@Autowired
	private SalaRepository repo;

	@Autowired
	private BioskopService bService;
	
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
	public Sala create(Sala sala) {
		// Da li je korisnik uopšte poslao Bioskop
	    if (sala.getBioskop() == null || sala.getBioskop().getId() == 0) {
	        throw new IllegalArgumentException("Morate poslati ID bioskopa.");
	    }

	    // Da li Bioskop postoji u bazi
	    if (!bService.existsById(sala.getBioskop().getId())) {
	        throw new EntityNotFoundException("Bioskop sa ID " + sala.getBioskop().getId() + " ne postoji.");
	    }

	    return repo.save(sala);
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
	public List<Sala> getSaleByBioskop(Bioskop bioskop) {
		return repo.findByBioskop(bioskop);
	}
	
	@Override 
	public List<Sala> naprednaPretraga(Integer kapacitet, Integer brojRedova, Long bioskopId)
	{
		Specification<Sala> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction(); //prazno
		
		if(kapacitet!=null)
		{
			spec = spec.and((root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThanOrEqualTo(root.get("kapacitet"), kapacitet)
            );
		}
		
		if(brojRedova!=null)
		{
			spec = spec.and((root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThanOrEqualTo(root.get("brojRedova"), brojRedova)
            );
		}
		
		if (bioskopId != null) {
	        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bioskop").get("id"), bioskopId));
	    }
		
		return repo.findAll(spec);
	}

}