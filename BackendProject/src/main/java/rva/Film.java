package rva;

public class Film {
	private long id;
	private String naziv;
	private int recenzija;
	private int trajanje;
	private String zanr;
	
	public Film(long id, String naziv, int recenzija, String zanr) {
		this.id = id;
		this.naziv = naziv;
		this.recenzija = recenzija;
		this.zanr = zanr;
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

	public int getRecenzija() {
		return recenzija;
	}

	public void setRecenzija(int recenzija) {
		this.recenzija = recenzija;
	}

	public int getTrajanje() {
		return trajanje;
	}

	public void setTrajanje(int trajanje) {
		this.trajanje = trajanje;
	}

	public String getZanr() {
		return zanr;
	}

	public void setZanr(String zanr) {
		this.zanr = zanr;
	}
}
