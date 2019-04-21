#!/bin/bash

# trap exit ERR
user=`whoami`
backup="false"

show_help_info(){
  echo "###  Usage: ./migrate.sh -b"
  echo "###  数据迁移，-b表示保存现在数据库"
  echo "###"
}

for i in "$@"
do
  PFLAG=`echo $i|cut -b1-2`
  PPARAM=`echo $i|cut -b3-`
  if [ $PFLAG = "-b" ]; then
    backup="true"
  elif [ $PFLAG = "-h" ]; then
    show_help_info
    exit 1
  fi
done

echo "Db migration start."
echo "----------"

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

flyway_version=4.2.0
db_host=${db_url#*//}
db_host=${db_host%%:*}
db_port=${db_url##*:}
db_port=${db_port%%/*}
db_schema=${db_url##*/}
db_schema=${db_schema%%\?*}

echo "Config read:[db_url: ${db_url}, username: ${username}, password: ${password}, db_schema: ${db_schema}]"

if [ -f create_db.sh ]; then
  bash create_db.sh -u${username} -p${password} -s${db_schema} -H${db_host} -P${db_port}
fi

if [ ${backup} = "true" ] && [ -f backup_db.sh ]; then
  bash backup_db.sh 
fi

echo "----------"
echo "Database migrate"

result="failed"

if [ ! -d "../bin/flyway-commandline-${flyway_version}.tar.gz" ]; then
  tar -xf ../bin/flyway-commandline-${flyway_version}.tar.gz  -C ../bin
fi

../bin/flyway-${flyway_version}/flyway -configFile=../conf/env.conf -locations=filesystem:../sql/ -installedBy=${user} migrate

if [ $? -eq 0 ]; then
  result="success"
  echo "Database migrate success."
else
  echo "Database migrate failed! please rollback you db."
fi

echo "----------"
echo "Database migrate end."

if [ ${result} = "failed" ]; then
  exit -1
fi
