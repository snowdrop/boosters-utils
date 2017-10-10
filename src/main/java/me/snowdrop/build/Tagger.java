package me.snowdrop.build;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Tagger {
  public static void tag(File repoDir, Branch branch) throws Exception {
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    Repository repository = builder.setGitDir(new File(repoDir, ".git"))
      .readEnvironment() // scan environment GIT_* variables
      .findGitDir() // scan up the file system tree
      .build();

    final Git git = new Git(repository);
    final String currentBranch = repository.getBranch();
    try {
      git.checkout().setName(branch.label()).call();

      AnyObjectId objectIdOfTag = repository.resolve("HEAD");
      RevWalk walk = new RevWalk(repository);
      RevTag tag = walk.parseTag(objectIdOfTag);

      String currentTag = tag.getTagName();
      String nextTag = branch.nextTag(currentTag);

      git.tag().setName(nextTag).call();
    } catch (Exception e) {
      System.err.println(e);
    } finally {
      git.checkout().setName(currentBranch).call();
    }
  }
}
