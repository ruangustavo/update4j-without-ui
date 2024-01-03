package me.ruan;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.update4j.Configuration;
import org.update4j.service.Delegate;

public class Bootstrap implements Delegate {

  public static void main(String[] args) {
    Bootstrap bootstrap = new Bootstrap();

    try {
      bootstrap.main(List.of());
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }

  @Override
  public long version() {
    return 1;
  }

  @Override
  public void main(List<String> list) throws Throwable {
    URL configUrl = new URL("http://docs.update4j.org/demo/business/config.xml");
    Configuration config = null;
    try (Reader in = new InputStreamReader(configUrl.openStream(), StandardCharsets.UTF_8)) {
      config = Configuration.read(in);
    } catch (IOException e) {
      System.err.println("Could not load remote config, falling back to local.");
      try (Reader in = Files.newBufferedReader(Paths.get("business/config.xml"))) {
        config = Configuration.read(in);
      }
    }
  }

}