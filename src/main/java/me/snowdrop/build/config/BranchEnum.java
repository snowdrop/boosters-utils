package me.snowdrop.build.config;

import me.snowdrop.build.tag.Tag;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
enum BranchEnum implements Branch {
  UPSTREAM(new UpBranch("upstream")),
  DOWNSTREAM(new DownBranch("downstream")),
  PROD(new DownBranch("prod")),
  REDHAT(new DownBranch("redhat"));

  private final Branch branch;

  BranchEnum(Branch branch) {
    this.branch = branch;
  }

  public String label() {
    return branch.label();
  }

  @Override
  public Tag nextTag(Iterable<Tag> currentTags) {
    return branch.nextTag(currentTags);
  }
}
