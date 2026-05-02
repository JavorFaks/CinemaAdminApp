package rva.model;



import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;

@Entity
public class Rezervacija {
	@Id
	@SequenceGenerator(name = "rezervacija_seq", sequenceName = "rezervacija_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rezervacija_seq")
	private long id;
	private Integer brojOsoba;
	
	
	private Double cenaKarte;
	
	private Date datum;
	private boolean placeno;
	
	@NotNull(message="Film je obavezan")
	@ManyToOne
	@JoinColumn(name = "id_film")
	private Film film;

	@NotNull(message="Sala je obavezna")
	@ManyToOne
	@JoinColumn(name = "id_sala")
	private Sala sala;
	
	public Rezervacija() {}
	
	public Rezervacija(long id, Integer brojOsoba, Double cenaKarte, Date datum, boolean placeno, Film film, Sala sala) {

		this.id = id;
		this.brojOsoba = brojOsoba;
		this.cenaKarte = cenaKarte;
		this.datum = datum;
		this.placeno = placeno;
		this.film = film;
		this.sala = sala;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getBrojOsoba() {
		return brojOsoba;
	}

	public void setBrojOsoba(Integer brojOsoba) {
		this.brojOsoba = brojOsoba;
	}

	public Double getCenaKarte() {
		return cenaKarte;
	}

	public void setCenaKarte(Double cenaKarte) {
		this.cenaKarte = cenaKarte;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public boolean isPlaceno() {
		return placeno;
	}

	public void setPlaceno(boolean placeno) {
		this.placeno = placeno;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}
}
