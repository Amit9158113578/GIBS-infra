#!/bin/bash
IP=`hostname -I | awk '{print $1}'`
master=172.31.75.247
Node1=172.31.76.106
Node2=172.31.78.159

echo "updating apt repositories for kubernetes installation"

echo "deb https://apt.kubernetes.io/ kubernetes-xenial main"|sudo tee /etc/apt/sources.list.d/kubernetes.list
sudo add-apt-repository    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -


echo "aupgrading existing packages"
sudo apt-get update && sudo apt-get upgrade -y

if [ $? -ne 0 ]
then
	echo "failed to upgrade os packages"
	exit 1
fi


echo "installing kubelet kubeadm kubectl and other supported packages"
sudo apt-get install -y     apt-transport-https     ca-certificates     curl     gnupg-agent     software-properties-common kubelet kubeadm kubectl docker-ce docker-ce-cli containerd.io
if [ $? -ne 0 ]
then
	echo "failed to install packages"
	exit 1
fi

echo "making hold kubernetes packages"
sudo apt-mark hold kubelet kubeadm kubectl


echo "swap off"
sudo swapoff -a

if [ $? -ne 0 ]
then
	echo "failed during swapoff"
	exit 1
fi



#Kubernetes MASTER
sudo kubeadm init --pod-network-cidr=192.168.0.0/16 --control-plane-endpoint "$master:6443" --upload-certs
if [ $? -ne 0 ]
then
        echo "kube init failed"
        exit 1
fi
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"
kubectl taint nodes --all node-role.kubernetes.io/master-
kubectl apply -f https://download.elastic.co/downloads/eck/1.2.0/all-in-one.yaml

token=$(sudo kubeadm token create --print-join-command|tail -1)

scp -o StrictHostKeyChecking=no -i /root/ssh.pem /root/script-kube.sh neerajkumar.aptude.risa@$Node1:/tmp
ssh -o StrictHostKeyChecking=no -i /root/ssh.pem neerajkumar.aptude.risa@$Node1 bash /tmp/script-kube.sh
ssh -o StrictHostKeyChecking=no -i /root/ssh.pem neerajkumar.aptude.risa@$Node1 sudo $token

scp -o StrictHostKeyChecking=no -i /root/ssh.pem /root/script-kube.sh neerajkumar.aptude.risa@$Node2:/tmp
ssh -o StrictHostKeyChecking=no -i /root/ssh.pem neerajkumar.aptude.risa@$Node2 bash /tmp/script-kube.sh
ssh -o StrictHostKeyChecking=no -i /root/ssh.pem neerajkumar.aptude.risa@$Node2 sudo $token


# Kubernetes Minion 
##########execute command using ssh to join the nodes on cluster.Once all the nodes are in cluster then execute below command to label all the nodes.




#MASTER
Node_Names=`kubectl get no|awk '{print $1}'|tail -n +$((1+1))`
kubectl label nodes $Node_Names app=elasticsearch-master
