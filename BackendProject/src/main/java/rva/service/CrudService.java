package rva.service;

import java.util.List;

public interface CrudService<T> {
	List<T>	getAll();
	boolean existsById(long id);
	T findById(long id);
	T create(T body);
	T update (T body, long id);
	void delete(long id);
}
