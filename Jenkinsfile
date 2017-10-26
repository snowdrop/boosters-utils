node {
    stage('Preparation') {
        // Get some code from a GitHub repository
        git "${params.gitrepo}"
    }
    stage('Build') {
        // Run the maven build
        sh "mvn clean package"
    }
    stage('Deploy') {
        sh "java -jar ${WORKSPACE}/target/boosters-tag-${params.btagversion}.jar -b=${params.branch} -lp=${WORKSPACE} -o=${params.org} -t=${params.token} -r=${params.regexp} -q=${params.query}"
    }
}
