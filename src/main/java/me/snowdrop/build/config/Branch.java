package me.snowdrop.build.config;

import me.snowdrop.build.tag.Tag;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Branch {
    /**
     * Get branch' label / name.
     *
     * @return the label
     */
    String label();

    /**
     * Next tag.
     *
     * @param currentTags sorted (by version) current tags
     * @return next tag
     */
    Tag nextTag(Iterable<Tag> currentTags);
}
