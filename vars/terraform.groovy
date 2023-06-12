def call() {
    pipeline {

        agent {
            node {
                label 'workspace'
            }
        }
        options {
            ansiColor('xterm')
        }
        parameters {
            choice(name: 'env', choices: ['dev', 'prod'], description: 'Environment')
            choice(name: 'action', choices: ['apply', 'destroy'], description: 'Build/Destroy')
        }
        stages {
            stage('Terraform INIT') {
                steps{
                    sh 'terraform init -backend-config=env-${env}/state.tfvars'
                }
            }
            stage('Terraform Apply') {
                steps {
                    sh 'terraform ${action} -auto-approve -var-file=env-${env}/main.tfvars'
                }
            }
        }
        post {
            always {
                cleanWs()
            }
        }
    }
}