package rva;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import rva.model.Film;
import rva.model.Rezervacija;
import rva.model.Sala;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RezervacijaControllerIntegrationTest {
	
	RestTemplate template = new RestTemplate();
	String apiUrl = "http://localhost:8080/rezervacija";
	long largestId = 0;	
	
	@Test
	@Order(1)
	void getAllRezervacije() {
		ResponseEntity<List<Rezervacija>> response = template.exchange(apiUrl, HttpMethod.GET, null, 
				new ParameterizedTypeReference<List<Rezervacija>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotEquals(0, response.getBody().size());
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
		
		ResponseEntity<List<Rezervacija>> response = template.exchange(apiUrl + "/film?filmId=" + filmId, 
				HttpMethod.GET, null, 
				new ParameterizedTypeReference<List<Rezervacija>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotEquals(0, response.getBody().size());
		
		for(Rezervacija r : response.getBody()) {
			assertEquals(filmId, r.getFilm().getId());
		}
	}
	
	@Test
	@Order(5)
	void getRezervacijaBySala()
	{
		int salaId = 1;
		
		ResponseEntity<List<Rezervacija>> response = template.exchange(apiUrl + "/sala?salaId=" + salaId, 
				HttpMethod.GET, null, 
				new ParameterizedTypeReference<List<Rezervacija>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotEquals(0, response.getBody().size());
		
		for(Rezervacija r : response.getBody()) {
			assertEquals(salaId, r.getSala().getId());
		}
	}
	
	
	@Test
	@Order(6)
	void createRezervacija_ValidInput() {
	    Rezervacija rezervacija = new Rezervacija();
	    rezervacija.setCenaKarte(400.22);
	    rezervacija.setBrojOsoba(4);
	    
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
	    
	    ResponseEntity<List<Rezervacija>> response = template.exchange(url, 
	            HttpMethod.GET, null, 
	            new ParameterizedTypeReference<List<Rezervacija>>() {});
	    
	    assertEquals(200, response.getStatusCode().value());
	    assertNotNull(response.getBody());
	    
	    for(Rezervacija r : response.getBody()) {
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
	    
	    ResponseEntity<List<Rezervacija>> response = template.exchange(url, 
	            HttpMethod.GET, null, 
	            new ParameterizedTypeReference<List<Rezervacija>>() {});
	    
	    assertEquals(200, response.getStatusCode().value());
	    assertNotNull(response.getBody());
	    
	    // Proveravamo da li su rezultati strogo filtrirani i po filmu i po sali
	    for(Rezervacija r : response.getBody()) {
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
	        template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Rezervacija>>() {});
	        
	        fail("Ocekivano je 404 Not Found, jer film ne postoji.");
	        
	    } catch (HttpClientErrorException e) {
	        
	        assertEquals(404, e.getStatusCode().value());
	    }
	}
}
