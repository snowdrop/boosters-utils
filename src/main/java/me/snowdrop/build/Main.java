package me.snowdrop.build;

import me.snowdrop.build.config.Config;
import me.snowdrop.build.goals.Context;
import me.snowdrop.build.goals.Goal;
import me.snowdrop.build.goals.Goals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

    public static void run(String... args) {
        main(args);
    }

    public static void main(String[] args) {
        Config config = null;
        try {
            config = Config.parse(args);
        } catch (Exception e) {
            log.error("Invalid config: " + e.getMessage(), e);
            System.exit(255);
        }
        log.info("Config: " + config.dump());

        Context context = new Context(config);
        for (String sGoal : config.getGoals()) {
            Goal goal = Goals.getGoal(sGoal);
            goal.run(context);
        }

        int failures = context.getStats().getFailures();
        if (failures > 0) {
            log.warn("Failures: " + failures);
            System.exit(failures);
        }
    }
}
