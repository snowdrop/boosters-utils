package me.snowdrop.build.tag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.lib.Ref;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Tag implements Comparable<Tag> {
  private static final Pattern TAG = Pattern.compile("v([0-9]+)(-[a-zA-Z]+)?");

  private final String name;
  private final int number;

  public Tag(Ref ref) {
    String fullName = ref.getName();
    int p = fullName.lastIndexOf("/");
    this.name = fullName.substring(p + 1);
    Matcher matcher = TAG.matcher(this.name);
    if (matcher.find()) {
      number = Integer.parseInt(matcher.group(1));
    } else {
      throw new IllegalStateException("Cannot match tag: " + name);
    }
  }

  public String getName() {
    return name;
  }

  @Override
  public int compareTo(Tag o) {
    return o.number - number;
  }
}
