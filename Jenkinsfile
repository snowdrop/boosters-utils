node {
    def mvnHome
    stage('Preparation') {
        // Get some code from a GitHub repository
        git "${params.gitrepo}"
        // Get the Maven tool.
        // ** NOTE: This 'M3' Maven tool must be configured
        // **       in the global configuration.
        mvnHome = tool 'M3'
    }
    stage('Build') {
        // Run the maven build
        sh "'${mvnHome}/bin/mvn' clean package"
    }
    stage('Deploy') {
        sh "java -jar ${WORKSPACE}/target/boosters-tag-${params.btagversion}.jar -b=${params.branch} -lp=${WORKSPACE} -o=${params.org} -t=${params.token} -r=${params.regexp} -q=${params.query}"
    }
}
