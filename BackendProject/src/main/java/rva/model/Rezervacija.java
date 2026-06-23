package rva.model;



import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Rezervacija {
	@Id
	@SequenceGenerator(name = "rezervacija_seq", sequenceName = "rezervacija_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rezervacija_seq")
	private long id;
	
	@NotNull(message = "Broj osoba je obavezan.")
	@Min(value = 1, message = "Mora postojati bar jedna osoba u rezervaciji.")
	private Integer brojOsoba;
	
	@NotNull(message = "Cena karte je obavezna.")
	@PositiveOrZero(message = "Cena karte ne moze biti negativna.")
	private Double cenaKarte;
	
	@NotNull(message = "Datum rezervacije je obavezan.")
	private LocalDate datum; //LocalDate da se ne zamaramo vremenskim zonama
	private Boolean placeno;
	
	@NotNull(message="Film je obavezan")
	@ManyToOne
	@JoinColumn(name = "id_film")
	private Film film;

	@NotNull(message="Sala je obavezna")
	@ManyToOne
	@JoinColumn(name = "id_sala")
	private Sala sala;
	
	public Rezervacija() {}
	
	public Rezervacija(long id, Integer brojOsoba, Double cenaKarte, LocalDate datum, Boolean placeno, Film film, Sala sala) {

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

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	public Boolean isPlaceno() {
		return placeno;
	}

	public void setPlaceno(Boolean placeno) {
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
