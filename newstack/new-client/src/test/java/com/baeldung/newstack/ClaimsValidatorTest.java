package com.baeldung.newstack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.baeldung.newstack.web.model.Project;
import io.restassured.RestAssured;
import io.restassured.response.Response;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClaimsValidatorTest {
	@Autowired
	private WebTestClient webTestClient;
	
    private static final String CLIENT_ID = "newClient";
    private static final String CLIENT_SECRET = "newClientSecret";
    private static final String TOKEN_URL = "http://localhost:8083/auth/realms/baeldung/protocol/openid-connect/token";
    private static final String RESOURCE_URL = "http://localhost:8080/new-resource-server/api/projects";

    @Test
    public void invalidClaimTestCase() {
    	
    	final String accessToken = getAccessToken("mike@other.com", "pass");
        this.webTestClient.get()
                .uri(RESOURCE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void validClaimTestCase() {
    	
    	 final String accessToken = getAccessToken("gaurav@baeldung.com", "pass");
         this.webTestClient.get()
                 .uri(RESOURCE_URL)
                 .header("Authorization", "Bearer " + accessToken)
                 .exchange()
                 .expectStatus().isOk()
                 .expectBodyList(Project.class);

    }

    private String getAccessToken(String username, String password) {

    	final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", username);
        params.add("scope", "profile read write");
        params.add("password", password);
        final Response response = RestAssured.given().auth().preemptive().basic(CLIENT_ID, CLIENT_SECRET).with().params(params).when().post(TOKEN_URL);

        return response.jsonPath().getString("access_token");
    }
}