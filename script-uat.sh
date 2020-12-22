node1="10.10.94.140"
node2="10.10.94.141"
node3="10.10.94.142"
#udo apt-add-repository --yes --update ppa:ansible/ansible
apt update -y
apt install ansible -y 
apt-get install python-jmespath zip unzip git -y
echo '[defaults]
private_key_file = /root/.ssh/id_rsa
remote_user = root
host_key_checking=False' | tee  /etc/ansible/ansible.cfg

cd /tmp/
#git clone git@github.com:gaur4580/elk.git
#cd elk


echo "
$node1 node1.elastic.uat.net node1 
$node2 node2.elastic.uat.net node2 
$node3 node3.elastic.uat.net node3 "| tee -a /etc/hosts

#unzip uat.zip
#ansible-playbook -i host elk.yml
#ansible-playbook -i host kibana.yml
