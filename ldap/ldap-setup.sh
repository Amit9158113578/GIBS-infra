#Installing ansible
sudo apt-get update -y
sudo apt-get install default-jre -y 
wget "$INSTALL_LDAP_URL"
dpkg -i $INSTALL_LDAP_FILE
/etc/init.d/$LDAP_SERVICE status