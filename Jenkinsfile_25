pipeline {
   agent any

   environment {
      // Get the Maven tool.
      // ** NOTE: This 'M3' Maven tool must be configured
      // **       in the global configuration.
      mvnHome = tool 'M3'
   }

   parameters {
      string(name: 'btagversion', defaultValue: '1.0.0-SNAPSHOT', description: 'Which boosters-tag version?')
      string(name: 'branch', defaultValue: 'upstream', description: 'Which branch to use?')
      string(name: 'gitrepo', defaultValue: 'https://github.com/alesj/boosters-tag', description: 'GitHub repository?')
      string(name: 'query', defaultValue: 'https://api.github.com/orgs/%s/repos?per_page=100', description: 'GitHub query?')
      string(name: 'reqexp', defaultValue: '-booster$', description: 'GitHub repo regexp match?')
      string(name: 'org', defaultValue: 'snowdrop', description: 'GitHub organization / user?')
      string(name: 'token', description: 'OAuth token?')
   }

   stages {
       stage('Preparation') {
          steps {
              // Get some code from a GitHub repository
              git "${params.gitrepo}"
          }
       }
       stage('Build') {
          steps {
              // Run the maven build
              sh "'${mvnHome}/bin/mvn' clean package"
          }
       }
       stage('Deploy') {
          steps {
              sh "java -jar ${WORKSPACE}/target/boosters-tag-${params.btagversion}.jar -b=${params.branch} -lp=${WORKSPACE} -o=${params.org} -t=${params.token} -r=${params.regexp} -q=${params.query}"
          }
       }
   }
}
