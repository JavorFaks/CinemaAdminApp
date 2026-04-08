package rva.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
	List<T>	getAll();
	boolean existsById(long id);
	Optional<T> findById(long id);
	T create(T body);
	Optional<T> update (T body, long id);
	void delete(long id);
}
