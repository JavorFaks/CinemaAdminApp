package rva;

public class Sala {
	private long id;
	private int kapacitet;
	private int brojRedova;
	
	private Bioskop bioskop;
	
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
