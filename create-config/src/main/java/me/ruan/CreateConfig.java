package me.ruan;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.update4j.Configuration;
import org.update4j.FileMetadata;
import org.update4j.OS;

public class CreateConfig {

  private static final String BASE_URI = "https://raw.githubusercontent.com/ruangustavo/update4j-without-ui/master/build";

  public static void main(String[] args) throws IOException {
    String configLoc = System.getProperty("user.dir");
    String dir = configLoc + "/build/business";

    Configuration config = Configuration.builder()
        .baseUri(BASE_URI)
        .basePath("${user.dir}/business")
        .file(FileMetadata.readFrom(dir + "/business-1.0.0.jar").path("business-1.0.0.jar").classpath())
        .property("maven.central", MAVEN_BASE)
        .property("default.launcher.main.class", "me.ruan.Business")
        .build();

    try (Writer out = Files.newBufferedWriter(Paths.get(dir + "/config.xml"))) {
      config.write(out);
    }

    dir = configLoc + "/build/bootstrap";

    config = Configuration.builder()
        .baseUri(BASE_URI)
        .basePath("${user.dir}/bootstrap")
        .file(FileMetadata.readFrom(dir + "/../business/config.xml") // fall back if no internet
            .uri(BASE_URI + "/business/config.xml")
            .path("../business/config.xml"))
        .file(FileMetadata.readFrom(dir + "/bootstrap-1.0.0.jar")
            .classpath()
            .uri("https://github.com/ruangustavo/update4j-without-ui/raw/master/build"
                + "/bootstrap/bootstrap-1.0.0.jar"))
        .property("default.launcher.main.class", "me.ruan.Business")
        .property("maven.central", MAVEN_BASE)
        .build();

    try (Writer out = Files.newBufferedWriter(Paths.get(System.getProperty("user.dir") + "/build/setup.xml"))) {
      config.write(out);
    }
  }

  private static final String MAVEN_BASE = "https://repo1.maven.org/maven2";
}
