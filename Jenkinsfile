#!/usr/bin/env groovy
def APP_NAME
def APP_VERSION
def DOCKER_IMAGE_NAME
def PROD_BUILD = false
pipeline {
    agent {
        node {
            label 'master'
        }
    }

    parameters {
        gitParameter branch: '',
                    branchFilter: '.*',
                    defaultValue: 'origin/main',
                    description: '', listSize: '0',
                    name: 'TAG',
                    quickFilterEnabled: false,
                    selectedValue: 'DEFAULT',
                    sortMode: 'DESCENDING_SMART',
                    tagFilter: '*',
                    type: 'PT_BRANCH_TAG'

        booleanParam defaultValue: false, description: '', name: 'RELEASE'
    }

    environment {
        GIT_URL = "https://github.com/wkdnffla3/k8s-backend-user.git"
        GITHUB_CREDENTIAL = "github-token"
        ARTIFACTS = "build/libs/**"
        DOCKER_REGISTRY = "wkdnffla3"
        DOCKERHUB_CREDENTIAL = 'docker hub -tocken'
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: "30", artifactNumToKeepStr: "30"))
        timeout(time: 120, unit: 'MINUTES')
    }

    tools {
        gradle 'Gradle 8.14.2'
        jdk 'OpenJDK 17'
        dockerTool 'Docker'
    }

    stages {
        stage('Set Version') {
            steps {
                script {
                    APP_NAME = sh (
                            script: "gradle -q getAppName",
                            returnStdout: true
                    ).trim()
                    APP_VERSION = sh (
                            script: "gradle -q getAppVersion",
                            returnStdout: true
                    ).trim()

                    DOCKER_IMAGE_NAME = "${DOCKER_REGISTRY}/${APP_NAME}:${APP_VERSION}"

                    sh "echo IMAGE_NAME is ${APP_NAME}"
                    sh "echo IMAGE_VERSION is ${APP_VERSION}"
                    sh "echo DOCKER_IMAGE_NAME is ${DOCKER_IMAGE_NAME}"

                    sh "echo TAG is ${params.TAG}"
                    if( params.TAG.startsWith('origin') == false && params.TAG.endsWith('/main') == false ) {
                        if( params.RELEASE == true ) {
                            DOCKER_IMAGE_VERSION += '-RELEASE'
                            PROD_BUILD = true
                        } else {
                            DOCKER_IMAGE_VERSION += '-TAG'
                        }
                    }
                }
            }
        }

        stage('Build & Test Application') {
            steps {
                sh "gradle clean build"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build "${DOCKER_IMAGE_NAME}"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry("", DOCKERHUB_CREDENTIAL) {
                        docker.image("${DOCKER_IMAGE_NAME}").push()
                    }

                    sh "docker rmi ${DOCKER_IMAGE_NAME}"
                }
            }
        }
    }
}
