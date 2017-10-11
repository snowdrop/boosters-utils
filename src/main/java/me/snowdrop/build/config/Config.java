package me.snowdrop.build.config;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Config {
  private String root;
  private List<String> repos = new ArrayList<>();
  private Branch branch;
  private String remote;

  private String username;
  private String password;
  private String passphrase;

  private String localPath;

  public String getRoot() {
    return root;
  }

  public List<String> getRepos() {
    return repos;
  }

  public Branch getBranch() {
    return branch;
  }

  public String getRemote() {
    return remote;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getPassphrase() {
    return passphrase;
  }

  public String getLocalPath() {
    return localPath;
  }

  public static Config parse(String[] args) throws Exception {
    Config config = new Config();
    config.branch = Branch.valueOf(findArg(args, true, "-b", "-branch").toUpperCase());

    InputStream stream;
    String propsURL = findArg(args, false, "-c", "-config");
    if (propsURL != null) {
      stream = new URL(propsURL).openStream();
    } else {
      stream = Config.class.getClassLoader().getResourceAsStream("default.properties");
    }
    Properties properties = new Properties(System.getProperties());
    try {
      properties.load(stream);
    } finally {
      stream.close();
    }

    String remote = findArg(args, false, "-r", "-remote");
    if (remote == null) {
      remote = properties.getProperty("remote", "origin");
    }
    config.remote = remote;

    String root = properties.getProperty("repo.root");
    config.root = root;

    int i = 1;
    String repo;
    while((repo = properties.getProperty("repo." + i)) != null) {
      i++;
      if (Boolean.parseBoolean(properties.getProperty("exclude." + repo)) == false) {
        config.repos.add(repo);
      }
    }

    String username = findArg(args, false, "-u", "-user");
    if (username == null) {
      username = properties.getProperty("username");
    }
    config.username = username;

    String password = findArg(args, false, "-p", "-pass");
    if (password == null) {
      password = properties.getProperty("password");
    }
    config.password = password;

    String passphrase = findArg(args, false, "-ph", "-phrase", "-passphrase");
    if (passphrase == null) {
      passphrase = properties.getProperty("passphrase");
    }
    if (passphrase == null) {
      passphrase = password;
    }
    config.passphrase = passphrase;

    String localPath = findArg(args, false, "-lp", "-path", "-localpath");
    if (localPath == null) {
      localPath = properties.getProperty("local.path");
    }
    if (localPath == null) {
      localPath = root;
    }
    config.localPath = localPath;

    return config;
  }

  public static void check(File file, boolean isFile) {
    if (file.exists() == false) {
      throw new IllegalArgumentException("No such file: " + file);
    }
    if (file.isFile() != isFile) {
      throw new IllegalArgumentException("File check failed: " + file);
    }
    if (file.isDirectory() == isFile) {
      throw new IllegalArgumentException("Directory check failed: " + file);
    }
  }

  public static void check(String msg, Object value) {
    if (value == null) {
      throw new IllegalArgumentException(msg);
    }
  }

  private static String findArg(String[] args, boolean required, String... prefixes) {
    for (String arg : args) {
      for (String prefix : prefixes) {
        if (arg.startsWith(prefix)) {
          return arg.substring(prefix.length() + 1); //+1=
        }
      }
    }
    if (required) {
      throw new IllegalArgumentException(String.format("No such prefix found: %s", Arrays.asList(prefixes)));
    }
    return null;
  }

  public String dump() {
    return "Root: " + root + "\n" + "Branch: " + branch + "\n" + "Repos: " + repos + "\n";
  }
}
