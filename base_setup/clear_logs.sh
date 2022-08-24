#!/bin/bash

# Elasticsearch Logs
rm /data/elasticsearch/logs/*.gz
rm /data/elasticsearch/logs/DEV-CLUSTER-*.log
rm /data/elasticsearch/logs/DEV-CLUSTER-*.json
rm /data/elasticsearch/logs/gc.log.*

truncate -s 0 /data/elasticsearch/logs/*

# Kibana Logs
rm /var/log/kibana/*.gz
rm /var/log/kibana/*-*.log

kibana_log_size=$(ls -lh /var/log/kibana/kibana.log | cut -d ' ' -f5)
if [[ $kibana_log_size == *"G"* ]]; then
  echo kibana.log is $kibana_log_size, flushing logs
  truncate -s 0 /var/log/kibana/kibana.log
fi

kibana_audit_size=$(ls -lh /var/log/kibana/audit.log | cut -d ' ' -f5)
if [[ $kibana_audit_size == *"G"* ]]; then
  echo Kibana audit.log is $kibana_audit_size, flushing logs
  truncate -s 0 /var/log/kibana/audit.log
fi


# Syslogs
rm /var/log/syslog.*

syslog_size=$(ls -lh /var/log/syslog | cut -d ' ' -f5)
if [[ $syslog_size == *"G"* ]]; then
  echo Syslog is $syslog_size, flushing logs
  truncate -s 0 /var/log/syslog
fi

# Logstash Logs
rm /var/log/logstash/*.gz
rm /var/log/logstash/logstash-plain-*.log
truncate -s 0 /var/log/logstash/*