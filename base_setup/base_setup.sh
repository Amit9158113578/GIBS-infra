#!/bin/bash
node1=$NODE1
node2=$NODE2
node3=$NODE3
ENV=$ENV

if [ $# != 4 ]
then
echo "Please provide 3 node IPs and ENV variable"
exit 1
fi


#Installing ansible
sudo apt-add-repository --yes --update ppa:ansible/ansible
sudo apt update -y
sudo apt install ansible -y 
#sudo apt-get install python-jmespath zip unzip git -y


#Making ansible cfg file
echo '[defaults]
log_path = ./ansible.log
private_key_file = /root/.ssh/id_rsa
remote_user = root
interpreter_python = /usr/bin/python3
host_key_checking=False' | sudo tee  /etc/ansible/ansible.cfg


grep "$node1 node1" /etc/hosts

if [ $? != 0 ]
then
echo "$node1 node1"| sudo tee -a /etc/hosts
else
echo "hosts entries already present"
fi

grep "$node2 node2" /etc/hosts

if [ $? != 0 ]
then
echo "$node2 node2"| sudo tee -a /etc/hosts
else
echo "hosts entries already present"
fi

grep "$node3 node3" /etc/hosts

if [ $? != 0 ]
then
echo "$node3 node3"| sudo tee -a /etc/hosts
else
echo "hosts entries already present"
fi

grep "10.10.74.133 www.gov.rw" /etc/hosts

if [ $? != 0 ]
then
echo "10.10.74.133 www.gov.rw"| sudo tee -a /etc/hosts
else
echo "hosts entries already present"
fi

scp /etc/hosts root@$node2:/etc/hosts
scp /etc/hosts root@$node3:/etc/hosts
