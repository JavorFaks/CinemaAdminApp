package rva.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import rva.model.Film;

public interface FilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film>{
	
   
}
