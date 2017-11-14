package me.snowdrop.build.goals;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Goals {
    private static final Map<String, Goal> GOALS = new HashMap<>();

    static {
        GOALS.put("tag", new TaggerGoal());
    }

    public static Goal getGoal(String sGoal) {
        Goal goal = GOALS.get(sGoal);
        if (goal == null) {
            try {
                Class<?> goalClass = Goals.class.getClassLoader().loadClass(sGoal);
                return (Goal) goalClass.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return goal;
    }
}
