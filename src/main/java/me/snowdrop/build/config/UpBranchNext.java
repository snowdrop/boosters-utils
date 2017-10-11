package me.snowdrop.build.config;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class UpBranchNext implements BranchNext {
  @Override
  public String nextTag(String currentTag) {
    if (currentTag == null) {
      return "v1";
    }
    int v = Integer.parseInt(currentTag.substring(1));
    return "v" + (++v);
  }
}
