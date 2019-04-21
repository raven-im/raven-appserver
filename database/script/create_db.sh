#!/bin/bash

for i in "$@"
do
  PFLAG=`echo $i|cut -b1-2`
  PPARAM=`echo $i|cut -b3-`
  if [ $PFLAG = "-u" ]; then
    username=$PPARAM
  elif [ $PFLAG = "-p" ]; then
    password=$PPARAM
  elif [ $PFLAG = "-s" ]; then
    db_schema=$PPARAM
  elif [ $PFLAG = "-H" ]; then
    db_host=$PPARAM
  elif [ $PFLAG = "-P" ]; then
    db_port=$PPARAM
  fi
done

sql="CREATE DATABASE IF NOT EXISTS ${db_schema} CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
eval "mysql -u${username} -p${password} -h${db_host} -P${db_port} -e\"${sql}\""
