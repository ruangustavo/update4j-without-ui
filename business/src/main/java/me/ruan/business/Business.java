package me.ruan.business;

import org.update4j.LaunchContext;
import org.update4j.service.Launcher;

public class Business implements Launcher {

  public static void main(String[] args) {
    System.out.println("Business.main: executando aplicação de negócio");
  }

  @Override
  public long version() {
    return 1;
  }

  @Override
  public void run(LaunchContext launchContext) {
    System.out.println("Business.run: executando aplicação de negócio");
  }
}