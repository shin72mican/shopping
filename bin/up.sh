#!/bin/bash

bin_dir=$(cd $(dirname $0) && pwd)

composeFile=${1:-"docker-compose.yml"}
cd $bin_dir/../docker && docker-compose -f $composeFile up $@
