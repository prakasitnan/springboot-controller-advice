pipeline {
    agent any

    parameters {
        string(name: 'MAJOR', defaultValue: '0', description: 'Major version number (X in X.Y.BUILD)')
        string(name: 'MINOR', defaultValue: '0', description: 'Minor version number (Y in X.Y.BUILD)')
    }

    environment {
        // 1. Docker Hub Configuration
        DOCKER_HUB_USER = 'prakasitnan'
        DOCKER_IMAGE    = "${DOCKER_HUB_USER}/springboot-controller-advice"

        // 2. Credentials
        DOCKER_CREDS = credentials('docker-hub-creds')  // Provides DOCKER_CREDS_USR / DOCKER_CREDS_PSW

        // 3. App version (Initialized empty, updated in script block)
        APP_VERSION = ""
    }

    // REMOVED: tools { maven 'Maven' } because we are using ./mvnw

    stages {

        // ── Stage 1: Checkout ────────────────────────────────────────────────
        stage('Checkout') {
            steps {
                echo "Checking out source code from GitHub..."
                checkout([
                    $class           : 'GitSCM',
                    branches         : [[name: '*/main']],
                    userRemoteConfigs: [[
                        // CHANGED: Using HTTPS instead of SSH to avoid Host Key Verification errors
                        url          : 'https://github.com/prakasitnan/springboot-controller-advice.git',
                        credentialsId: 'git-repo-creds'
                    ]]
                ])
                echo "Checkout complete – commit: ${GIT_COMMIT}"
            }
        }

        // ── Stage 2: Update Version in pom.xml ──────────────────────────────
        stage('Update Version') {
            steps {
                script {
                    // CHANGED: Must use env. prefix to globally update an environment variable
                    env.APP_VERSION = "${params.MAJOR}.${params.MINOR}.${BUILD_NUMBER}"
                }
                echo "Bumping pom.xml version to ${env.APP_VERSION}..."
                sh """
                    chmod +x mvnw
                    ./mvnw versions:set \
                        -DnewVersion=${env.APP_VERSION} \
                        -DgenerateBackupPoms=false \
                        -B
                """
                echo "pom.xml version updated to ${env.APP_VERSION}"
            }
        }

        // ── Stage 3: Build JAR + Docker Image ───────────────────────────────
        stage('Build') {
            steps {
                echo "Building Spring Boot JAR..."
                sh './mvnw clean package -DskipTests -B'

                echo "Building Docker image: ${DOCKER_IMAGE}:${env.APP_VERSION}"
                sh """
                    docker build \
                        -t ${DOCKER_IMAGE}:${env.APP_VERSION} \
                        -t ${DOCKER_IMAGE}:latest \
                        .
                """
            }
        }

        // ── Stage 4: Push Image to DockerHub ────────────────────────────────
        stage('Push to DockerHub') {
            steps {
                echo "Pushing Docker image to DockerHub..."
                sh """
                    echo "${DOCKER_CREDS_PSW}" | docker login -u "${DOCKER_CREDS_USR}" --password-stdin
                    docker push ${DOCKER_IMAGE}:${env.APP_VERSION}
                    docker push ${DOCKER_IMAGE}:latest
                """
                echo "Image ${DOCKER_IMAGE}:${env.APP_VERSION} pushed successfully."
            }
        }
    }

    // ── Post Actions ─────────────────────────────────────────────────────────
    post {
        always {
            echo "Cleaning up local Docker images to free disk space..."
            sh """
                docker rmi ${DOCKER_IMAGE}:${env.APP_VERSION} || true
                docker rmi ${DOCKER_IMAGE}:latest         || true
                docker logout                              || true
            """
        }
        success {
            echo "Pipeline succeeded! Image ${DOCKER_IMAGE}:${env.APP_VERSION} is live on DockerHub."
        }
        failure {
            echo "Pipeline FAILED. Check the logs above for details."
        }
    }
}