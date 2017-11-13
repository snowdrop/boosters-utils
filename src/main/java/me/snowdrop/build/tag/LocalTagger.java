package me.snowdrop.build.tag;

import java.io.File;
import java.io.IOException;

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

    @Override
    protected boolean tagInternal() throws Exception {
        Repository repository = null;
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            repository = builder.setGitDir(new File(config.getRoot() + repo, ".git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nextTag(new Git(repository));
    }

    @Override
    protected void updateBranch(Git git) throws Exception {
        git
            .pull()
            .setRebase(true)
            .setRemoteBranchName(config.getBranch().label())
            .setTransportConfigCallback(new CustomTransportConfigCallback())
            .call();
    }
}
