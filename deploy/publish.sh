#!/bin/bash

if [ $TRAVIS_PULL_REQUEST == "false" ] && [ ${TRAVIS_BRANCH} = 'snapshot' ]; then
    mvn deploy --settings ./settings.xml -Possrh,deploy
    exit $?
fi