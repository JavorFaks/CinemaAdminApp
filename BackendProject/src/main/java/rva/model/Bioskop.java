package rva.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Bioskop {
	@Id
	@SequenceGenerator(name = "bioskop_seq", sequenceName = "bioskop_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bioskop_seq")
	private long id;
	private String naziv;
	private String adresa;
	
	public Bioskop(long id, String naziv, String adresa)
	{
		this.id = id;
		this.naziv = naziv;
		this.adresa = adresa;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
}
