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
        File repoDir = new File(config.getRoot() + repo);
        Config.check(repoDir, false);
        Tagger.tag(repoDir, config.getBranch());
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}
