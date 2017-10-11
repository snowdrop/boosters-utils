package me.snowdrop.build.tag;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import me.snowdrop.build.config.Branch;
import me.snowdrop.build.config.Config;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractTagger implements Tagger {
  protected final Logger log = Logger.getLogger(getClass().getName());

  protected final Config config;
  protected final String repo;

  public AbstractTagger(Config config, String repo) {
    this.config = config;
    this.repo = repo;
  }

  protected void nextTag(Git git) throws IOException, GitAPIException {
    try {
      final String currentBranch = git.getRepository().getBranch();
      try {
        Branch branch = config.getBranch();

        git.checkout().setName(branch.label()).call();

        List<Ref> tagRefs = git.tagList().call();
        List<Tag> tags = tagRefs.stream().map(Tag::new).collect(Collectors.toList());
        Collections.sort(tags);

        String currentTag = (tags.isEmpty() ? null : tags.get(0).getName());
        log.info(String.format("Current tag [%s]: %s", repo, currentTag));
        String nextTag = branch.nextTag(currentTag);
        log.info(String.format("Next tag [%s]: %s", repo, nextTag));

        Ref tagged = git.tag().setName(nextTag).call();
        git.push().setTransportConfigCallback(new CustomTransportConfigCallback()).add(tagged).call();
        log.info(String.format("Tagging [%s] done.", repo));

      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        git.checkout().setName(currentBranch).call();
      }
    } finally {
      git.close();
    }
  }

  protected class CustomTransportConfigCallback implements TransportConfigCallback {
    @Override
    public void configure(Transport transport) {
      if (transport instanceof SshTransport) {
        SshTransport sshTransport = (SshTransport) transport;
        sshTransport.setSshSessionFactory(new CustomSshSessionFactory());
      }
    }
  }

  protected class CustomSshSessionFactory extends JschConfigSessionFactory {
    @Override
    protected void configure(OpenSshConfig.Host host, Session session) {
      session.setUserInfo(new UserInfo() {
        @Override
        public String getPassphrase() {
          return config.getPassphrase();
        }

        @Override
        public String getPassword() {
          return config.getPassword();
        }

        @Override
        public boolean promptPassword(String message) {
          return false;
        }

        @Override
        public boolean promptPassphrase(String message) {
          return true;
        }

        @Override
        public boolean promptYesNo(String message) {
          return false;
        }

        @Override
        public void showMessage(String message) {
        }
      });
    }
  }
}
