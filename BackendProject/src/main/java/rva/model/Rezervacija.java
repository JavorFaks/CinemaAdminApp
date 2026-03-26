package rva.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Rezervacija {
	@Id
	@SequenceGenerator(name = "rezervacija_seq", sequenceName = "rezervacija_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rezervacija_seq")
	private long id;
	private int brojOsoba;
	private double cenaKarte;
	private Date datum;
	private boolean placeno;
	
	@ManyToOne
	@JoinColumn(name = "id_film")
	private Film film;

	@ManyToOne
	@JoinColumn(name = "id_sala")
	private Sala sala;
	
	public Rezervacija(long id, int brojOsoba, int cenaKarte, Date datum, boolean placeno, Film film, Sala sala) {

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

	public int getBrojOsoba() {
		return brojOsoba;
	}

	public void setBrojOsoba(int brojOsoba) {
		this.brojOsoba = brojOsoba;
	}

	public double getCenaKarte() {
		return cenaKarte;
	}

	public void setCenaKarte(double cenaKarte) {
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

	public void setIdSala(Sala sala) {
		this.sala = sala;
	}
}
