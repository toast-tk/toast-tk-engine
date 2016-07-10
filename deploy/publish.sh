#!/bin/bash

if [ $TRAVIS_PULL_REQUEST == "false" ] && [ ${TRAVIS_BRANCH} = 'snapshot' ]; then
	mvn help:effective-settings -DshowPasswords=true
    mvn deploy --settings ./settings.xml -DskipTests=true -Pdeploy;
    exit $?;
fi