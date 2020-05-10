#!/bin/sh

NEW=$(FILENAME="relocations.bungee.json" ./relocate.sh $@)
if [ $? -ne 0 ]
then
	echo "error upon relocating"
	exit 1
fi
echo "$NEW" > relocations.bungee.json
