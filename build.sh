if [ ${TRAVIS_PULL_REQUEST} = 'false' ] && [ ${TRAVIS_BRANCH} = 'master' ]; then
      stringEcho 'Build and deploy master branch'
      mvn --batch-mode clean deploy org.sonarsource.scanner.maven:sonar-maven-plugin:3.0.1:sonar -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_LOGIN} --settings ./settings.xml -PDEPLOY_NEXUS_SYNAPTIX;
elif [ ${TRAVIS_PULL_REQUEST} != 'false' ]; then
      stringEcho 'Only build and analyze pull request'
      mvn --batch-mode clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:3.0.1:sonar -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_LOGIN} -Dsonar.github.oauth=${SONAAR_GITHUB_OAUTH} -Dsonar.github.repository=synaptix-labs/toast-tk-engine -Dsonar.github.pullRequest=${TRAVIS_PULL_REQUEST};
fi