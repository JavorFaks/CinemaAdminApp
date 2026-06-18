package rva.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import rva.model.Bioskop;

public interface BioskopRepository extends JpaRepository<Bioskop, Long>{
	Page<Bioskop> findByAdresaContainingIgnoreCase(String grad, Pageable pageable);
}
