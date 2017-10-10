package me.snowdrop.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

  public List<String> getRepos() {
    return repos;
  }

  public Branch getBranch() {
    return branch;
  }

  public static Config parse(String[] args) throws Exception {
    Config config = new Config();
    config.branch = Branch.valueOf(findArg(args, true, "-b", "-branch").toUpperCase());

    InputStream stream;
    String reposFile = findArg(args, false, "-r", "-repos");
    if (reposFile != null) {
      File file = new File(reposFile);
      if (file.exists() == false || file.isDirectory()) {
        throw new IllegalArgumentException("No such file: " + file);
      }
      stream = new FileInputStream(file);
    } else {
      stream = Config.class.getClassLoader().getResourceAsStream("default.properties");
    }
    Properties properties = new Properties(System.getProperties());
    try {
      properties.load(stream);
    } finally {
      stream.close();
    }

    String root = properties.getProperty("repo.root");
    check("No repo root!", root);
    config.root = root;

    int i = 1;
    String repo;
    while((repo = properties.getProperty("repo." + i)) != null) {
      i++;
      if (properties.getProperty("exclude." + repo) == null) {
        config.repos.add(repo);
      }
    }

    return config;
  }

  private static void check(String msg, Object value) {
    if (value == null) {
      throw new IllegalArgumentException(msg);
    }
  }

  private static String findArg(String[] args, boolean required, String... prefixes) {
    for (String arg : args) {
      for (String prefix : prefixes) {
        if (arg.startsWith(prefix)) {
          return arg.substring(prefix.length());
        }
      }
    }
    if (required) {
      throw new IllegalArgumentException(String.format("No such prefix found: %s", Arrays.asList(prefixes)));
    }
    return null;
  }
}
