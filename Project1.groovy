pipeline {
    agent none // Use agent none to define where each stage will run

    stages {
        stage('Checkout') {
            agent { label 'master' } // This stage will run on the master node
            steps {
                // Checkout the code from Git
                git url: 'https://your-git-repository-url.git', branch: 'main'
            }
        }
        stage('Build') {
            agent { label 'build' } // Build will run on the build slave node
            steps {
                script {
                    // Run Maven build
                    sh 'mvn clean package'
                }
            }
        }
        stage('Test') {
            agent { label 'test' } // Testing will run on the test slave node
            steps {
                script {
                    // Run Maven tests
                    sh 'mvn test'
                }
            }
        }
        stage('Archive') {
            agent { label 'master' } // Archive results on the master node
            steps {
                // Archive the build artifacts and test results
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
                junit '**/target/test-classes/*.xml'
            }
        }
    }

    post {
        always {
            // Actions to perform after the build, regardless of success or failure
            echo 'Pipeline completed.'
        }
        success {
            // Actions to perform if the build is successful
            echo 'Build was successful.'
        }
        failure {
            // Actions to perform if the build fails
            echo 'Build failed.'
        }
    }
}
