pipeline {
    agent none
    stages {
        stage('Clone Repository') {
            agent { label 'master' }  // Run this on the master node
            steps {
                git url: 'https://github.com/Hems1315/jenkins.git', branch: 'main'
            }
        }
        stage('Build') {
            agent { label 'slave1' }  // Build on Slave Node 1
            steps {
               dir('/home/ec2-user/workspace/Jenkins-Project1/maven-app/pom.xml)
                sh 'mvn clean package'  // Maven build command
            }
        }
        stage('Test') {
            agent { label 'slave2' }  // Run tests on Slave Node 2
            steps {
                sh 'mvn test'  // Maven test command
            }
        }
    }
    post {
        success {
            echo 'Build and Tests completed successfully!'
        }
        failure {
            echo 'Build or Tests failed.'
        }
    }
}
