package rva;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import rva.model.Film;
import rva.model.Rezervacija;
import rva.model.Sala;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
//Svaka test klasa dobija novi kontekst, a time i svjeze napunjenu bazu iz data.sql
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RezervacijaControllerIntegrationTest {
	
	RestTemplate template = new RestTemplate();
	
	@LocalServerPort
	private int port;
	
	String apiUrl;
	
	@BeforeEach
	void setUp() {
		apiUrl = "http://localhost:" + port + "/rezervacija";
	}
	
	long largestId = 0;	
	
	@Test
	@Order(1)
	void getAllRezervacije() {
		ResponseEntity<PageResponse<Rezervacija>> response = template.exchange(apiUrl, HttpMethod.GET, null, 
				new ParameterizedTypeReference<PageResponse<Rezervacija>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotEquals(0, response.getBody().getContent().size());
	}
	
	@Test
	@Order(2)
	void getRezervacijaById() {
		long id = 1;
		ResponseEntity<Rezervacija> response = template.getForEntity(apiUrl + "/" + id, Rezervacija.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(id, response.getBody().getId());
	}
	
	
	@Test
	@Order(4)
	void getRezervacijaByFilm()
	{
		int filmId = 1;
		
		ResponseEntity<PageResponse<Rezervacija>> response = template.exchange(apiUrl + "/film?filmId=" + filmId, 
				HttpMethod.GET, null, 
				new ParameterizedTypeReference<PageResponse<Rezervacija>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotEquals(0, response.getBody().getContent().size());
		
		for(Rezervacija r : response.getBody().getContent()) {
			assertEquals(filmId, r.getFilm().getId());
		}
	}
	
	@Test
	@Order(5)
	void getRezervacijaBySala()
	{
		int salaId = 1;
		
		ResponseEntity<PageResponse<Rezervacija>> response = template.exchange(apiUrl + "/sala?salaId=" + salaId, 
				HttpMethod.GET, null, 
				new ParameterizedTypeReference<PageResponse<Rezervacija>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotEquals(0, response.getBody().getContent().size());
		
		for(Rezervacija r : response.getBody().getContent()) {
			assertEquals(salaId, r.getSala().getId());
		}
	}
	
	
	@Test
	@Order(6)
	void createRezervacija_ValidInput() {
	    Rezervacija rezervacija = new Rezervacija();
	    rezervacija.setCenaKarte(400.22);
	    rezervacija.setBrojOsoba(4);
	    rezervacija.setDatum(LocalDate.of(2026, 6, 1));
	    
	    // Moramo postaviti i relacije da baza ne bi bacila 500 grešku
	    Film film = new Film();
	    Long id = 1L;
	    film.setId(id); 
	    rezervacija.setFilm(film);
	    
	    Sala sala = new Sala();
	    sala.setId(id); 
	    rezervacija.setSala(sala);

	    HttpEntity<Rezervacija> entity = new HttpEntity<>(rezervacija);
	    
	    ResponseEntity<Rezervacija> response = template.exchange(apiUrl, HttpMethod.POST, entity, Rezervacija.class);
	    
	    assertEquals(201, response.getStatusCode().value());
	    assertNotNull(response.getBody());
	    assertEquals(400.22, response.getBody().getCenaKarte());
	    assertEquals(4, response.getBody().getBrojOsoba());
	}
	
	@Test
	@Order(7)
	void createRezervacija_InvalidInput() {
	    Rezervacija neispravnaRezervacija = new Rezervacija();
	    neispravnaRezervacija.setCenaKarte(-500.0);
	    
	    HttpEntity<Rezervacija> entity = new HttpEntity<>(neispravnaRezervacija);
	    
	    try {
	     
	        template.exchange(apiUrl, HttpMethod.POST, entity, Rezervacija.class);
	        
	        fail("Ocekivano je 400 bad request.");
	        
	    } catch (HttpClientErrorException e) {
	        assertEquals(400, e.getStatusCode().value());
	    }
	}
	
	@Test
	@Order(8)
	void updateRezervacija() {
	    ResponseEntity<Rezervacija> getResponse = template.getForEntity(apiUrl + "/1", Rezervacija.class);
	    Rezervacija postojecaRezervacija = getResponse.getBody();
	    
	    assertNotNull(postojecaRezervacija);

	    postojecaRezervacija.setCenaKarte(500.00);
	    postojecaRezervacija.setBrojOsoba(5);
	    
	    HttpEntity<Rezervacija> entity = new HttpEntity<>(postojecaRezervacija);
	    
	  
	    ResponseEntity<Rezervacija> response = template.exchange(
	            apiUrl + "/" + postojecaRezervacija.getId(), 
	            HttpMethod.PUT, 
	            entity, 
	            Rezervacija.class);
	    
	    assertEquals(200, response.getStatusCode().value());
	    assertEquals(500.00, response.getBody().getCenaKarte());
	    assertEquals(5, response.getBody().getBrojOsoba());
	}
	
	@Test
	@Order(9)
	void deleteRezervacija() {
		largestId = 19;
		ResponseEntity<?> response = template.exchange(apiUrl + "/" + largestId, HttpMethod.DELETE, null, Object.class);
		
		assertEquals(204, response.getStatusCode().value());
		assertNull(response.getBody());
	}
	
	@Test
	@Order(10)
	void pretragaRezervacija_SamoPoFilmu() {
	    long filmId = 1;
	    
	    String url = apiUrl + "/pretraga?filmId=" + filmId;
	    
	    ResponseEntity<PageResponse<Rezervacija>> response = template.exchange(url, 
	            HttpMethod.GET, null, 
	            new ParameterizedTypeReference<PageResponse<Rezervacija>>() {});
	    
	    assertEquals(200, response.getStatusCode().value());
	    assertNotNull(response.getBody());
	    
	    for(Rezervacija r : response.getBody().getContent()) {
	        assertEquals(filmId, r.getFilm().getId());
	    }
	}
	
	@Test
	@Order(11)
	void pretragaRezervacija_KombinovanoFilmISalaIDatum() {
	    long filmId = 1;
	    long salaId = 1;
	    String datumOd = "2023-01-01"; 
	    

	    String url = apiUrl + "/pretraga?filmId=" + filmId + "&salaId=" + salaId + "&datumOd=" + datumOd;
	    
	    ResponseEntity<PageResponse<Rezervacija>> response = template.exchange(url, 
	            HttpMethod.GET, null, 
	            new ParameterizedTypeReference<PageResponse<Rezervacija>>() {});
	    
	    assertEquals(200, response.getStatusCode().value());
	    assertNotNull(response.getBody());
	    
	    // Proveravamo da li su rezultati strogo filtrirani i po filmu i po sali
	    for(Rezervacija r : response.getBody().getContent()) {
	        assertEquals(filmId, r.getFilm().getId());
	        assertEquals(salaId, r.getSala().getId());
	    }
	}

	@Test
	@Order(12)
	void pretragaRezervacija_NepostojeciFilmBacaGresku() {
	    long nepostojeciFilmId = 999;
	    String url = apiUrl + "/pretraga?filmId=" + nepostojeciFilmId;
	    
	    try {
	        template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<Rezervacija>>() {});
	        
	        fail("Ocekivano je 404 Not Found, jer film ne postoji.");
	        
	    } catch (HttpClientErrorException e) {
	        
	        assertEquals(404, e.getStatusCode().value());
	    }
	}

	@Test
	@Order(13)
	void updateRezervacija_FilmBezId_Returns400() {
	    ResponseEntity<Rezervacija> getResponse = template.getForEntity(apiUrl + "/1", Rezervacija.class);
	    Rezervacija rezervacija = getResponse.getBody();
	    assertNotNull(rezervacija);
	    
	    // Film bez ID-ja ranije je izazivao NullPointerException i 500
	    rezervacija.setFilm(new Film());
	    
	    HttpEntity<Rezervacija> entity = new HttpEntity<>(rezervacija);
	    
	    try {
	        template.exchange(apiUrl + "/1", HttpMethod.PUT, entity, String.class);
	        
	        fail("Ocekivano je 400 Bad Request jer film nema ID.");
	        
	    } catch (HttpClientErrorException e) {
	        assertEquals(400, e.getStatusCode().value());
	        assertTrue(e.getResponseBodyAsString().contains("Morate poslati ID filma"));
	    }
	}
}
