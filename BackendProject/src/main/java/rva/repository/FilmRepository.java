package rva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rva.model.Film;

public interface FilmRepository extends JpaRepository<Film, Long>{
	List<Film> findByNazivContainingIgnoreCase(String naziv);
}
