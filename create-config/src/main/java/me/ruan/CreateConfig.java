package me.ruan;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
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

  /**
   * Configuração do Update4j para o a atualização da aplicação de negócio (config.xml)
   *
   * @return configuração do Update4j para o a aplicação de negócio
   */
  public static Configuration getUpdateConfig() {
    return Configuration.builder().baseUri(BASE_URI + "business").basePath("${user.dir}/business")
        .file(
            FileMetadata.readFrom(BUSINESS_DIR + "/business-1.0.0.jar").path("business-1.0.0.jar")
                .classpath()).property("maven.central", MAVEN_BASE)
        .files(
            FileMetadata.streamDirectory(BUSINESS_DIR).filter(CreateConfig::isImage)
        )
        .build();
  }

  private static final String MAVEN_BASE = "https://repo1.maven.org/maven2";

  /**
   * Configuração do Update4j para o bootstrap (setup.xml)
   *
   * @return configuração do Update4j para o bootstrap (setup.xml)
   */
  public static Configuration getBootstrapConfig() {
    URI businessConfigUri = URI.create(BASE_URI).resolve("business/config.xml");
    URI bootstrapJarUri = URI.create(BASE_URI).resolve("bootstrap/bootstrap-1.0.0.jar");

    return Configuration.builder().basePath("${user.dir}/bootstrap").file(
            FileMetadata.readFrom(BUSINESS_DIR + "/config.xml")
                .uri(businessConfigUri).path("../business/config.xml")).file(
            FileMetadata.readFrom(BOOTSTRAP_DIR + "/bootstrap-1.0.0.jar").classpath()
                .uri(bootstrapJarUri))
        .property("default.launcher.main.class", "org.update4j.Bootstrap")
//        .launcher("me.ruan.business.Business")
        .property("maven.central", MAVEN_BASE).build();
  }

  /**
   * @param output caminho o qual o arquivo de configuração será escrito
   * @param config configuração do Update4j
   */
  public static void writeTo(String output, Configuration config) {
    try (Writer out = Files.newBufferedWriter(Paths.get(output))) {
      config.write(out);
    } catch (IOException e) {
      System.out.printf("Error writing config file: %s%n", e.getMessage());
    }
  }

  private static boolean isImage(Reference file) {
    return Stream.of("png", "jpeg", "jpg")
        .anyMatch(extension -> file.getSource().toString().endsWith(extension));
  }
}
