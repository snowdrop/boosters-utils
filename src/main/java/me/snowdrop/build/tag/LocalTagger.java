package me.snowdrop.build.tag;

import java.io.File;

import me.snowdrop.build.config.Config;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class LocalTagger extends AbstractTagger {
  public LocalTagger(Config config, String repo) {
    super(config, repo);
    Config.check("No repo root!", config.getRoot());
  }

  public void tag() throws Exception {
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    Repository repository = builder.setGitDir(new File(config.getRoot() + repo, ".git"))
      .readEnvironment() // scan environment GIT_* variables
      .findGitDir() // scan up the file system tree
      .build();

    nextTag(new Git(repository));
  }
}
