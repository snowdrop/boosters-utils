package me.snowdrop.build;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class UpBranchNext implements BranchNext {
  @Override
  public String nextTag(String currentTag) {
    int v = Integer.parseInt(currentTag.substring(1));
    return "v" + (++v);
  }
}
