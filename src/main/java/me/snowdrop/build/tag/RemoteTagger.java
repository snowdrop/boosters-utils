package me.snowdrop.build.tag;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import me.snowdrop.build.config.Branch;
import me.snowdrop.build.config.Config;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RemoteTagger extends AbstractTagger {
    public RemoteTagger(Config config, String repo) {
        super(config, repo);
        Config.check("No local path!", config.getLocalPath());
    }

    @Override
    protected boolean tagInternal() throws Exception {
        File localPath = new File(config.getLocalPath());
        Config.check(localPath, false);
        File tempDir = new File(localPath, "tmp-tags");
        if (tempDir.exists()) {
            deleteTempDir(tempDir);
        }
        if (tempDir.mkdir() == false) {
            throw new IllegalStateException("Cannot create temp dir: " + tempDir);
        }

        Branch branch = config.getBranch();
        log.info(String.format("Remote checkout [%s] to %s", branch, tempDir));

        CloneCommand cloneCommand = Git.cloneRepository().setURI(repo).setDirectory(tempDir).setBranch(branch.label());
        handleSecurity(cloneCommand);
        Git git = cloneCommand.call();
        try {
            return nextTag(git);
        } finally {
            deleteTempDir(tempDir);
            log.info("Deleted temp dir: " + tempDir);
        }
    }

    private static void deleteTempDir(File dir) throws IOException {
        Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
