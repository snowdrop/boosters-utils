package me.snowdrop.build.goals;

import me.snowdrop.build.config.Config;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Context {
    private Config config;
    private Stats stats = new Stats();

    public Context(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public Stats getStats() {
        return stats;
    }
}
