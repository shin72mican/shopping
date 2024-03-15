#!/bin/bash

bin_dir=$(cd $(dirname $0) && pwd)
container_name=gradle
cd $bin_dir/../docker && docker-compose run $container_name  gradle clean build

