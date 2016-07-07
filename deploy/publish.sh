#!/bin/bash

if [[ $TRAVIS_PULL_REQUEST == "false" ]]; then
    mvn deploy --settings ./settings.xml -Possrh,deploy
    exit $?
fi