#!/bin/bash

for i in "$@"
do
  PFLAG=`echo $i|cut -b1-2`
  PPARAM=`echo $i|cut -b3-`
  if [ $PFLAG = "-f" ]; then
    file=$PPARAM
  fi
done

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
echo "resume file: ${file}"
mysql -u${username} -p${password} -h${db_host} -P${db_port} ${db_schema} < ${file}
