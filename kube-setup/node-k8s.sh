i#!/bin/bash
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

