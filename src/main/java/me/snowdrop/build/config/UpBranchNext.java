package me.snowdrop.build.config;

import me.snowdrop.build.tag.Tag;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class UpBranchNext implements BranchNext {
  @Override
  public String nextTag(String currentTag) {
    if (currentTag == null) {
      return "v1";
    }
    int v = Tag.parseVersion(currentTag);
    return "v" + (++v);
  }
}
