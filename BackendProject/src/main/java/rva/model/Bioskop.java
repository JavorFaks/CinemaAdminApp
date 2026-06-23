package rva.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Bioskop {
	@Id
	@SequenceGenerator(name = "bioskop_seq", sequenceName = "bioskop_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bioskop_seq")
	private Long id;
	
	@NotBlank(message = "Naziv bioskopa je obavezan i ne sme biti prazan.")
	private String naziv;
	
	@NotBlank(message = "Adresa je obavezna")
	private String adresa;
	
	@OneToMany(mappedBy="bioskop", cascade = CascadeType.ALL)
	@JsonIgnore
	List<Sala> sale;
	
	public Bioskop() {}
	
	public Bioskop(Long id, String naziv, String adresa)
	{
		this.id = id;
		this.naziv = naziv;
		this.adresa = adresa;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
