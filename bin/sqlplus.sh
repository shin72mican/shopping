bin_dir=$(cd $(dirname $0) && pwd)
docker cp ./1-create-tables.sql docker-dbserver-1:/home/oracle
docker cp ./2-insert-user.sql docker-dbserver-1:/home/oracle
docker cp ./3-drop-tables.sql docker-dbserver-1:/home/oracle
container_name=dbserver
. $bin_dir/../docker/.env
cd $bin_dir/../docker && docker-compose exec $container_name sqlplus tuser/tpassword@localhost:1521/TEST
