package org.myproject.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.myproject.dto.GetAllSocksResponseDto;
import org.myproject.dto.SocksDto;
import org.myproject.entity.enums.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SocksControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api";
    }

    @Test
    public void testGetAllSocks() {
        ResponseEntity<Integer> response = restTemplate.getForEntity(baseUrl + "/socks/all", Integer.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testAddSocks() {
        SocksDto socksDto = new SocksDto();
        socksDto.setColor(Color.green);
        socksDto.setCottonPart(50);
        socksDto.setQuantity(20);

        ResponseEntity<Long> response = restTemplate.postForEntity(baseUrl + "/socks/income", socksDto, Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ResponseEntity<Integer> allSocksResponse = restTemplate.getForEntity(baseUrl + "/socks/all", Integer.class);
        assertTrue(allSocksResponse.getBody() > 0);
    }

    @Test
    public void testDeleteSocksById() {
        SocksDto socksDto = new SocksDto();
        socksDto.setColor(Color.purple);
        socksDto.setCottonPart(30);
        socksDto.setQuantity(15);

        ResponseEntity<Long> addResponse = restTemplate.postForEntity(baseUrl + "/socks/income", socksDto, Long.class);
        Long id = addResponse.getBody();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/socks/outcome/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<Integer> allSocksResponse = restTemplate.getForEntity(baseUrl + "/socks/all", Integer.class);
        assertTrue(allSocksResponse.getBody() >= 0);
    }

    @Test
    public void testGetSocksWithParams() {
        SocksDto socksDto = new SocksDto();
        socksDto.setColor(Color.orange);
        socksDto.setCottonPart(40);
        socksDto.setQuantity(10);

        restTemplate.postForEntity(baseUrl + "/socks/income", socksDto, Long.class);
        ResponseEntity<GetAllSocksResponseDto> response = restTemplate.exchange(
                baseUrl + "/socks?color=orange&operation=equal&cottonPart=40",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getSize());
    }
}