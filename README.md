# Tag your boosters!

Available options:
* -b, -branch = upstream | downstream *
* -c, -config = configuration file, if none, default.proprties from the project is used
* -u, -user = git username **
* -p, -pass = git password **
* -ph, -phrase, -passphrase = ssh passphrase **
* -root = url to git root owning booster repos ***
* -lp, -path, -localpath = local path to boosters ***


    * -- argument is required
    ** -- either user/pass or passphrase (depends on the git root)
    *** -- one of the 2 options is required

A config example:

-b=downstream
-c=file:/Users/alesj/projects/obsidian/snowdrop/boosters-tag/test.properties
-ph=mypazzfrajz

Where test.properties looks like

    local.path=/Users/alesj/projects/obsidian/snowdrop/
    repo.1=git@github.com:alesj/btagtest.git
