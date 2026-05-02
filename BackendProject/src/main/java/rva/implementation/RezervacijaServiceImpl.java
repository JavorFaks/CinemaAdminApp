package rva.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import rva.model.Rezervacija;
import rva.repository.RezervacijaRepository;
import rva.service.FilmService;
import rva.service.RezervacijaService;
import rva.service.SalaService;




@Component
public class RezervacijaServiceImpl implements RezervacijaService {
	
	@Autowired
	private RezervacijaRepository repo;
	
	@Autowired
	private FilmService filmService;
	
	@Autowired
	private SalaService salaService;

	@Override
	public List<Rezervacija> getAll() {
		List<Rezervacija> r = repo.findAll();
		
		if(r.isEmpty()) {
			throw new EntityNotFoundException("Trenutno nema nijedne rezervacije.");
		}
		
		return r;
	}

	@Override
	public boolean existsById(long id) {
		return repo.existsById(id);
	}

	@Override
	public Rezervacija findById(long id) {
		Optional<Rezervacija> r = repo.findById(id);
		
		if(r.isEmpty()) {
			throw new EntityNotFoundException("Rezervacija sa ID-jem " + id + " ne postoji.");
		}
		
		return r.get();
	}

	@Override
	public Rezervacija create(Rezervacija body) {
		return repo.save(body);
	}

	@Override
	public Rezervacija update(Rezervacija body, long id) {
		Long filmId = body.getFilm().getId();
		Long salaId = body.getSala().getId();
		
		if(repo.existsById(id)) {
			if(!filmService.existsById(filmId)) {
				throw new EntityNotFoundException("Film sa ID-jem " + filmId + " ne postoji.");
			}
			
			if(!salaService.existsById(salaId)) {
				throw new EntityNotFoundException("Sala sa ID-jem " + salaId + " ne postoji.");
			}
			
			body.setId(id);
			return repo.save(body);
			
		} else {
			throw new EntityNotFoundException("Rezervacija sa ID-jem " + id + " ne postoji.");
		}
		
	}

	@Override
	public void delete(long id) {
		if(existsById(id)) {
			repo.deleteById(id);
		} else {
			throw new EntityNotFoundException("Rezervacija sa ID-jem " + id + " ne postoji.");
		}
		
		
	}


	@Override
	public List<Rezervacija> getRezervacijeByFilm(Long filmId) {
		if(!filmService.existsById(filmId)) {
			throw new EntityNotFoundException("Film sa ID-jem " + filmId + " ne postoji.");
		}
		
		List<Rezervacija> r = repo.findByFilm_Id(filmId);
		
		if(r.isEmpty()) {
			throw new EntityNotFoundException("Trenutno nema rezervacija za film sa ID-jem " + filmId);
		}
		
		return r;
	}
	
	@Override
	public List<Rezervacija> getRezervacijeBySala(Long salaId) {
		if(!salaService.existsById(salaId)) {
			throw new EntityNotFoundException("Sala sa ID-jem " + salaId + " ne postoji.");
		}
		
		List<Rezervacija> r = repo.findBySala_Id(salaId);
		
		if(r.isEmpty()) {
			throw new EntityNotFoundException("Trenutno nema rezervacija za salu sa ID-jem " + salaId);
		}
		
		return r;
	}
	
	
	@Override
	public List<Rezervacija> naprednaPretraga(Long filmId, Long salaId, Boolean placeno, LocalDate datumOd, LocalDate datumDo)
	{
		//Validacija stranih kljuceva
		if (filmId != null && !filmService.existsById(filmId)) {
            throw new EntityNotFoundException("Film sa ID-jem: " + filmId + " ne postoji.");
        }
        
        if (salaId != null && !salaService.existsById(salaId)) {
            throw new EntityNotFoundException("Sala sa ID-jem: " + salaId + " ne postoji.");
        }
        
        
        Specification<Rezervacija> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction(); //prazno
        
        
        if(filmId!=null)
		{
			spec = spec.and((root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("film").get("id"), filmId)
            );
		}
		
		if(salaId!=null)
		{
			spec = spec.and((root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("sala").get("id"), salaId)
            );
		}
		
		if (placeno != null) 
		{
	        spec = spec.and((root, query, criteriaBuilder) -> 
	        criteriaBuilder.equal(root.get("placeno"), placeno));
	    }
		
		if (datumOd != null) 
		{
	        spec = spec.and((root, query, criteriaBuilder) -> 
	        criteriaBuilder.greaterThanOrEqualTo(root.get("datum"), datumOd));
	    }
		
		if (datumDo != null) 
		{
	        spec = spec.and((root, query, criteriaBuilder) -> 
	        criteriaBuilder.lessThanOrEqualTo(root.get("datum"), datumDo));
	    }
		
		List<Rezervacija> r = repo.findAll(spec);
		
		if(r.isEmpty()) {
			throw new EntityNotFoundException("Nema rezervacija sa ovim kriterijumima.");
		}
		
		return r;
	}

}