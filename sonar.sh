if [ $TRAVIS_BRANCH == master ]; then
        mvn --batch-mode verify org.sonarsource.scanner.maven:sonar-maven-plugin:3.0.1:sonar -Dsonar.host.url=${env.SONAR_URL} -Dsonar.login=${env.SONAR_LOGIN}
fi