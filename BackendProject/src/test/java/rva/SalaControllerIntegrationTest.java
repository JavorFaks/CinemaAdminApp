package rva;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import rva.model.Bioskop;
import rva.model.Sala;
import rva.service.BioskopService;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
//Svaka test klasa dobija novi kontekst, a time i svjeze napunjenu bazu iz data.sql
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SalaControllerIntegrationTest {
	
	@Autowired
	private BioskopService bService;
	
	RestTemplate template = new RestTemplate();
	
	@LocalServerPort
	private int port;
	
	String apiUrl;
	
	@BeforeEach
	void setUp() {
		apiUrl = "http://localhost:" + port + "/sala";
	}
	
	long largestId = 0;
	
	@Test
	@Order(1)
	void getAllSale() {
		ResponseEntity<PageResponse<Sala>> response = template.exchange(apiUrl, HttpMethod.GET, null, 
				new ParameterizedTypeReference<PageResponse<Sala>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotEquals(0, response.getBody().getContent().size());
	}
	
	@Test
	@Order(2)
	void getSalaById() {
		long id = 1;
		ResponseEntity<Sala> response = template.getForEntity(apiUrl + "/" + id, Sala.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(id, response.getBody().getId());
	}
	
	/*Get by bioskop*/
	@Test
	@Order(3)
	void getSalaByBioskop_ValidanId() {
		long bioskopId = 1;
		
		String url = apiUrl + "/bioskop?bioskopId=" + bioskopId;
		
		ResponseEntity<PageResponse<Sala>> response = template.exchange(url, 
				HttpMethod.GET, null, 
				new ParameterizedTypeReference<PageResponse<Sala>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertNotEquals(0, response.getBody().getContent().size());
		
		// Prolazimo kroz sve vraćene sale i proveravamo da li zaista pripadaju tom bioskopu
		for(Sala s : response.getBody().getContent()) {
			assertEquals(bioskopId, s.getBioskop().getId());
		}
	}

	@Test
	@Order(4)
	void getSalaByBioskop_NepostojeciBioskop() {
		long nepostojeciId = 999;
		String url = apiUrl + "/bioskop?bioskopId=" + nepostojeciId;
		
		try {
			template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<Sala>>() {});
			
			fail("Ocekivano je 404 Not Found jer bioskop ne postoji.");
			
		} catch (HttpClientErrorException e) {
			assertEquals(404, e.getStatusCode().value());
		}
	}
	
	
	//Napredna pretraga
	@Test
	@Order(5)
	void pretragaSala_SamoPoKapacitetu() {
		int trazeniKapacitet = 150; 
		
		String url = apiUrl + "/pretraga?kapacitet=" + trazeniKapacitet;
		
		ResponseEntity<PageResponse<Sala>> response = template.exchange(url, 
				HttpMethod.GET, null, 
				new ParameterizedTypeReference<PageResponse<Sala>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		
		// Proveravamo da li svaka vraćena sala zaista ima taj kapacitet
		for(Sala s : response.getBody().getContent()) {
			assertTrue(trazeniKapacitet <= s.getKapacitet());
			
		}
	}
	
	@Test
	@Order(6)
	void pretragaSala_KombinovanoKapacitetIRedovi() {
		// Opet, unesi vrednosti koje odgovaraju podacima u bazi
		int trazeniKapacitet = 120;
		int trazeniBrojRedova = 10;
		long bioskopId = 1;
		
		
		String url = apiUrl + "/pretraga?kapacitet=" + trazeniKapacitet + "&brojRedova=" + trazeniBrojRedova + "&bioskopId=" + bioskopId;
		
		ResponseEntity<PageResponse<Sala>> response = template.exchange(url, 
				HttpMethod.GET, null, 
				new ParameterizedTypeReference<PageResponse<Sala>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		
		// Stroga provera: da li rezultati ispunjavaju SVE uslove
		for(Sala s : response.getBody().getContent()) {
			assertTrue(trazeniKapacitet <= s.getKapacitet());
			assertTrue(trazeniBrojRedova <= s.getBrojRedova());
			assertEquals(bioskopId, s.getBioskop().getId());
		}
	}
	
	
	//////////////
	//Unos podataka
	//////////////
	@Test
	@Order(7)
	void createSala_ValidInput() {
		Sala sala = new Sala();
		sala.setKapacitet(100);
		sala.setBrojRedova(10);
		
		// Povezujemo salu sa postojecim bioskopom da baza ne bi bacila 500 gresku
		Bioskop bioskop = bService.findById(1);
		sala.setBioskop(bioskop);

		HttpEntity<Sala> entity = new HttpEntity<>(sala);
		
		ResponseEntity<Sala> response = template.exchange(apiUrl, HttpMethod.POST, entity, Sala.class);
		
		// Ocekujemo 201 Created
		assertEquals(201, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(100, response.getBody().getKapacitet());
		assertEquals(10, response.getBody().getBrojRedova());
	}
	
	@Test
	@Order(8)
	void createSala_InvalidInput() {
		Sala neispravnaSala = new Sala();
		neispravnaSala.setKapacitet(50); 
		
		//Poslana je sala bez bioskopa, a bioskop mora biti unesen
		HttpEntity<Sala> entity = new HttpEntity<>(neispravnaSala);
		
		try {
			template.exchange(apiUrl, HttpMethod.POST, entity, Sala.class);
			
			fail("Ocekivano je 400 Bad Request zbog validacije (@Valid).");
			
		} catch (HttpClientErrorException e) {
			assertEquals(400, e.getStatusCode().value());
		}
	}
	
	@Test
	@Order(9)
	void updateSala_ValidInput() {
		long id = 1;
		
		Sala salaZaIzmenu = new Sala();
		salaZaIzmenu.setKapacitet(150); // Menjamo kapacitet
		salaZaIzmenu.setBrojRedova(15); // Menjamo broj redova
		
		Bioskop bioskop = bService.findById(1); 
		salaZaIzmenu.setBioskop(bioskop);

		HttpEntity<Sala> entity = new HttpEntity<>(salaZaIzmenu);
		
		ResponseEntity<Sala> response = template.exchange(apiUrl + "/" + id, HttpMethod.PUT, entity, Sala.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(150, response.getBody().getKapacitet());
		assertEquals(15, response.getBody().getBrojRedova());
	}
	
	@Test
	@Order(10)
	void updateSala_NepostojeciId() {
		long nepostojeciId = 999;
		
		Sala sala = new Sala();
		sala.setKapacitet(100);
		sala.setBrojRedova(10);
		
		Bioskop bioskop = bService.findById(1); 
		sala.setBioskop(bioskop);
		
		HttpEntity<Sala> entity = new HttpEntity<>(sala);
		
		try {
			template.exchange(apiUrl + "/" + nepostojeciId, HttpMethod.PUT, entity, Sala.class);
			
			fail("Ocekivano je 404 Not Found, jer ta sala ne postoji.");
			
		} catch (HttpClientErrorException e) {
			assertEquals(404, e.getStatusCode().value());
		}
	}
	
	@Test
	@Order(11)
	void deleteSala_ValidId() {
		long idZaBrisanje = 2; 
		
		ResponseEntity<Void> response = template.exchange(apiUrl + "/" + idZaBrisanje, HttpMethod.DELETE, null, Void.class);
		
		assertEquals(204, response.getStatusCode().value());
	}
	
	@Test
	@Order(12)
	void deleteSala_NepostojeciId() {
		long nepostojeciId = 999;
		
		try {
			template.exchange(apiUrl + "/" + nepostojeciId, HttpMethod.DELETE, null, Void.class);
			
			fail("Ocekivano je 404 Not Found, jer ne mozemo obrisati salu koja ne postoji.");

		} catch (HttpClientErrorException e) {
			assertEquals(404, e.getStatusCode().value());
		}
	}

	@Test
	@Order(13)
	void updateSala_BioskopBezId_Returns400() {
		Sala sala = new Sala();
		sala.setKapacitet(100);
		sala.setBrojRedova(10);

		// Bioskop bez ID-ja ranije je izazivao NullPointerException i 500
		sala.setBioskop(new Bioskop());

		HttpEntity<Sala> entity = new HttpEntity<>(sala);

		try {
			template.exchange(apiUrl + "/1", HttpMethod.PUT, entity, String.class);

			fail("Ocekivano je 400 Bad Request jer bioskop nema ID.");

		} catch (HttpClientErrorException e) {
			assertEquals(400, e.getStatusCode().value());
			assertTrue(e.getResponseBodyAsString().contains("Morate poslati ID bioskopa"));
		}
	}
}
