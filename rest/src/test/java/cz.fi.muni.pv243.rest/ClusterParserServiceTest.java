package cz.fi.muni.pv243.rest;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.Restaurant;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.logging.Logger;

import static cz.fi.muni.pv243.rest.Configuration.BASE_URL2;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@RunWith(Arquillian.class)
@RunAsClient
@Ignore // Don't know ho to cleanup server base config after test, removing configuration like in @BeforeClass doesn't work, but tests are working when running only this class
public class ClusterParserServiceTest {

    private static final Logger log = Logger.getLogger( ClusterParserServiceTest.class.getName() );

    private static final String BASE_URL = Configuration.BASE_URL;

    private Parser parser;
    private static boolean initialized = false;

    @ArquillianResource
    private Deployer deployer;

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Deployment(name = "d2", managed = false)
    @TargetsContainer("node-2")
    public static WebArchive createDeployment2() {
        return Configuration.deployment();
    }
//
//    @BeforeClass
//    public static void removeContainerConfig() throws IOException {
//        FileUtils.deleteDirectory(new File(System.getProperty("jboss.home") + "/standalone-ha-node-1/configuration"));
//        FileUtils.deleteDirectory(new File(System.getProperty("jboss.home") + "/standalone-ha-node-2/configuration"));
//    }

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
            //store.addParser(parser);
            initialized = true;
            deployer.deploy("d2");
        }
    }

    @Test
    @Ignore
    public void testCluster(){
        Response response = given()
                .when()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(parser)
                .post(BASE_URL + "/parsers");
        log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "\n" +
                response.body().prettyPrint());

        response = given()
                .when()
                .get(BASE_URL + "/parsers");
        log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "\n" +
                response.body().prettyPrint());

        response.then()
                .statusCode(200)
                .body("find{it.id==1}.xpath", is(parser.getXpath()));

        response = given()
                .when()
                .get(BASE_URL2 + "/parsers");
        System.out.println("SECOND CALL ___------------------------");
        log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "\n" +
                response.body().prettyPrint());

        response.then()
                .statusCode(200)
                .body("find{it.id==1}.xpath", is(parser.getXpath()));
    }

//    @Test
//    public void testPost() throws IOException {
//        {
//            Parser newParser = new Parser();
//            newParser.setXpath("x/y/z");
//            Restaurant restaurant = new Restaurant();
//            restaurant.setGooglePlaceID("testGOOGLEid");
//            restaurant.setName("uPstrosa");
//            restaurant.addParser(newParser);
//            newParser.setRestaurant(restaurant);
//
//            Response response = given()
//                    .when()
//                    .accept(ContentType.JSON)
//                    .contentType(ContentType.JSON)
//                    .body(newParser)
//                    .post(BASE_URL + "/parsers");
//            log.info(new Object() {
//            }.getClass().getEnclosingMethod().getName() + "\n" +
//                    response.body().prettyPrint());
//
//            response.then()
//                    .statusCode(200)
//                    .body("xpath", is(newParser.getXpath()));
//            assertThat(store.getAllParsers(false))
//                    .hasSize(2);
//        }
//
//        Response response = given()
//                .when()
//                .accept(ContentType.JSON)
//                .contentType(ContentType.JSON)
//                .post(BASE_URL + "/parsers");
//
//    }
}
