if [ $TRAVIS_BRANCH == master ]; then
    if [ $TRAVIS_PULL_REQUEST == false ]; then
        mvn --batch-mode verify org.sonarsource.scanner.maven:sonar-maven-plugin:3.0.1:sonar -Dsonar.host.url=${env.SONAR_URL} -Dsonar.login=${env.SONAR_LOGIN}
    else
        mvn --batch-mode verify org.sonarsource.scanner.maven:sonar-maven-plugin:3.0.1:sonar -Dsonar.host.url=${env.SONAR_URL} -Dsonar.login=${env.SONAR_LOGIN} -Dsonar.analysis.mode=preview -Dsonar.issuesReport.console.enable=true
    fi
fi