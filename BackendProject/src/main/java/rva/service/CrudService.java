package rva.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<T> {
	Page<T>	getAll(Pageable pageable);
	boolean existsById(long id);
	T findById(long id);
	T create(T body);
	T update (T body, long id);
	void delete(long id);
}
