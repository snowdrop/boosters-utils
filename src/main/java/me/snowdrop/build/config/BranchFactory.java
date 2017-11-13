package me.snowdrop.build.config;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class BranchFactory {
    static Branch fromString(String value) {
        try {
            return BranchEnum.valueOf(value);
        } catch (Exception e) {
            return new UpBranch(value);
        }
    }
}
