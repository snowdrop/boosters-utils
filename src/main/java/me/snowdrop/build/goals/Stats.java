package me.snowdrop.build.goals;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Stats {
    private int failures;

    public void incrementFailures() {
        failures++;
    }

    public int getFailures() {
        return failures;
    }
}
