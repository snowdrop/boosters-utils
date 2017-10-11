package me.snowdrop.build.config;

import me.snowdrop.build.tag.Tag;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface BranchNext {
  Tag nextTag(Tag currentTag);
}
