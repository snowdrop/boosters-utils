package me.snowdrop.build.tag;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import me.snowdrop.build.config.Config;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RemoteTagger extends AbstractTagger {
  public RemoteTagger(Config config, String repo) {
    super(config, repo);
  }

  public void tag() throws Exception {
    File localPath = new File(config.getLocalPath());
    Config.check(localPath, false);
    File localDir = new File(localPath, "tmp-tags");
    deleteTempDir(localDir);

    CloneCommand cloneCommand = Git.cloneRepository().setURI(repo).setDirectory(localDir);
    cloneCommand.setTransportConfigCallback(new CustomTransportConfigCallback());
    if (config.getUsername() != null && config.getPassword() != null) {
      cloneCommand.setCredentialsProvider(
        new UsernamePasswordCredentialsProvider(config.getUsername(), config.getPassword())
      );
    }
    Git git = cloneCommand.call();
    try {
      nextTag(git);
    } finally {
      deleteTempDir(localDir);
    }
  }

  private static void deleteTempDir(File dir) throws IOException {
    Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }
}
