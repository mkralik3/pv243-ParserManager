package cz.fi.muni.pv243.rest;

import static org.assertj.core.api.Assertions.*;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.store.ParserStore;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;

import java.io.IOException;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@RunWith(Arquillian.class)
public class ParserServiceTest {

    private static final Logger log = Logger.getLogger( ParserServiceTest.class.getName() );

    private static final String BASE_URL = Configuration.BASE_URL;

    private Parser parser;
    private static boolean initialized = false;

    @Inject
    @CachedStore
    ParserStore store;

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Before
    public void init(){
        if (!initialized) { //workaround, beforeClass have to be static, but store is injected
            parser = new Parser();
            parser.setXpath("/a/b/c");
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
    @Transactional(TransactionMode.ROLLBACK)
    public void testPost() throws IOException {
        Parser newParser = new Parser();
        newParser.setXpath("x/y/z");

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
        assertThat(store.getAllParsers())
                .hasSize(2);
    }
}
