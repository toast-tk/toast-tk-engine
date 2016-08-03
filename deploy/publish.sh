#!/bin/bash

echo 'working on branch $TRAVIS_BRANCH'
echo 'Preparing deploy for tag: $TRAVIS_TAG'

if [ $TRAVIS_PULL_REQUEST == "false" ] && [ ${TRAVIS_BRANCH} = 'snapshot' ]; then
    echo 'Deploying snapshot to OSS'
    mvn clean deploy --settings ./settings.xml -DskipTests=true -Pdeploy -B -U;
    exit $?;
elif [ ! -z "$TRAVIS_TAG" ] && [ ${TRAVIS_BRANCH} = 'master' ]; then
	echo 'Deploying release tag to OSS'
 	mvn --settings ./settings.xml org.codehaus.mojo:versions-maven-plugin:2.1:set -DnewVersion=$TRAVIS_TAG;
	mvn clean deploy --settings ./settings.xml -DskipTests=true -Pdeploy -B -U;
	exit $?;
fi