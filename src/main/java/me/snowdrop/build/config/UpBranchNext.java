package me.snowdrop.build.config;

import me.snowdrop.build.tag.Tag;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class UpBranchNext implements BranchNext {
  @Override
  public Tag nextTag(Iterable<Tag> tags) {
    Tag currentTag = null;

    for (Tag tag : tags) {
      if (tag.match(prefix(), suffix())) {
        currentTag = tag;
        break;
      }
    }

    if (currentTag == null) {
      return Tag.first(prefix(), suffix());
    } else {
      return currentTag.next(prefix(), suffix());
    }
  }

  protected String prefix() {
    return "v";
  }

  protected String suffix() {
    return "";
  }
}
