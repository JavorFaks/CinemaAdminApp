package rva.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import rva.model.Film;
import rva.repository.FilmRepository;
import rva.service.FilmService;

@Component
public class FilmServiceImpl implements FilmService{
	
	@Autowired 
	private FilmRepository repo;
	
	@Override
	public Page<Film> getAll(Pageable pageable) {
		Page<Film> filmovi = repo.findAll(pageable);

		return filmovi;
	}

	@Override
	public boolean existsById(long id) {
		return repo.existsById(id);
	}

	@Override
	public Film findById(long id) {
		Optional<Film> film = repo.findById(id);
		
		if(film.isEmpty()) {
			throw new EntityNotFoundException("Film sa ID-jem: " + id + " ne postoji.");
		}
		
		return film.get();
	}

	@Override
	public Film create(Film body) {
		return repo.save(body);
	}

	@Override
	public Film update(Film body, long id) {
		
		if(existsById(id)) {
			body.setId(id);
			return repo.save(body);
		} else {
			throw new EntityNotFoundException("Film sa ID-jem: " + id + " ne postoji."); 
		} 
		
	}

	@Override
	public void delete(long id) {
		if(existsById(id)) {
			repo.deleteById(id);
		} else {
			throw new EntityNotFoundException("Film sa ID-jem: " + id + " ne postoji."); 
		}
	
	}

	@Override
	public Page<Film> naprednaPretraga(String naziv, String zanr, Integer maxMinuta, Pageable pageable) {
		
		Specification<Film> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction(); //Prazno
		
		
		//Filter po nazivu
		if (naziv != null && !naziv.trim().isEmpty()) {
			//dodaje se and uslov
			//root tabela
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.like(criteriaBuilder.lower(root.get("naziv")), "%" + naziv.toLowerCase() + "%") //uporedi dva naziv iz tabele i uneseni
            );
        }
		
		//Dodaje se filter po zanru
		if(zanr != null && !zanr.trim().isEmpty()) {
			spec = spec.and((root, query, criteriaBuilder) ->
				criteriaBuilder.equal(criteriaBuilder.lower(root.get("zanr")), zanr.toLowerCase())
			);
		}
		
		
		//Dodaje se filter po trajanju
		if(maxMinuta != null) {
			spec = spec.and((root, query, criteriaBuilder) ->
				criteriaBuilder.lessThanOrEqualTo(root.get("trajanje"), maxMinuta)
			);
		}
		
		Page<Film> filmovi = repo.findAll(spec, pageable);
		
		
		if(filmovi.isEmpty()) {
			throw new EntityNotFoundException("Nema filmova koji ispunjavaju ove kriterijume.");
		}
		
		return filmovi;
	
	}
	

}
