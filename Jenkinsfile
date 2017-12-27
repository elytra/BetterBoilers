node {
	checkout scm
        sh 'chmod +x ./gradlew'
	sh './gradlew setupCiWorkspace clean build'
	archive 'build/libs/*jar'
}
