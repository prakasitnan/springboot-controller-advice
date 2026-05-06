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

        // 2. Credentials (IDs must match Jenkins settings)
        DOCKER_CREDS = credentials('docker-hub-creds')  // Provides DOCKER_CREDS_USR / DOCKER_CREDS_PSW
        GIT_CREDS    = credentials('git-repo-creds')    // SSH key for GitHub checkout

        // 3. App version — set dynamically in 'Update Version' stage
        APP_VERSION = ""
    }

    tools {
        // Requires 'Maven' tool configured in Jenkins → Global Tool Configuration
        maven 'Maven'
    }

    stages {

        // ── Stage 1: Checkout ────────────────────────────────────────────────
        stage('Checkout') {
            steps {
                echo "Checking out source code from GitHub..."
                checkout([
                    $class           : 'GitSCM',
                    branches         : [[name: '*/main']],
                    userRemoteConfigs: [[
                        url          : 'git@github.com:prakasitnan/springboot-controller-advice.git',
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
                    // SemVer: MAJOR.MINOR.BUILD_NUMBER  (e.g. 1.2.42)
                    // MAJOR and MINOR come from pipeline input parameters
                    APP_VERSION = "${params.MAJOR}.${params.MINOR}.${BUILD_NUMBER}"
                }
                echo "Bumping pom.xml version to ${APP_VERSION}..."
                sh """
                    chmod +x mvnw
                    ./mvnw versions:set \
                        -DnewVersion=${APP_VERSION} \
                        -DgenerateBackupPoms=false \
                        -B
                """
                echo "pom.xml version updated to ${APP_VERSION}"
            }
        }

        // ── Stage 3: Build JAR + Docker Image ───────────────────────────────
        stage('Build') {
            steps {
                echo "Building Spring Boot JAR..."
                sh './mvnw clean package -DskipTests -B'

                echo "Building Docker image: ${DOCKER_IMAGE}:${APP_VERSION}"
                sh """
                    docker build \
                        -t ${DOCKER_IMAGE}:${APP_VERSION} \
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
                    docker push ${DOCKER_IMAGE}:${APP_VERSION}
                    docker push ${DOCKER_IMAGE}:latest
                """
                echo "Image ${DOCKER_IMAGE}:${APP_VERSION} pushed successfully."
            }
        }
    }

    // ── Post Actions ─────────────────────────────────────────────────────────
    post {
        always {
            echo "Cleaning up local Docker images to free disk space..."
            sh """
                docker rmi ${DOCKER_IMAGE}:${APP_VERSION} || true
                docker rmi ${DOCKER_IMAGE}:latest         || true
                docker logout                              || true
            """
        }
        success {
            echo "Pipeline succeeded! Image ${DOCKER_IMAGE}:${APP_VERSION} is live on DockerHub."
        }
        failure {
            echo "Pipeline FAILED. Check the logs above for details."
        }
    }
}









