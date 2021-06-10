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
sudo apt-get install python-jmespath zip unzip git -y


#Making ansible cfg file
echo '[defaults]
private_key_file = /root/.ssh/id_rsa
remote_user = root
host_key_checking=False' | sudo tee  /etc/ansible/ansible.cfg

#Making /etc/hosts entries

grep "$node1 node1 
$node2 node2 
$node3 node3
10.10.74.133 www.gov.rw" /etc/hosts


echo "$node1 node1 
$node2 node2 
$node3 node3
10.10.74.133 www.gov.rw"| sudo tee -a /etc/hosts


scp /etc/hosts root@$node2:/etc/hosts
scp /etc/hosts root@$node3:/etc/hosts
