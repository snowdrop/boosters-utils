package me.snowdrop.build;

import java.io.File;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Main {
  public static void main(String[] args) {
    try {
      Config config = Config.parse(args);
      for (String repo : config.getRepos()) {
        File repoFile = new File(config.getRoot() + repo);
        Config.check(repoFile);
        Tagger.tag(repoFile, config.getBranch());
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}
