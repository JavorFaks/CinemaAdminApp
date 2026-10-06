package rva;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import rva.model.Bioskop;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
//Svaka test klasa dobija novi kontekst, a time i svjeze napunjenu bazu iz data.sql
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BioskopControllerIntegrationTest {

	RestTemplate template = new RestTemplate();
	
	@LocalServerPort
	private int port;
	
	String apiUrl;
	
	@BeforeEach
	void setUp() {
		apiUrl = "http://localhost:" + port + "/bioskop";
	}
	
	long largestId = 0;	
	
	@Test
	@Order(1)
	void getAllBioskop() {
		ResponseEntity<PageResponse<Bioskop>> response = template.exchange(apiUrl, HttpMethod.GET, null, 
				new ParameterizedTypeReference<PageResponse<Bioskop>>() {});
		
		assertEquals(200, response.getStatusCode().value());
		assertNotEquals(0, response.getBody().getContent().size());
	}
	
	@Test
	@Order(2)
	void getBioskopById() {
		long id = 1;
		ResponseEntity<Bioskop> response = template.getForEntity(apiUrl + "/" + id, Bioskop.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(id, response.getBody().getId());
	}
	
	@Test
	@Order(3)
	void getBioskopPoGradu() {
		String trazeniGrad = "Beograd"; 
		
		try {
			ResponseEntity<PageResponse<Bioskop>> response = template.exchange(apiUrl + "/grad?grad=" + trazeniGrad, 
					HttpMethod.GET, 
					null, 
					new ParameterizedTypeReference<PageResponse<Bioskop>>() {});
			
			assertEquals(200, response.getStatusCode().value());
			
			assertNotNull(response.getBody());
			assertFalse(response.getBody().getContent().isEmpty());
			
			for (Bioskop b : response.getBody().getContent()) {
			
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
	void updateBioskop() {
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
	void deleteBioskop() {
		largestId = 6;
		ResponseEntity<?> response = template.exchange(apiUrl + "/" + largestId, HttpMethod.DELETE, null, Object.class);
		
		assertEquals(204, response.getStatusCode().value());
		assertNull(response.getBody());
	}
	
	@Test
    @Order(7)
    void testCreateBioskop_InvalidInput_Returns400() {
        // Pravimo neispravan objekat
        Bioskop losBioskop = new Bioskop();
        losBioskop.setNaziv(""); 
        losBioskop.setAdresa("Beograd");

        HttpEntity<Bioskop> entity = new HttpEntity<>(losBioskop);

        try {
            template.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            
            fail("Trebalo je da baci 400 Bad Request zbog praznog naziva!");
            
        } catch (HttpClientErrorException e) {
            
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
           
            assertTrue(e.getResponseBodyAsString().contains("obavezan"));
        }
    }

    @Test
    @Order(8)
    void testUpdateBioskop_InvalidId_Returns404() {
        Bioskop bioskop = new Bioskop();
        bioskop.setNaziv("Izmena");
        bioskop.setAdresa("Adresa");

        HttpEntity<Bioskop> entity = new HttpEntity<>(bioskop);
        long nepostojeciId = 9999;

        try {
            template.exchange(apiUrl + "/" + nepostojeciId, HttpMethod.PUT, entity, String.class);
            
            fail("Trebalo je da baci 404 Not Found!");
            
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("ne postoji"));
        }
    }

    @Test
    @Order(9)
    void testDeleteBioskop_InvalidId_Returns404() {
        long nepostojeciId = 9999;

        try {
            template.exchange(apiUrl + "/" + nepostojeciId, HttpMethod.DELETE, null, String.class);
            
            fail("Trebalo je da baci 404 Not Found pri brisanju!");
            
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

}
