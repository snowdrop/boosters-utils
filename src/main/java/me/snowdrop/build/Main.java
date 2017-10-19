package me.snowdrop.build;

import java.util.logging.Logger;

import me.snowdrop.build.config.Config;
import me.snowdrop.build.config.RepoUtils;
import me.snowdrop.build.tag.Tagger;
import me.snowdrop.build.tag.TaggerFactory;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Main {
  private static final Logger log = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    try {
      Config config = Config.parse(args);
      log.info("Config: " + config.dump());
      for (String repo : RepoUtils.getRepos(config)) {
        log.info(String.format("Tagging repo: %s", repo));
        Tagger tagger = TaggerFactory.create(config, repo);
        tagger.tag();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
