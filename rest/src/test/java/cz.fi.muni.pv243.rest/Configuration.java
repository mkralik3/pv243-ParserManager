package cz.fi.muni.pv243.rest;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenFormatStage;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;

import java.io.File;

public class Configuration {

    public final static int PORT = (System.getProperty("port.offset") == null)
            ? 8080
            : 8080 + Integer.valueOf(System.getProperty("port.offset"));

    public final static String BASE_URL = "http://localhost:" + PORT + "/" + Configuration.WAR_NAME + "/rest";

    public static final String WAR_NAME = "test-rest";

    public static WebArchive deployment() {

        MavenResolverSystem resolver = Maven.resolver();
        MavenFormatStage dependencies = resolver
                .loadPomFromFile("pom.xml")
                .importCompileAndRuntimeDependencies()
                .importTestDependencies()
                .resolve()
                .withTransitivity();

        final WebArchive archive = ShrinkWrap
                .create(WebArchive.class, WAR_NAME + ".war")
                .addPackages(true, "cz.fi.muni.pv243.rest")
                .addAsLibraries(dependencies.asFile())
                .setWebXML("web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        archive.as(ZipExporter.class).exportTo(new File("/tmp/" + archive.getName()), true);

        return archive;
    }
}
