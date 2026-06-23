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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
public class Film {
	@Id
	@SequenceGenerator(name = "film_seq", sequenceName = "film_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "film_seq")
	private Long id;
	
	@NotBlank(message = "Naziv filma je obavezan i ne sme biti prazan.")
	private String naziv;
	
	private Integer recenzija;
	
	@NotNull(message = "Trajanje filma je obavezno.")
    @Min(value = 1, message = "Trajanje filma mora biti veće od 0 minuta.")
	private Integer trajanje;
	
	@NotBlank(message = "Zanr filma je obavezan.")
	private String zanr;
	
	@OneToMany(mappedBy="film", cascade = CascadeType.ALL)
	@JsonIgnore
	List<Rezervacija> rezervacije;
	
	public Film() {}
	
	public Film(Long id, String naziv, Integer recenzija, String zanr) {
		this.id = id;
		this.naziv = naziv;
		this.recenzija = recenzija;
		this.zanr = zanr;
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

	public Integer getRecenzija() {
		return recenzija;
	}

	public void setRecenzija(Integer recenzija) {
		this.recenzija = recenzija;
	}

	public Integer getTrajanje() {
		return trajanje;
	}

	public void setTrajanje(Integer trajanje) {
		this.trajanje = trajanje;
	}

	public String getZanr() {
		return zanr;
	}

	public void setZanr(String zanr) {
		this.zanr = zanr;
	}
}
