package rva.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
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
	public Page<Sala> getAll(Pageable pageable) {
		Page<Sala> sale = repo.findAll(pageable);
		
		return sale;
	}

	@Override
	public boolean existsById(long id) {
		return repo.existsById(id);
	}

	@Override
	public Sala findById(long id) {
		Optional<Sala> sala = repo.findById(id);
		
		if(sala.isEmpty()) {
			throw new EntityNotFoundException("Sala sa ID-jem " + id + " ne postoji.");
		}
		
		return sala.get();
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
	public Sala update(Sala body, long id) {
		if(repo.existsById(id)) {
			body.setId(id);
			if(!bService.existsById(body.getBioskop().getId())) {
				throw new EntityNotFoundException("Bioskop sa ID-jem " + id + " ne postoji.");
			}
			return repo.save(body);
		} else {
			throw new EntityNotFoundException("Sala sa ID-jem " + id + " ne postoji.");
		}
		
		
		
	}

	@Override
	public void delete(long id) {
		if(existsById(id)){
			repo.deleteById(id);
		} else {
			throw new EntityNotFoundException("Sala sa ID-jem " + id + " ne postoji.");
		}
		
		
	}

	@Override
	public Page<Sala> getSaleByBioskop(Long bioskopId, Pageable pageable) {
		if(!bService.existsById(bioskopId)) {
			throw new EntityNotFoundException("Bioskop sa ID-jem " + bioskopId + " ne postoji.");
		} else {
			Page<Sala> sale = repo.findByBioskop_Id(bioskopId, pageable);
			if(sale.isEmpty()) {
				throw new EntityNotFoundException("Trenutno ne postoje sale u bioskopu sa ID-jem " + bioskopId);
			} else {
				return sale;
			}
		}
		
		
	}
	
	@Override 
	public Page<Sala> naprednaPretraga(Integer kapacitet, Integer brojRedova, Long bioskopId, Pageable pageable)
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
			if(!bService.existsById(bioskopId)) {
				throw new EntityNotFoundException("Bioskop sa ID-jem " + bioskopId + " ne postoji.");
			}
			
	        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bioskop").get("id"), bioskopId));
	    }
		
		Page<Sala> sale = repo.findAll(spec, pageable);
		
		if(sale.isEmpty()) {
			throw new EntityNotFoundException("Trenutno nema sala sa ovim kriterijumima");
		}
		
		return sale;
	}

}