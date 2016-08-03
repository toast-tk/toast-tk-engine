#!/bin/bash

echo "working on branch $TRAVIS_BRANCH"
echo "Preparing deploy for tag: $TRAVIS_TAG"

if [ $TRAVIS_PULL_REQUEST == "false" ] && [ ${TRAVIS_BRANCH} = 'snapshot' ]; then
    echo 'Deploying snapshot to OSS'
    mvn clean deploy --settings ./settings.xml -DskipTests=true -Pdeploy;
    exit $?;
elif [ ! -z "$TRAVIS_TAG" ]; then
	echo 'Deploying release tag to OSS'
 	mvn --settings ./settings.xml org.codehaus.mojo:versions-maven-plugin:2.1:set -DnewVersion=$TRAVIS_TAG;
 	mvn clean install --settings ./settings.xml;
	mvn --batch-mode deploy --settings ./settings.xml -DskipTests=true -Pdeploy;
	exit $?;
fi