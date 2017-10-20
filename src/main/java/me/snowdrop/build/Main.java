package me.snowdrop.build;

import me.snowdrop.build.config.Config;
import me.snowdrop.build.config.RepoUtils;
import me.snowdrop.build.tag.Tagger;
import me.snowdrop.build.tag.TaggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Main {
  private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

  public static void run(String... args) {
    main(args);
  }

  public static void main(String[] args) {
    int failures = 0;
    try {
      Config config = Config.parse(args);
      log.info("Config: " + config.dump());
      for (String repo : RepoUtils.getRepos(config)) {
        log.info(String.format("Tagging repo: %s", repo));
        Tagger tagger = TaggerFactory.create(config, repo);
        if (!tagger.tag()) {
          failures++;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.exit(failures);
  }
}
