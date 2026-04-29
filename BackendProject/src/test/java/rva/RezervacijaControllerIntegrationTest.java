package rva;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
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
		ResponseEntity<Rezervacija> response = template.getForEntity(apiUrl + "?id=" + id, Rezervacija.class);
		
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
	void createRezervacija() {
		Rezervacija rezervacija = new Rezervacija();
		rezervacija.setCenaKarte(400.22);
		rezervacija.setBrojOsoba(4);;
		
		HttpEntity<Rezervacija> entity = new HttpEntity<Rezervacija>(rezervacija);
		
		ResponseEntity<Rezervacija> response = template.exchange(apiUrl, HttpMethod.POST, entity, Rezervacija.class);
		
		assertEquals(201, response.getStatusCode().value());
		assertEquals(rezervacija.getCenaKarte(), response.getBody().getCenaKarte());
		assertEquals(rezervacija.getBrojOsoba(), response.getBody().getBrojOsoba());
		
	}
	
	@Test
	@Order(7)
	void updateRezervacija() {
		largestId = 19;
		Rezervacija rezervacija = new Rezervacija();
		rezervacija.setCenaKarte(500.00);
		rezervacija.setBrojOsoba(5);;
		
		HttpEntity<Rezervacija> entity = new HttpEntity<Rezervacija>(rezervacija);
		
		ResponseEntity<Rezervacija> response = template.exchange(apiUrl+"/"+largestId, 
				HttpMethod.PUT, entity, Rezervacija.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(rezervacija.getCenaKarte(), response.getBody().getCenaKarte());
		assertEquals(rezervacija.getBrojOsoba(), response.getBody().getBrojOsoba());
	}
	
	@Test
	@Order(8)
	void deleteRezervacija() {
		largestId = 19;
		ResponseEntity<?> response = template.exchange(apiUrl + "/" + largestId, HttpMethod.DELETE, null, Object.class);
		
		assertEquals(204, response.getStatusCode().value());
		assertNull(response.getBody());
	}
	
	@Test
	@Order(9)
	void getNeplaceneRezervacije()
	{
		ResponseEntity<List<Rezervacija>> response = template.exchange(apiUrl + "/neplacene", 
				HttpMethod.GET, null, 
				new ParameterizedTypeReference<List<Rezervacija>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		
		for(Rezervacija r : response.getBody()) {
			assertFalse(r.isPlaceno()); 
		}
	}
	
	@Test
	@Order(10)
	void getRezervacijeZaDatum() {
	    String testDatum = "2024-03-26"; 
	    
	    try {
	        ResponseEntity<List<Rezervacija>> response = template.exchange(apiUrl + "/datum?datum=" + testDatum, 
	                HttpMethod.GET, null, 
	                new ParameterizedTypeReference<List<Rezervacija>>() {});
	        
	        assertEquals(200, response.getStatusCode().value());
	        assertNotEquals(0, response.getBody().size());
	        
	       
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        for (Rezervacija r : response.getBody()) {
	            // Proveravamo da li je datum svake vraćene rezervacije zaista taj koji smo tražili
	            String datumIzBaze = sdf.format(r.getDatum());
	            assertEquals(testDatum, datumIzBaze);
	        }
	        
	    } catch (org.springframework.web.client.HttpClientErrorException e) {
	        assertEquals(404, e.getStatusCode().value());
	    }
	}
	
	@Test
	@Order(11)
	void getPredstojece() {
	    String testDatum = "2024-03-26"; 
	    Date targetDate = null;
	    
	    try {
	        targetDate = new SimpleDateFormat("yyyy-MM-dd").parse(testDatum);
	    } catch (ParseException e) {
	        Assertions.fail("Greška: Test datum nije u dobrom formatu!");
	        return;
	    }
	    
	    try {
	        ResponseEntity<List<Rezervacija>> response = template.exchange(apiUrl + "/predstojece?datum=" + testDatum, 
	                HttpMethod.GET, null, 
	                new ParameterizedTypeReference<List<Rezervacija>>() {});
	        
	        assertEquals(200, response.getStatusCode().value());
	        assertNotEquals(0, response.getBody().size());
	        
	       
	        
	        for (Rezervacija r : response.getBody()) {
	            assertTrue(r.getDatum().after(targetDate));
	        }
	        
	    } catch (org.springframework.web.client.HttpClientErrorException e) {
	        assertEquals(404, e.getStatusCode().value());
	    }
	}
	
	@Test
	@Order(12)
	void getIstoriju() {
	    String testDatum = "2024-03-29"; 
	    Date targetDate = null;
	    
	    try {
	        targetDate = new SimpleDateFormat("yyyy-MM-dd").parse(testDatum);
	    } catch (ParseException e) {
	        Assertions.fail("Greška: Test datum nije u dobrom formatu!");
	        return;
	    }
	    
	    try {
	        ResponseEntity<List<Rezervacija>> response = template.exchange(apiUrl + "/istorija?datum=" + testDatum, 
	                HttpMethod.GET, null, 
	                new ParameterizedTypeReference<List<Rezervacija>>() {});
	        
	        assertEquals(200, response.getStatusCode().value());
	        assertNotEquals(0, response.getBody().size());
	        
	       
	        
	        for (Rezervacija r : response.getBody()) {
	            assertTrue(r.getDatum().before(targetDate));
	        }
	        
	    } catch (org.springframework.web.client.HttpClientErrorException e) {
	        assertEquals(404, e.getStatusCode().value());
	    }
	}
}
