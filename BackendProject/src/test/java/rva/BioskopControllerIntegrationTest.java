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

import rva.model.Bioskop;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BioskopControllerIntegrationTest {

	RestTemplate template = new RestTemplate();
	String apiUrl = "http://localhost:8080/bioskop";
	long largestId = 0;	
	
	@Test
	@Order(1)
	void getAllBioskop() {
		ResponseEntity<List<Bioskop>> response = template.exchange(apiUrl, HttpMethod.GET, null, 
				new ParameterizedTypeReference<List<Bioskop>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotEquals(0, response.getBody().size());
	}
	
	@Test
	@Order(2)
	void getBioskopById() {
		long id = 1;
		ResponseEntity<Bioskop> response = template.getForEntity(apiUrl + "?id=" + id, Bioskop.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(id, response.getBody().getId());
	}
	
	@Test
	@Order(3)
	void getBioskopPoGradu() {
		String trazeniGrad = "Beograd"; 
		
		try {
			ResponseEntity<List<Bioskop>> response = template.exchange(apiUrl + "/grad?grad=" + trazeniGrad, 
					HttpMethod.GET, 
					null, 
					new ParameterizedTypeReference<List<Bioskop>>() {});
			
			assertEquals(200, response.getStatusCode().value());
			
			assertNotNull(response.getBody());
			assertFalse(response.getBody().isEmpty());
			
			for (Bioskop b : response.getBody()) {
			
				String adresaBioskopa = b.getAdresa().toLowerCase();
				assertTrue(adresaBioskopa.contains(trazeniGrad.toLowerCase()));
			}
			
		} catch (org.springframework.web.client.HttpClientErrorException e) {
			// Ako metoda u bazi ne nadje nista baca 404
			// To je savrseno ponasanje kontrolera
			assertEquals(404, e.getStatusCode().value());
		}
	}
	
	@Test
	@Order(4)
	void createBioskop() {
		Bioskop bioskop = new Bioskop();
		bioskop.setAdresa("Beograd");
		bioskop.setNaziv("NoviBioskop");
		
		HttpEntity<Bioskop> entity = new HttpEntity<Bioskop>(bioskop);
		
		ResponseEntity<Bioskop> response = template.exchange(apiUrl, HttpMethod.POST, entity, Bioskop.class);
		
		assertEquals(201, response.getStatusCode().value());
		assertEquals(bioskop.getAdresa(), response.getBody().getAdresa());
		assertEquals(bioskop.getNaziv(), response.getBody().getNaziv());
		
	}
	
	@Test
	@Order(5)
	void updateRezervacija() {
		largestId = 6;
		Bioskop bioskop = new Bioskop();
		bioskop.setAdresa("Beograd");
		bioskop.setNaziv("NoviBioskop");
		
		HttpEntity<Bioskop> entity = new HttpEntity<Bioskop>(bioskop);
		
		ResponseEntity<Bioskop> response = template.exchange(apiUrl+"/"+largestId, 
				HttpMethod.PUT, entity, Bioskop.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(bioskop.getAdresa(), response.getBody().getAdresa());
		assertEquals(bioskop.getNaziv(), response.getBody().getNaziv());
	}
	
	@Test
	@Order(6)
	void deleteRezervacija() {
		largestId = 6;
		ResponseEntity<?> response = template.exchange(apiUrl + "/" + largestId, HttpMethod.DELETE, null, Object.class);
		
		assertEquals(204, response.getStatusCode().value());
		assertNull(response.getBody());
	}

}
