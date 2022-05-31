/*
 * Copyright The Stargate Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.stargate.sgv2.docsapi.api.common.security;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.quarkus.test.junit.QuarkusTest;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class HeaderBasedAuthenticationMechanismTest {

  @ApplicationScoped
  public static class TestingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/v2/testing")
    public String greetAuthorized() {
      return "hello-authorized";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/testing")
    public String greet() {
      return "hello";
    }
  }

  @Test
  public void missingToken() {
    given()
        .when()
        .get("/v2/testing")
        .then()
        .statusCode(401)
        .body("code", is(401))
        .body(
            "description",
            is(
                "Role unauthorized for operation: Missing token, expecting one in the X-Cassandra-Token header."));
  }

  @Test
  public void notProtectedPaths() {
    given().when().get("/testing").then().statusCode(200);
  }
}