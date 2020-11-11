#MASTER
Node_Names=`kubectl get no|awk '{print $1}'|tail -n +$((1+1))`
kubectl label nodes $Node_Names app=elasticsearch-master