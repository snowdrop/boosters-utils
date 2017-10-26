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
      string(name: 'token', description: 'OAuth token?')
   }

   stages {
       stage('Preparation') {
          steps {
              // Get some code from a GitHub repository
              git 'https://github.com/alesj/boosters-tag'
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
              sh "java -jar ${WORKSPACE}/target/boosters-tag-${params.btagversion}.jar -b=${params.branch} -lp=${WORKSPACE} -o=snowdrop -t=${params.token} -r=-booster\$5"
          }
       }
   }
}
