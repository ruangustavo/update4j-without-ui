package me.ruan;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.update4j.Configuration;
import org.update4j.FileMetadata;
import org.update4j.FileMetadata.Reference;

public class CreateConfig {

  private static final String BASE_URI = "https://raw.githubusercontent.com/ruangustavo/update4j-without-ui/master/build/";
  private static final String CONFIG_DIR = System.getProperty("user.dir") + "/build";
  private static final String BUSINESS_DIR = CONFIG_DIR + "/business";
  private static final String BOOTSTRAP_DIR = CONFIG_DIR + "/bootstrap";

  public static void main(String[] args) {
    Configuration businessConfig = getUpdateConfig();
    writeTo(BUSINESS_DIR + "/config.xml", businessConfig);

    Configuration bootstrapConfig = getBootstrapConfig();
    writeTo(CONFIG_DIR + "/setup.xml", bootstrapConfig);
  }

  public static Configuration getUpdateConfig() {
    return Configuration.builder().baseUri(BASE_URI).basePath("${user.dir}/business").file(
            FileMetadata.readFrom(BUSINESS_DIR + "/business-1.0.0.jar").path("business-1.0.0.jar")
                .classpath()).property("maven.central", MAVEN_BASE)
        .files(
            FileMetadata.streamDirectory(BUSINESS_DIR).filter(CreateConfig::isImage)
        )
        .build();
  }

  private static final String MAVEN_BASE = "https://repo1.maven.org/maven2";

  public static Configuration getBootstrapConfig() {
    URI businessConfigUri = URI.create(BASE_URI).resolve("business/config.xml");
    URI bootstrapJarUri = URI.create(BASE_URI).resolve("bootstrap/bootstrap-1.0.0.jar");

    return Configuration.builder().basePath("${user.dir}/bootstrap").file(
            FileMetadata.readFrom(BUSINESS_DIR + "/config.xml")
                .uri(businessConfigUri).path("../business/config.xml")).file(
            FileMetadata.readFrom(BOOTSTRAP_DIR + "/bootstrap-1.0.0.jar").classpath()
                .uri(bootstrapJarUri))
        .property("default.launcher.main.class", "org.update4j.Bootstrap")
        .property("maven.central", MAVEN_BASE).build();
  }

  public static void writeTo(String output, Configuration config) {
    try (Writer out = Files.newBufferedWriter(Paths.get(output))) {
      config.write(out);
    } catch (IOException e) {
      System.out.println("Error writing config file: " + e.getMessage());
    }
  }

  private static boolean isImage(Reference file) {
    for (String extension : new String[]{"png", "jpeg", "jpg"}) {
      if (file.getSource().toString().endsWith(extension)) {
        return true;
      }
    }
    return false;
  }
}
