package rva.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Sala {
	@Id
	@SequenceGenerator(name = "sala_seq", sequenceName = "sala_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sala_seq")
	private long id;
	private int kapacitet;
	private int brojRedova;
	
	@ManyToOne
	@JoinColumn(name = "id_bioskop")
	private Bioskop bioskop;
	
	@OneToMany(mappedBy="sala", cascade = CascadeType.ALL)
	@JsonIgnore
	List<Rezervacija> rezervacije;
	
	public Sala() {}
	
	public Sala(long id, int kapacitet, int brojRedova, Bioskop bioskop) {
		
		this.id = id;
		this.kapacitet = kapacitet;
		this.brojRedova = brojRedova;
		this.bioskop = bioskop;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getKapacitet() {
		return kapacitet;
	}

	public void setKapacitet(int kapacitet) {
		this.kapacitet = kapacitet;
	}

	public int getBrojRedova() {
		return brojRedova;
	}

	public void setBrojRedova(int brojRedova) {
		this.brojRedova = brojRedova;
	}

	public Bioskop getBioskop() {
		return bioskop;
	}

	public void setBioskop(Bioskop bioskop) {
		this.bioskop = bioskop;
	}
}
