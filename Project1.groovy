pipeline {
    agent none // Use agent none to define where each stage will run

    stages {
        stage('Checkout') {
            agent { label 'master' } // This stage will run on the master node
            steps {
                // Checkout the code from Git
                git url: 'https://github.com/Hems1315/jenkins.git', branch: 'main'
            }
        }
        stage('Build') {
            agent { label 'build' } // Build will run on the build slave node
            steps {
                script {
                    dir('/home/ec2-user/workspace/Testing') { // Ensure the directory is correct
                        echo 'Building the project with Maven'
                        sh 'ls -la' // List files to verify that pom.xml is present
                        sh 'mvn clean package'
                    }
                }
            }
        }
        stage('Test') {
            agent { label 'test' } // Testing will run on the test slave node
            steps {
                script {
                    dir('/home/ec2-user/workspace/Testing') { // Ensure the directory is correct
                        echo 'Running tests with Maven'
                        sh 'ls -la' // List files to verify that pom.xml is present
                        sh 'mvn test'
                    }
                }
            }
        }
        stage('Archive') {
            agent { label 'master' } // Archive results on the master node
            steps {
                echo 'Archiving build artifacts and test results'
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
                junit '**/target/test-classes/*.xml'
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        success {
            echo 'Build was successful.'
        }
        failure {
            echo 'Build failed.'
        }
    }
}
