node1=""
node2=""
node3=""
#udo apt-add-repository --yes --update ppa:ansible/ansible
sudo apt update -y
sudo apt install ansible -y 
sudo apt-get install python-jmespath zip unzip git -y
echo '[defaults]
private_key_file = /root/.ssh/id_rsa
remote_user = root
host_key_checking=False' | sudo tee  /etc/ansible/ansible.cfg

echo "
$node1 node1.elastic.stage.net node1 
$node2 node2.elastic.stage.net node2 
$node3 node3.elastic.stage.net node3 "| sudo tee -a /etc/hosts

#unzip uat.zip
#ansible-playbook -i host elk.yml
#ansible-playbook -i host kibana.yml


