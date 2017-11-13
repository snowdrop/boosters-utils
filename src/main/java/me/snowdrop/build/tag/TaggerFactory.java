package me.snowdrop.build.tag;

import me.snowdrop.build.config.Config;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class TaggerFactory {
    public static Tagger create(Config config, String repo) throws Exception {
        if (repo.startsWith("https://") || repo.startsWith("git@") || repo.startsWith("git://")) {
            return new RemoteTagger(config, repo);
        } else {
            return new LocalTagger(config, repo);
        }
    }
}
