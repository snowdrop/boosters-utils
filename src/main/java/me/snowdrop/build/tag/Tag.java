package me.snowdrop.build.tag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.lib.Ref;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Tag implements Comparable<Tag> {
    private static final Pattern TAG = Pattern.compile("([a-z]+)([0-9]+)(-[a-zA-Z]+)?");

    private final String prefix;
    private final int number;
    private final String suffix;

    private Tag(String prefix, int number, String suffix) {
        this.prefix = prefix;
        this.number = number;
        this.suffix = suffix;
    }

    public Tag(Ref ref) {
        String fullName = ref.getName();
        int p = fullName.lastIndexOf("/");
        String version = fullName.substring(p + 1);
        Matcher matcher = TAG.matcher(version);
        if (matcher.find()) {
            prefix = matcher.group(1);
            number = Integer.parseInt(matcher.group(2));
            String end = matcher.group(3);
            suffix = (end != null ? end : "");
        } else {
            throw new IllegalStateException("Cannot match tag: " + version);
        }
    }

    public boolean match(String prefix, String suffix) {
        return this.prefix.equals(prefix) && this.suffix.equals(suffix);
    }

    public static Tag first(String prefix, String suffix) {
        return new Tag(prefix, 1, suffix);
    }

    public Tag previous(String p, String s) {
        if (number - 1 <= 0) {
            throw new IllegalStateException("Already first version: " + toString());
        }
        return new Tag(p, number - 1, s);
    }

    public Tag next(String p, String s) {
        return new Tag(p, number + 1, s);
    }

    @Override
    public String toString() {
        return prefix + number + suffix;
    }

    @Override
    public int compareTo(Tag o) {
        return o.number - number;
    }
}
