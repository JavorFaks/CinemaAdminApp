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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import rva.model.Film;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmControllerIntegrationTest {
	
	RestTemplate template = new RestTemplate(); //test je da ne puca na 404
	String apiUrl = "http://localhost:8080/film";
	
	@Test
    @Order(1)
    void testGetAllFilmovi_Returns200OK() {
        ResponseEntity<List<Film>> response = template.exchange(
        		apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Film>>() {}
        );


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }
	
	@Test
    @Order(2)
    void testGetFilmById_ValidId_Returns200OK() {
        ResponseEntity<Film> response = template.getForEntity(apiUrl + "/1", Film.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    @Order(3)
    void testGetFilmById_InvalidId_Returns404NotFound() {
    	try {
            template.getForEntity(apiUrl + "/9999", String.class);
            
           
            fail("Test je pao: Ocekivana je 404 greska, ali je zahtev prosao (status 200).");
            
        } catch (HttpClientErrorException e) {
            // Ocekivano ponasanje, greska 404
            
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("ne postoji"));
        }
    }
    
    @Test
    @Order(4)
    void testNaprednaPretraga_PronadjenFilm_Returns200OK_WithList() {
        String url = apiUrl + "/pretraga?maxMinuta=200";

        ResponseEntity<List<Film>> response = template.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Film>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
        
        for (Film f : response.getBody()) {
            assertTrue(f.getTrajanje() <= 200);
        }
    }

    @Test
    @Order(5)
    void testNaprednaPretraga_NemaRezultata_Returns404NotFound() {
    	try {
    		String url = apiUrl + "/pretraga?zanr=NepostojeciZanr123";

            template.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            fail("Test je pao: Ocekivana je 404 greska, ali je zahtev prosao (status 200).");
    	}catch (HttpClientErrorException e) {
            // Ocekivano ponasanje, greska 404
            
    		assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("Nema filmova sa ovim kriterijumima!"));
        }
   }
        

   @Test
   @Order(6)
   void testCreateFilm_ValidInput_Return201()
   {
	   Film noviFilm = new Film();
	   noviFilm.setNaziv("DobriFilm");
	   noviFilm.setTrajanje(120);
	   noviFilm.setZanr("Drama");
	   
	   ResponseEntity<Film> response = template.postForEntity(apiUrl, noviFilm, Film.class);
	   
	   assertEquals(HttpStatus.CREATED,response.getStatusCode());
	   assertNotNull(response.getBody());
	   assertEquals("DobriFilm",response.getBody().getNaziv());
	   assertEquals("Drama",response.getBody().getZanr());
	   assertEquals(120,response.getBody().getTrajanje());
   }
	
   @Test
   @Order(7)
   void testCreateFilm_InvalidInput_Return400BadRequest()
   {
	   Film noviFilm = new Film();
	   noviFilm.setNaziv("");
	   noviFilm.setTrajanje(-23);
	   noviFilm.setZanr("");
	   
	   try {
	        template.postForEntity(apiUrl, noviFilm, String.class);
	        
	        
	        fail("Test pao: Očekivao sam 400 Bad Request, ali je validacija nekim cudom prošla.");
	        
	    } catch (HttpClientErrorException e) {
	        //Ciljano ponasanje
	        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
	        
	        assertTrue(e.getResponseBodyAsString().contains("obavezan")); 
	        
	    } catch (HttpServerErrorException e) {
	        // Nesto ne valja
	        fail("Test pao: Baza je pukla i dobili smo 500 umesto 400! Baci pogled u konzolu da vidiš printStackTrace().");
	    }


   }
   
   @Test
   @Order(8)
   void testUpdateFilm_ValidInput_Returns200OK() {
       // Pretpostavljamo da film sa ID 1 postoji
       Film izmena = new Film();
       izmena.setNaziv("Matriks Izmenjen");
       izmena.setZanr("Akcija");
       izmena.setTrajanje(150);
       izmena.setRecenzija(9);

      
       HttpEntity<Film> requestEntity = new HttpEntity<>(izmena);
       ResponseEntity<Film> response = template.exchange(apiUrl + "/1", HttpMethod.PUT, requestEntity, Film.class);

       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertNotNull(response.getBody());
       assertEquals("Matriks Izmenjen", response.getBody().getNaziv());
   }

   @Test
   @Order(9)
   void testUpdateFilm_InvalidId_Returns404NotFound() {
       Film izmena = new Film();
       izmena.setNaziv("Nepostojeci");
       izmena.setZanr("Nista");
       izmena.setTrajanje(100);

       try {
    	   HttpEntity<Film> requestEntity = new HttpEntity<>(izmena);
           
           template.exchange(apiUrl + "/9999", HttpMethod.PUT, requestEntity, String.class);
           fail("Test pao: ocekivano je 404 a ne 200");
       }catch(HttpClientErrorException e) {
    	   assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
           assertTrue(e.getResponseBodyAsString().contains("does not exist"));
       }
       

       
   }

   @Test
   @Order(10)
   void testUpdateFilm_InvalidInput_Returns400BadRequest() {
       // Testiramo da li @Valid radi i u PUT metodi
       Film losaIzmena = new Film();
       losaIzmena.setNaziv(""); // Prazno - krsi @NotBlank
       losaIzmena.setZanr("Drama");
       losaIzmena.setTrajanje(-5); // Krsi @Min(1)

       try {
    	   HttpEntity<Film> requestEntity = new HttpEntity<>(losaIzmena);
           template.exchange(apiUrl + "/1", HttpMethod.PUT, requestEntity, String.class);
           fail("Test pao: ocekivano je 404 a ne 200");
       }catch(HttpClientErrorException e) {
           assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
       }
             
       
   }
   
   @Test
   @Order(11)
   void testDeleteFilm_ValidId_Returns204NoContent() {
       ResponseEntity<Void> response = template.exchange(apiUrl + "/1", HttpMethod.DELETE, null, Void.class);

       assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
   }

   @Test
   @Order(12)
   void testDeleteFilm_InvalidId_Returns404NotFound() {
       // Brisanje necega sto ne postoji
	   try {
		   template.exchange(apiUrl + "/9999", HttpMethod.DELETE, null, String.class);
		   fail("Test pao: ocekivano je 404 a ne 200");
	   }catch(HttpClientErrorException e) {
		   assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
       }
       

      
   }
}
