#!/usr/bin/env bash

git checkout master
CURRENT_DATE=$(date '+%Y-%m-%d')
git tag -a "$CURRENT_DATE" -m "Create a tag for release"
git push origin "$CURRENT_DATE" --no-verify