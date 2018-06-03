package cz.fi.muni.pv243;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenFormatStage;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;

import java.io.File;

public class Configuration {

    public static WebArchive deployment() {

        MavenResolverSystem resolver = Maven.resolver();
        MavenFormatStage dependencies = resolver
            .loadPomFromFile("pom.xml")
            .importCompileAndRuntimeDependencies()
            .importTestDependencies()
            .resolve()
            .withTransitivity();

        final WebArchive archive = ShrinkWrap
            .create(WebArchive.class, "test-persistence.war")
            .addPackages(true, "cz.fi.muni.pv243")
            .addAsResource("test-persistence.xml","META-INF/persistence.xml")
            .addAsLibraries(dependencies.asFile())
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        archive.as(ZipExporter.class).exportTo(new File("/tmp/" + archive.getName()), true);

        return archive;
    }
}