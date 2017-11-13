package me.snowdrop.build.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Config {
  private static final String REPOS = "https://api.github.com/orgs/%s/repos?per_page=100";

  private String root;
  private Set<String> repos = new TreeSet<>();
  private Branch branch;

  private String queryUrl;
  private String organization;
  private String repoRegExp;

    private String repo;

  private String username;
  private String password;
  private String passphrase;
  private String token;
  private String tokenFile;

  private String localPath;

  public String getRoot() {
    return root;
  }

  public Set<String> getRepos() {
    return repos;
  }

  public Branch getBranch() {
    return branch;
  }

  public String getQueryUrl() {
    return queryUrl;
  }

  public String getOrganization() {
    return organization;
  }

  public String getRepoRegExp() {
    return repoRegExp;
  }

    public String getRepo() {
        return repo;
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

  public String getToken() {
    return token;
  }

  public String getLocalPath() {
    return localPath;
  }

  public static Config parse(String... args) throws Exception {
    Config config = new Config();
    config.branch = BranchFactory.fromString(findArg(args, true, "b", "branch").toUpperCase());

    InputStream stream;
    String propsURL = findArg(args, false, "c", "config");
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

    config.root = findValue(args, properties, "repo.root", "root");

    int i = 1;
    String repo;
    while((repo = properties.getProperty("repo." + i)) != null) {
      i++;
      if (Boolean.parseBoolean(properties.getProperty("exclude." + repo)) == false) {
        config.repos.add(repo);
      }
    }

    config.queryUrl = findValue(args, properties, "queryUrl", "q", "query");
    if (config.queryUrl == null) {
      config.queryUrl = REPOS;
    }
    config.organization = findValue(args, properties, "organization", "o", "org", "organization");
      config.repoRegExp = findValue(args, properties, "repo.regexp", "re", "regexp", "repo.regexp");

      config.repo = findValue(args, properties, "repo", "r", "repo");

    config.username = findValue(args, properties, "username", "u", "user");
    config.password = findValue(args, properties, "password", "p", "pass");
    config.passphrase = findValue(args, properties, "passphrase", "ph", "phrase", "passphrase");
    config.token = findValue(args, properties, "token", "t", "token");
    config.tokenFile = findValue(args, properties, "tokenfile", "tf", "tokenfile");

    if (config.token == null && config.tokenFile != null) {
      File tokenFile = new File(config.tokenFile);
      check(tokenFile, true);
      try (BufferedReader reader = new BufferedReader(new FileReader(tokenFile))) {
        config.token = reader.readLine();
      }
    }

    config.localPath = findValue(args, properties, "local.path", "lp", "path", "localpath");

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

  private static String findValue(String[] args, Properties properties, String key, String... prefixes) {
    String value = findArg(args, false, prefixes);
    if (value == null) {
      value = properties.getProperty(key, null);
    }
    return value;
  }

  private static String findArg(String[] args, boolean required, String... prefixes) {
    for (String arg : args) {
      for (String prefix : prefixes) {
          if (arg.startsWith("-" + prefix + "=")) {
          return arg.substring(prefix.length() + 2); // - and =
        }
      }
    }
    if (required) {
      throw new IllegalArgumentException(String.format("No such prefix found: %s", Arrays.asList(prefixes)));
    }
    return null;
  }

  public String dump() {
    if (organization != null && repoRegExp != null) {
        return "\nOrg: " + organization + "\nRegExp: " + repoRegExp + "\n";
    } else if (repo != null) {
      return "\nRepo: " + repo + "\n";
    } else {
      return "\nRoot: " + root + "\nBranch: " + branch + "\nRepos: " + repos + "\n";
    }
  }
}
