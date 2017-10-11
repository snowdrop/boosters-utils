package me.snowdrop.build.config;

import me.snowdrop.build.tag.Tag;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class UpBranchNext implements BranchNext {
  @Override
  public Tag nextTag(Tag currentTag) {
    if (currentTag == null) {
      return Tag.first(prefix(), suffix());
    }
    return currentTag.next();
  }

  protected String prefix() {
    return "v";
  }

  protected String suffix() {
    return "";
  }
}
