#!/bin/bash
IP=`hostname -I | awk '{print $1}'`
#master=10.10.73.15
#Node1=10.10.73.16
#Node2=10.10.73.17
newnode=IP

token=$(sudo kubeadm token create --print-join-command|tail -1)

scp -o StrictHostKeyChecking=no /root/node-k8s.sh neerajkumar.aptude.risa@$newnode:/tmp
ssh -o StrictHostKeyChecking=no neerajkumar.aptude.risa@$newnode bash /tmp/node-k8s.sh
ssh -o StrictHostKeyChecking=no neerajkumar.aptude.risa@$newnode sudo $token



# Kubernetes Minion 
##########execute command using ssh to join the nodes on cluster.Once all the nodes are in cluster then execute below command to label all the nodes.
