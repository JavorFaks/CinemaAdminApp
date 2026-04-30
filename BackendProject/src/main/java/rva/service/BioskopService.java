package rva.service;

import java.util.List;

import org.springframework.stereotype.Service;

import rva.model.Bioskop;

@Service
public interface BioskopService extends CrudService<Bioskop> {
	List<Bioskop> getBioskopPoGradu(String grad);
}
