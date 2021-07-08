#Installing ansible
sudo apt-get update -y
sudo apt-get install default-jre -y 
wget "$LDAP_URL"
dpkg -i $LDAP_FILE
/etc/init.d/$LDAP_SERVICE status