package me.snowdrop.build.config;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DownBranchNext extends UpBranchNext {
  @Override
  protected String suffix() {
    return "-redhat";
  }
}
