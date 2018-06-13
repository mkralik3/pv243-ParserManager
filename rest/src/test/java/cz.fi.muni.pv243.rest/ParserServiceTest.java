package cz.fi.muni.pv243.rest;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.store.ParserStore;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(Arquillian.class)
public class ParserServiceTest {

    private static final Logger log = Logger.getLogger( ParserServiceTest.class.getName() );

    private static final String BASE_URL = Configuration.BASE_URL;

    private Parser parser;
    private static boolean initialized = false;

    @Inject
    @CachedStore
    private ParserStore store;

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Before
    public void init(){
        if (!initialized) { //workaround, beforeClass have to be static, but store is injected
            parser = new Parser();
            parser.setXpath("/a/b/c");
            Restaurant restaurant = new Restaurant();
            restaurant.setGooglePlaceID("testGOOGLEid");
            restaurant.setName("uPstrosa");
            restaurant.addParser(parser);
            parser.setRestaurant(restaurant);
            store.addParser(parser);
            initialized = true;
        }
    }

    @Test
    public void testGet(){
        Response response = given()
                .when()
                .get(BASE_URL + "/parsers");
        log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "\n" +
                response.body().prettyPrint());

        response.then()
                .statusCode(200)
                .body("find{it.id==" + parser.getId() + "}.xpath", is(parser.getXpath()));
    }

    @Test
    public void testPost() throws IOException {
        {
            Parser newParser = new Parser();
            newParser.setXpath("x/y/z");
            Restaurant restaurant = new Restaurant();
            restaurant.setGooglePlaceID("testGOOGLEid");
            restaurant.setName("uPstrosa");
            restaurant.addParser(newParser);
            newParser.setRestaurant(restaurant);

            Response response = given()
                    .when()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(newParser)
                    .post(BASE_URL + "/parsers");
            log.info(new Object() {
            }.getClass().getEnclosingMethod().getName() + "\n" +
                    response.body().prettyPrint());

            response.then()
                    .statusCode(200)
                    .body("xpath", is(newParser.getXpath()));
            assertThat(store.getAllParsers(false))
                    .hasSize(2);
        }
        {
            Parser newParser = new Parser();
            newParser.setXpath("x/y/a");
            Restaurant restaurant = new Restaurant();
            restaurant.setGooglePlaceID("testGOOGLEid");
            restaurant.setName("uPstrosa");
            restaurant.addParser(newParser);
            newParser.setRestaurant(restaurant);

            Response response = given()
                    .when()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(newParser)
                    .post(BASE_URL + "/parsers");
            log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "\n" +
                    response.body().prettyPrint());

            response.then()
                    .statusCode(200)
                    .body("xpath", is(newParser.getXpath()));
            assertThat(store.getAllParsers(false))
                    .hasSize(3);
        }
    }
}
