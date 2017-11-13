package me.snowdrop.build.config;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DownBranch extends UpBranch {
    public DownBranch(String label) {
        super(label);
    }

    @Override
    protected String suffix() {
        return "-redhat";
    }
}
