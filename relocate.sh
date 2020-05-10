#!/bin/sh

set -e

which jq >/dev/null 2>/dev/null
if [ $? -ne 0 ]
then
	echo "no jq found"
	exit 2
fi

if [ -z "$FILENAME" ]
then
	echo "no filename given"
	exit 1
fi

BASEPACKAGE="us.minevict.mvutil.dependencies"
PACKAGE="$@"
NEWPACKAGE="${BASEPACKAGE}.${PACKAGE}"

cat "$FILENAME" | jq ".\"${PACKAGE}\"=\"${NEWPACKAGE}\"" --monochrome-output --indent 4
