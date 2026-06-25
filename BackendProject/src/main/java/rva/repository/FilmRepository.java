package rva.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import rva.model.Film;

public interface FilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film>{
	
	@Query("SELECT DISTINCT f.zanr FROM Film f ORDER BY f.zanr")
	List<String> findDistinctZanrovi();
}
