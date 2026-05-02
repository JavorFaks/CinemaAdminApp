package rva.implementation;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<Rezervacija> getNeplaceneRezervacije()
	{
		List<Rezervacija> r = repo.findByPlacenoFalse();
		
		if(r.isEmpty()) {
			throw new EntityNotFoundException("Trenutno nema rezervacija koje nisu placene.");
		}
		
		return r;
	}
	
	@Override
	public List<Rezervacija> getRezervacijeZaDatum(Date datum)
	{
		
		List<Rezervacija> r = repo.findByDatum(datum);
		
		if(r.isEmpty()) {
			throw new EntityNotFoundException("Trenutno nema rezervacija za datum: " + datum.toString());
		}
		
		return r;
	}
	
	@Override
	public List<Rezervacija> getIstorijaRezervacija(Date datum)
	{
		List<Rezervacija> r = repo.findByDatumBefore(datum);
		
		if(r.isEmpty()) {
			throw new EntityNotFoundException("Nema istorije rezervacija prije datuma: " + datum.toString());
		}
		
		return r;
	}
	
	@Override
	public List<Rezervacija> getPredstojeceRezervacije(Date datum)
	{
		List<Rezervacija> r = repo.findByDatum(datum);
		
		if(r.isEmpty()) {
			throw new EntityNotFoundException("Nema rezervacija poslije datuma: " + datum.toString());
		}
		
		return r;
	}

}