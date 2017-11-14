package me.snowdrop.build.goals;

import me.snowdrop.build.config.Config;
import me.snowdrop.build.config.RepoUtils;
import me.snowdrop.build.tag.Tagger;
import me.snowdrop.build.tag.TaggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class TaggerGoal implements Goal {
    private static final Logger log = LoggerFactory.getLogger(TaggerGoal.class);

    @Override
    public void run(Context context) {
        try {
            Config config = context.getConfig();
            for (String repo : RepoUtils.getRepos(config)) {
                log.info(String.format("Tagging repo: %s", repo));
                Tagger tagger = TaggerFactory.create(config, repo);
                if (!tagger.tag()) {
                    context.getStats().incrementFailures();
                }
            }
        } catch (Exception e) {
            context.getStats().incrementFailures();
            log.warn("Failure during run: " + e.getMessage(), e);
        }
    }
}
