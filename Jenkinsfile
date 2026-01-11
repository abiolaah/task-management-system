pipeline {
	agent any

	tools {
		maven 'Maven-3.9'
		jdk 'JDK-11'
	}

	stages {
		stage('Checkout') {
			steps {
				git branch: 'main', url: 'https://github.com/abiolaah/task-management-system.git'
			}
		}

		stage('Build') {
			steps {
				sh 'mvn clean compile'
			}
		}

		stage('Test') {
			steps {
				sh 'mvn test'
			}
			post {
				always {
					junit '**/target/surefire-reports/*.xml'
				}
			}
		}

		stage('Package') {
			steps {
				sh 'mvn package -DskipTests'
			}
		}

		stage('Deploy') {
			steps {
				echo 'Deploying application...'
				// Add deployment steps here
			}
		}
	}

	post {
		success {
			echo 'Pipeline succeeded!'
		}
		failure {
			echo 'Pipeline failed!'
		}
	}
}