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
import org.springframework.web.client.RestTemplate;

import rva.model.Rezervacija;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RezervacijaControllerIntegrationTest {
	
	RestTemplate template = new RestTemplate();
	String apiUrl = "http://localhost:8080/rezervacija";
	int largestId = 0;
	
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
		ResponseEntity<Rezervacija> response = template.getForEntity(apiUrl + "?id=" + id, Rezervacija.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(id, response.getBody().getId());
	}
	
	@Test
	@Order(3)
	void getRezervacijaByCenaKarteLessThan()
	{
		double cenaKarte = 1000;
		
		ResponseEntity<List<Rezervacija>> response = template.exchange(apiUrl + "?cena=" + cenaKarte, 
				HttpMethod.GET, null, 
				new ParameterizedTypeReference<List<Rezervacija>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotEquals(0, response.getBody().size());
		
		for(Rezervacija r : response.getBody()) {
			assertTrue(r.getCenaKarte() < cenaKarte);
		}
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
	void createRezervacija() {
		Rezervacija rezervacija = new Rezervacija();
		rezervacija.setCenaKarte(400.22);
		rezervacija.setBrojOsoba(4);;
		
		HttpEntity<Rezervacija> entity = new HttpEntity<Rezervacija>(rezervacija);
		
		ResponseEntity<Rezervacija> response = template.exchange(apiUrl, HttpMethod.POST, entity, Rezervacija.class);
		
		assertEquals(201, response.getStatusCode().value());
		assertEquals(rezervacija.getCenaKarte(), response.getBody().getCenaKarte());
		assertEquals(rezervacija.getBrojOsoba(), response.getBody().getBrojOsoba());
		if(largestId<response.getBody().getId())
			largestId=(int) response.getBody().getId();
	}
	
	@Test
	@Order(7)
	void updateRezervacija() {
		Rezervacija rezervacija = new Rezervacija();
		rezervacija.setCenaKarte(400.22);
		rezervacija.setBrojOsoba(4);;
		
		HttpEntity<Rezervacija> entity = new HttpEntity<Rezervacija>(rezervacija);
		
		ResponseEntity<Rezervacija> response = template.exchange(apiUrl+"?id="+largestId, 
				HttpMethod.POST, entity, Rezervacija.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(rezervacija.getCenaKarte(), response.getBody().getCenaKarte());
		assertEquals(rezervacija.getBrojOsoba(), response.getBody().getBrojOsoba());
	}
	
	@Test
	@Order(8)
	void deleteRezervacija() {
		ResponseEntity<?> response = template.exchange(apiUrl + "?id=" + largestId, HttpMethod.DELETE);
		
		assertEquals(204, response.getStatusCode().value());
		assertNull(response.getBody());
	}
}
