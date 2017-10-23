package me.snowdrop.build.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import me.snowdrop.build.config.Branch;
import me.snowdrop.build.config.Config;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractTagger implements Tagger {
  protected final Logger log = LoggerFactory.getLogger(getClass().getName());

  protected final Config config;
  protected final String repo;

  public AbstractTagger(Config config, String repo) {
    this.config = config;
    this.repo = repo;
  }

  protected void updateBranch(Git git) throws Exception {
  }

  protected boolean nextTag(Git git) throws Exception {
    try {
      final String currentBranch = git.getRepository().getBranch();
      try {
        Branch branch = config.getBranch();

        git.checkout().setName(branch.label()).call();

        updateBranch(git);

        List<Ref> tagRefs = git.tagList().call();
        List<Tag> tags = tagRefs.stream().map(Tag::new).collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(tags);

        Tag nextTag = branch.nextTag(tags);
        log.info(String.format("Next tag [%s]: %s", repo, nextTag));

        Ref tagged = git.tag().setName(nextTag.toString()).call();
        PushCommand pushCommand = git.push();
        handleSecurity(pushCommand);
        pushCommand.add(tagged).call();
        log.info(String.format("Tagging [%s] done.", repo));

        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      } finally {
        git.checkout().setName(currentBranch).call();
      }
    } finally {
      git.close();
    }
  }

  protected void handleSecurity(TransportCommand command) {
    command.setTransportConfigCallback(new CustomTransportConfigCallback());

    if (config.getToken() != null) {
      command.setCredentialsProvider(
        new UsernamePasswordCredentialsProvider(config.getToken(), "")
      );
    } else if (config.getUsername() != null && config.getPassword() != null) {
      command.setCredentialsProvider(
        new UsernamePasswordCredentialsProvider(config.getUsername(), config.getPassword())
      );
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
          showMessage(message);
          return (config.getPassword() != null);
        }

        @Override
        public boolean promptPassphrase(String message) {
          showMessage(message);
          return (config.getPassphrase() != null);
        }

        @Override
        public boolean promptYesNo(String message) {
          showMessage(message);
          return false;
        }

        @Override
        public void showMessage(String message) {
          log.info(">> " + message);
        }
      });
    }
  }
}

