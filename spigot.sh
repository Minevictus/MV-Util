#!/bin/sh

NEW=$(FILENAME="relocations.spigot.json" ./relocate.sh $@)
if [ $? -ne 0 ]
then
	echo "error upon relocating"
	exit 1
fi
echo "$NEW" > relocations.spigot.json
