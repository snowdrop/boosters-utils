# DEPRECATED: use https://github.com/snowdrop/booster-common instead

---

# Boosters Utils!

Available options:
* -g, -goals = ordered set of goals *
* -b, -branch = upstream | downstream | redhat | \<any\> *
* -c, -config = configuration file, if none, default.properties from the project is used
* -u, -user = git username **
* -p, -pass = git password **
* -ph, -phrase, -passphrase = ssh passphrase **
* -t, -token = oauth token **
* -tf, -tokenfile = file with oauth token **
* -root = url to git root owning booster repos ***
* -lp, -path, -localpath = local path to boosters ***
* -q, -query = GitHub query url to grab repos, by default organization repos list query is used
* -o, -org, -organization = repo organization
* -re, -regexp, -repo.regexp = regexp for matching repo org repos
* -r, -repo = explicit repo


    * -- argument is required
    ** -- either user/pass or passphrase or token (depends on the git root)
    *** -- one of the 2 options is required

A config example:

-b=downstream
-c=file:/Users/alesj/projects/obsidian/snowdrop/boosters-utils/test.properties
-ph=mypazzfrajz

Where test.properties looks like

    local.path=/Users/alesj/projects/obsidian/snowdrop/
    repo.1=git@github.com:alesj/btagtest.git

After "mvn clean install" you get a runnable uber-jar:

    java -jar target/boosters-utils-1.0.0-SNAPSHOT.jar -b=upstream -c=file:/Users/alesj/projects/obsidian/snowdrop/boosters-utils/test.properties -ph=mypazzfrajz
