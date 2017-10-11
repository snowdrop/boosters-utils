package me.snowdrop.build.config;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public enum Branch implements BranchNext {
  UPSTREAM(new UpBranchNext()),
  DOWNSTREAM(new DownBranchNext());

  private final BranchNext next;

  Branch(BranchNext next) {
    this.next = next;
  }

  public String label() {
    return name().toLowerCase();
  }


  @Override
  public String nextTag(String currentTag) {
    return next.nextTag(currentTag);
  }
}