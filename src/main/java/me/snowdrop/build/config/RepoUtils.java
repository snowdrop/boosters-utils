package me.snowdrop.build.config;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RepoUtils {

  public static Set<String> getRepos(Config config) throws Exception {
    if (config.getOrganization() != null && config.getRepoRegExp() != null) {
      Pattern pattern = Pattern.compile(config.getRepoRegExp());
      Set<String> repos = new TreeSet<>();
      String url = String.format(config.getQueryUrl(), config.getOrganization());
      try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/vnd.github.v3+json");
        try (CloseableHttpResponse response = client.execute(request)) {
          HttpEntity entity = response.getEntity();
          String content = EntityUtils.toString(entity);
          int statusCode = response.getStatusLine().getStatusCode();
          if (statusCode < 200 || statusCode >= 300) {
            throw new IllegalStateException("Cannot read repositories: " + content);
          }
          boolean useSSH = (config.getPassphrase() != null);
          Gson gson = new Gson();
          GitHubProject[] projects = gson.fromJson(content, GitHubProject[].class);
          for (GitHubProject project : projects) {
            if (pattern.matcher(project.name).find()) {
              repos.add(useSSH ? project.ssh_url : project.clone_url);
            }
          }
        }
      }
      return repos;
    } else {
      return config.getRepos();
    }
  }

  private static class GitHubProject {
    private String name;
    private String ssh_url;
    private String clone_url;

    @Override
    public String toString() {
      return String.format("%s [%s] [%s]", name, ssh_url, clone_url);
    }
  }
}
