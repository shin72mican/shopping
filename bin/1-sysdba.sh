#!/bin/bash

bin_dir=$(cd $(dirname $0) && pwd)
container_name=dbserver

# 環境変数読み込み
. $bin_dir/../docker/.env

cd $bin_dir/../docker && docker-compose exec $container_name sqlplus sys/$ORACLE_PWD@localhost:1521/XE as sysdba

