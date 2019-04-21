#!/bin/bash

while read line
do
  if [[ $line = "flyway.url = "* ]];then
    db_url=${line##flyway.url = }
  fi
  if [[ $line = "flyway.user = "* ]];then
    username=${line##flyway.user = }
  fi
  if [[ $line = "flyway.password = "* ]];then
    password=${line##flyway.password = }
  fi
done < ../conf/env.conf

db_host=${db_url#*//}
db_host=${db_host%%:*}
db_port=${db_url##*:}
db_port=${db_port%%/*}
db_schema=${db_url##*/}
db_schema=${db_schema%%\?*}

start_time=`date +%Y%m%d%H%M%S`

echo "----------"
echo "Database backup"
mkdir -p "backup"

file="backup/${db_schema}-${start_time}.sql"
echo "backup file: ${file}"

mysqldump -u${username} -p${password} -h${db_host} -P${db_port} ${db_schema} > ${file}
