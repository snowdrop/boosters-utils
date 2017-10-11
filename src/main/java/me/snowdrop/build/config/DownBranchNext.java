package me.snowdrop.build.config;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DownBranchNext extends UpBranchNext {
  @Override
  public String nextTag(String currentTag) {
    return super.nextTag(currentTag) + "-redhat";
  }
}
