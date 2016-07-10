#!/bin/bash

if [ $TRAVIS_PULL_REQUEST == "false" ] && [ ${TRAVIS_BRANCH} = 'snapshot' ]; then
    mvn deploy -X --settings ./settings.xml -Pdeploy
    exit $?
fi