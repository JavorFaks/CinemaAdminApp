package rva.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
	public Page<Bioskop> getAll(Pageable pageable) {
		Page<Bioskop> bioskopi = repo.findAll(pageable);
		
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
	public Page<Bioskop> getBioskopPoGradu(String grad, Pageable pageable)
	{
		Page<Bioskop> bioskopi = repo.findByAdresaContainingIgnoreCase(grad, pageable);
		
		if(bioskopi.isEmpty()) {
			throw new EntityNotFoundException("Bioskop iz ovog grada ne postoje.");
		}
		
		return bioskopi;
	}
	
	@Override
	public Page<Bioskop> naprednaPretraga(String naziv, String adresa, Pageable page)
	{
		Specification<Bioskop> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction(); //Prazno
		
		
		//Filter po nazivu
		if (naziv != null && !naziv.trim().isEmpty()) {
			//dodaje se and uslov
			//root tabela
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(criteriaBuilder.lower(root.get("naziv")), "%" + naziv.toLowerCase() + "%") //uporedi dva naziv iz tabele i uneseni
            );
        }
		
		//Dodaje se filter po adresi
		if(adresa != null && !adresa.trim().isEmpty()) {
			spec = spec.and((root, query, criteriaBuilder) ->
				criteriaBuilder.like(criteriaBuilder.lower(root.get("adresa")), "%" + adresa.toLowerCase() + "%")
			);
		}
		
		
		Page<Bioskop> bioskopi = repo.findAll(spec, page);
		
		
		return bioskopi;
	}

}
