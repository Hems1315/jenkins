pipeline {
    agent none // Use agent none to define where each stage will run
    environment {
        M2_HOME = '/usr/share/apache-maven'
        PATH = "${M2_HOME}/bin:${env.PATH}"
    }
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
                    sh 'mvn clean package'
                }
            }
        }
        stage('Test') {
            agent { label 'test' } // Testing will run on the test slave node
            steps {
                script {
                    
                    sh 'mvn test'
                    
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
