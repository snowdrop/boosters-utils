package me.snowdrop.build.config;

import me.snowdrop.build.tag.Tag;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class UpBranch implements Branch {
    private String label;

    public UpBranch(String label) {
        this.label = label;
    }

    @Override
    public String label() {
        return label;
    }

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
