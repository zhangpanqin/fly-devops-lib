#!/usr/bin/env bash

git checkout master
CURRENT_DATE=$(date '+%Y-%m-%d-%H-%M-%S')
git tag -a "$CURRENT_DATE" -m "Create a tag for release"
git push origin --tags -f