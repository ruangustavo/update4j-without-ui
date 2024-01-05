package me.ruan;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.update4j.Archive;
import org.update4j.Configuration;
import org.update4j.FileMetadata;
import org.update4j.UpdateContext;
import org.update4j.UpdateOptions;
import org.update4j.UpdateResult;
import org.update4j.inject.Injectable;
import org.update4j.service.UpdateHandler;

public class BootstrapUpdateHandler implements UpdateHandler, Injectable {

  private final Configuration configuration;

  public BootstrapUpdateHandler(Configuration configuration) {
    this.configuration = configuration;
  }

  public void update() {
    Path zip = Paths.get("business-update.zip");
    UpdateResult updateResult = this.configuration.update(
        UpdateOptions.archive(zip).updateHandler(BootstrapUpdateHandler.this));
    boolean isUpdateSuccessful = updateResult.getException() == null;

    if (isUpdateSuccessful) {
      try {
        Archive.read(zip).install();
      } catch (IOException e) {
        System.out.println("Erro ao instalar atualização");
      }
    }
  }

  public void launch() {
    configuration.launch(this);
  }

  @Override
  public void updateDownloadFileProgress(FileMetadata file, float frac)  {
    System.out.println("Downloading " + file.getPath().getFileName() + " (" + ((int) (100 * frac)) + "%)");
  }

  @Override
  public void failed(Throwable t) {
    if (t instanceof Exception) {
      System.out.println("Update aborted");
    } else {
      System.out.println("Failed: " + t.getClass().getSimpleName() + ": " + t.getMessage());
    }
  }

  @Override
  public void succeeded() {
    System.out.println("Download complete");
  }

  @Override
  public void init(UpdateContext context) throws Throwable {
    System.out.println("Initializing...");
    UpdateHandler.super.init(context);
  }
}
