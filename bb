rm /root/fog-debian -rf
#    â–‘â–’â–“â–ˆ â˜ï¸ Project Fog 2.4.0 â˜ï¸ â–ˆâ–“â–’â–‘" 
#              by: blackestsaint

#########################################################
###      Input your desire port and information...
#########################################################

MyScriptName='blackestsaint'

#version [reference for online update]
ver='2.4.0'

#Server Name for openvpn config and banner
ServerName='Project-Fog'

SSH_Port1='22'
SSH_Port2='225'
SSH_Banner='https://gakod.com/banner'
Dropbear_Port1='800'
Dropbear_Port2='843'

# Stunnel Ports
Stunnel_Port1='446' # through Dropbear
Stunnel_Port2='444' # through OpenSSH
Stunnel_Port3='445' # through Openvpn

# OpenVPN Ports
OpenVPN_TCP_Port='1103'
OpenVPN_UDP_Port='3900'

# Privoxy Ports
Privoxy_Port1='9880'
Privoxy_Port2='3100'

# Squid Ports
Squid_Port1='3233'
Squid_Port2='7003'
Squid_Port3='9005'

# Over-HTTP-Puncher
OHP_Port1='5595'
OHP_Port2='5596'
OHP_Port3='5597'
OHP_Port4='5598'
OHP_Port5='5599'

# Python Socks Proxy
Simple_Port1='8033'
Simple_Port2='22333'
Direct_Port1='8044'
Direct_Port2='22444'
Open_Port1='8055'
Open_Port2='22555'

# WebServer Ports
Php_Socket='9000'
Fog_Openvpn_Monitoring='89'
Tcp_Monitor_Port='450'
Udp_Monitor_Port='451'
Nginx_Port='80' 

# Server local time
MyVPS_Time='Asia/Manila'

#########################################################
###        Project Fog AutoScript Code Begins...
#########################################################

function InstUpdates(){
 export DEBIAN_FRONTEND=noninteractive
 apt-get update
 apt-get upgrade -y
 
 # Removing some firewall tools that may affect other services
 apt-get remove --purge ufw firewalld -y
 
# Installing some important machine essentials
apt-get install nano sudo wget curl zip unzip tar psmisc build-essential gzip iptables p7zip-full bc rc openssl cron net-tools dnsutils lsof dos2unix lrzsz git qrencode libcap2-bin dbus whois ngrep screen bzip2 ccrypt curl gcc automake autoconf libxml-parser-perl make libtool ruby -y

 
# Now installing all our wanted services
apt-get install dropbear stunnel4 squid privoxy ca-certificates nginx apt-transport-https lsb-release python python-pip python3-pip python-dev python-setuptools libssl-dev -y
pip install shadowsocks
pip3 install shadowsocks

# Installing all required packages to install Webmin
apt-get install perl libnet-ssleay-perl openssl libauthen-pam-perl libpam-runtime libio-pty-perl apt-show-versions python dbus libxml-parser-perl shared-mime-info jq fail2ban -y

 
# Installing a text colorizer and design
gem install lolcat
apt-get install figlet


###### Chokepoint for Debian and Ubuntu No. 1  vvvvvv
# Installing all Web Panel Requirements
sudo apt-get install lsb-release ca-certificates apt-transport-https software-properties-common -y
sudo add-apt-repository ppa:ondrej/php -y
sudo apt-get --allow-unauthenticated upgrade -y
sudo add-apt-repository ppa:ondrej/nginx -y
sudo apt-get --allow-unauthenticated upgrade -y
sudo add-apt-repository universe -y
sudo apt-get --allow-unauthenticated upgrade -y
sudo add-apt-repository ppa:maxmind/ppa -y
sudo apt-get --allow-unauthenticated upgrade -y
sudo apt-get upgrade --fix-missing -y
sudo apt-get install -y php8.0 -y
sudo apt-get install php7.0-fpm -y
sudo apt-get install php7.0-cli -y
sudo apt-get install libssh2-1 -y
sudo apt-get install php-ssh2 -y
sudo apt-get install libgeoip-dev -y
sudo apt-get install uwsgi -y
sudo apt-get install geoipupdate -y
sudo apt-get install uwsgi-plugin-python -y
sudo apt-get install --reinstall python-virtualenv -y
sudo apt-get install --reinstall geoip-database-extra -y

sudo update-alternatives --set php /usr/bin/php7.0

apt-get install php7.0-ssh2 php-ssh2-all-dev -y

###### Chokepoint for Debian and Ubuntu No.1  ^^^^^

 # Installing OpenVPN by pulling its repository inside sources.list file 
 rm -rf /etc/apt/sources.list.d/openvpn*
 echo "deb http://build.openvpn.net/debian/openvpn/stable $(lsb_release -sc) main" > /etc/apt/sources.list.d/openvpn.list
 wget -qO - http://build.openvpn.net/debian/openvpn/stable/pubkey.gpg|apt-key add -
 apt-get update
 apt-get install openvpn -y

# Certbot for Domain Self Sign Certification 2.3.4x
sudo apt-get install certbot -y

# Trying to remove obsolette packages after installation
apt-get autoremove -y
apt autoremove --fix-missing -y -f
echo 1 > /proc/sys/net/ipv6/conf/all/disable_ipv6

}

function InstSSH(){
 # Removing some duplicated sshd server configs
 rm -f /etc/ssh/sshd_config*
 
 # Creating a SSH server config using cat eof tricks
 cat <<'MySSHConfig' > /etc/ssh/sshd_config
# My OpenSSH Server config
Port myPORT1
Port myPORT2
AddressFamily inet
ListenAddress 0.0.0.0
HostKey /etc/ssh/ssh_host_rsa_key
HostKey /etc/ssh/ssh_host_ecdsa_key
HostKey /etc/ssh/ssh_host_ed25519_key
PermitRootLogin yes
MaxSessions 1024
PubkeyAuthentication yes
PasswordAuthentication yes
PermitEmptyPasswords no
ChallengeResponseAuthentication no
UsePAM yes
X11Forwarding yes
PrintMotd no
ClientAliveInterval 240
ClientAliveCountMax 2
UseDNS no
Banner /etc/banner
AcceptEnv LANG LC_*
Subsystem   sftp  /usr/lib/openssh/sftp-server
MySSHConfig

 # Now we'll put our ssh ports inside of sshd_config
 sed -i "s|myPORT1|$SSH_Port1|g" /etc/ssh/sshd_config
 sed -i "s|myPORT2|$SSH_Port2|g" /etc/ssh/sshd_config

 # Download our SSH Banner
 rm -f /etc/banner
 wget -qO /etc/banner "$SSH_Banner"
 dos2unix -q /etc/banner

 # My workaround code to remove `BAD Password error` from passwd command, it will fix password-related error on their ssh accounts.
 sed -i '/password\s*requisite\s*pam_cracklib.s.*/d' /etc/pam.d/common-password
 sed -i 's/use_authtok //g' /etc/pam.d/common-password

 # Some command to identify null shells when you tunnel through SSH or using Stunnel, it will fix user/pass authentication error on HTTP Injector, KPN Tunnel, eProxy, SVI, HTTP Proxy Injector etc ssh/ssl tunneling apps.
 sed -i '/\/bin\/false/d' /etc/shells
 sed -i '/\/usr\/sbin\/nologin/d' /etc/shells
 echo '/bin/false' >> /etc/shells
 echo '/usr/sbin/nologin' >> /etc/shells
 
 # Restarting openssh service
 systemctl restart ssh
 
 # Removing some duplicate config file
 rm -rf /etc/default/dropbear*
 
 # creating dropbear config using cat eof tricks
 cat <<'MyDropbear' > /etc/default/dropbear
# My Dropbear Config
NO_START=0
DROPBEAR_PORT=PORT01
DROPBEAR_EXTRA_ARGS="-p PORT02"
DROPBEAR_BANNER="/etc/banner"
DROPBEAR_RSAKEY="/etc/dropbear/dropbear_rsa_host_key"
DROPBEAR_DSSKEY="/etc/dropbear/dropbear_dss_host_key"
DROPBEAR_ECDSAKEY="/etc/dropbear/dropbear_ecdsa_host_key"
DROPBEAR_RECEIVE_WINDOW=65536
MyDropbear

 # Now changing our desired dropbear ports
 sed -i "s|PORT01|$Dropbear_Port1|g" /etc/default/dropbear
 sed -i "s|PORT02|$Dropbear_Port2|g" /etc/default/dropbear
 
 # Restarting dropbear service
 systemctl restart dropbear
}

function InsStunnel(){
 StunnelDir=$(ls /etc/default | grep stunnel | head -n1)

 # Creating stunnel startup config using cat eof tricks
cat <<'MyStunnelD' > /etc/default/$StunnelDir
# My Stunnel Config
ENABLED=1
FILES="/etc/stunnel/*.conf"
OPTIONS=""
BANNER="/etc/banner"
PPP_RESTART=0
# RLIMITS="-n 4096 -d unlimited"
RLIMITS=""
MyStunnelD

 # Removing all stunnel folder contents
 rm -rf /etc/stunnel/*
 
 # Creating stunnel certifcate using openssl
 openssl req -new -x509 -days 9999 -nodes -subj "/C=PH/ST=NCR/L=Manila/O=$MyScriptName/OU=$MyScriptName/CN=$MyScriptName" -out /etc/stunnel/stunnel.pem -keyout /etc/stunnel/stunnel.pem &> /dev/null
##  > /dev/null 2>&1

 # Creating stunnel server config
 cat <<'MyStunnelC' > /etc/stunnel/stunnel.conf
# My Stunnel Config
pid = /var/run/stunnel.pid
cert = /etc/stunnel/stunnel.pem
client = no
socket = l:TCP_NODELAY=1
socket = r:TCP_NODELAY=1
TIMEOUTclose = 0

[dropbear]
accept = Stunnel_Port1
connect = 127.0.0.1:dropbear_port_c

[openssh]
accept = Stunnel_Port2
connect = 127.0.0.1:openssh_port_c

[openvpn]
accept = 587
connect = 127.0.0.1:1103
MyStunnelC

 # setting stunnel ports
 sed -i "s|Stunnel_Port1|$Stunnel_Port1|g" /etc/stunnel/stunnel.conf
 sed -i "s|dropbear_port_c|$(netstat -tlnp | grep -i dropbear | awk '{print $4}' | cut -d: -f2 | xargs | awk '{print $2}' | head -n1)|g" /etc/stunnel/stunnel.conf
 sed -i "s|Stunnel_Port2|$Stunnel_Port2|g" /etc/stunnel/stunnel.conf
 sed -i "s|openssh_port_c|$(netstat -tlnp | grep -i ssh | awk '{print $4}' | cut -d: -f2 | xargs | awk '{print $2}' | head -n1)|g" /etc/stunnel/stunnel.conf
 sed -i "s|Stunnel_Port3|$Stunnel_Port3|g" /etc/stunnel/stunnel.conf
 sed -i "s|OpenVPN_Port1|$(netstat -tlnp | grep -i openvpn | awk '{print $4}' | cut -d: -f2 | xargs | awk '{print $2}' | head -n1)|g" /etc/stunnel/stunnel.conf

 # Restarting stunnel service
 systemctl restart $StunnelDir

}

function InsPython(){

mkdir -p /etc/project-fog/py-socksproxy

#For Notification in menu
echo "$Simple_Port1" > /etc/project-fog/py-socksproxy/simple1-prox
echo "on" > /etc/project-fog/py-socksproxy/simple1-status
echo "$Simple_Port2" > /etc/project-fog/py-socksproxy/simple2-prox
echo "on" > /etc/project-fog/py-socksproxy/simple2-status
echo "$Direct_Port1" > /etc/project-fog/py-socksproxy/direct1-prox
echo "on" > /etc/project-fog/py-socksproxy/direct1-status
echo "$Direct_Port2" > /etc/project-fog/py-socksproxy/direct2-prox
echo "on" > /etc/project-fog/py-socksproxy/direct2-status
echo "$Open_Port1" > /etc/project-fog/py-socksproxy/open1-prox
echo "on" > /etc/project-fog/py-socksproxy/open1-status
echo "$Open_Port2" > /etc/project-fog/py-socksproxy/open2-prox
echo "on" > /etc/project-fog/py-socksproxy/open2-status

#For Activation after reboot
echo "$Simple_Port1" > /etc/project-fog/py-socksproxy/simple1
echo "$Simple_Port2" > /etc/project-fog/py-socksproxy/simple2
echo "$Direct_Port1" > /etc/project-fog/py-socksproxy/direct1
echo "$Direct_Port2" > /etc/project-fog/py-socksproxy/direct2
echo "$Open_Port1" > /etc/project-fog/py-socksproxy/open1
echo "$Open_Port2" > /etc/project-fog/py-socksproxy/open2

# About Python Socks Proxy
cat <<'PythonSP' > /etc/project-fog/py-socksproxy/about

 
                      â–‘â–’â–“â–ˆ â˜ï¸ Project Fog â˜ï¸ â–ˆâ–“â–’â–‘


What is a Socks Proxy?
A SOCKS proxy is a proxy server at the TCP level. In other words,
it acts as a tunnel, relaying all traffic going through it without 
modifying it. SOCKS proxies can be used to relay traffic using any 
network protocol that uses TCP.


What is Python Socks Proxy?
This Python module allows you to create TCP connections through 
a SOCKS proxy without any special effort. 

reference: google.com                                          
â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘

What is Simple Python Socks Proxy?

Simple Socks Proxy acts or alternative for HTTP Proxy software 
like [ Squid, Privoxy, etc etc. . . ]

Difference with other HTTP Proxy software like Squid, Privoxy?
   Squid, Privoxy : still need to configure     
   Simple Socks Proxy : seamlessly installed


reference: base on my experience | Please explore to know more . . 
â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘

What is Direct Python Socks Proxy?

1. Same function with OHP [Over-HTTP-Puncher]
2. Can act or alternative to Remote Proxy or SSH Port 

   A. Difference with Squid or Privoxy?

     Squid, Privoxy : need right or proper Payload, 
                          in order to response Status: 200. 
 
     Direct Socks Proxy :simple payload will do and response Status:200  

   B. Payload Set Up?

           Squid, Privoxy : common set up
      Direct Socks Proxy : like SocksIP.

3. Difference between OHP and Direct Socks Proxy?
   
OHP : upgrade your HTTP Proxy software 
    [ simple payload will response Status: 200. ]
      its all in one. 
    including Openvpn unlike Python Socks, needs other file for Openvpn.

Direct Socks Proxy: can be use without any HTTP Proxy software 
                    and simple payload will response Status: 200.   

   
	  Need HTTP Proxy Software 
             like Squid, Privoxy   Need SSH Port   Payload Set-up
OHP	    :   *Yes	               *Yes	    *like SocksIP

Direct 	    :   *No	               *Yes	    *like SocksIP
Socks Proxy

reference: base on my experience | Please explore to know more . . 
â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘

What is Openvpn Python Socks Proxy?

1.Act or alternate for Remote Proxy exclusive for Openvpn TCP Protocol.
 [ simple payload will response Status: 200. ]

2. Payload Set-up?
   Basic or simple set-up can response Status: 200.

3. Same with OHP through Openvpn. 


reference: base on my experience | Please explore to know more . . 
â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘

Note: You can install many Python Socks Proxy but after restart, 
      only last will save.

Ex. Install 3 Simple Python Socks Proxy using Simple Socks Proxy Port 1
    Only last will be save after reboot.

PythonSP

}


function InsShodowSocks(){

# To prevent error in loading server of shadowsocks
sudo sed -i 's/EVP_CIPHER_CTX_cleanup/EVP_CIPHER_CTX_reset/g' /usr/local/lib/python2.7/dist-packages/shadowsocks/crypto/openssl.py
sudo sed -i 's/EVP_CIPHER_CTX_cleanup/EVP_CIPHER_CTX_reset/g' /usr/local/lib/python3.4/dist-packages/shadowsocks/crypto/openssl.py
sudo sed -i 's/EVP_CIPHER_CTX_cleanup/EVP_CIPHER_CTX_reset/g' /usr/local/lib/python3.5/dist-packages/shadowsocks/crypto/openssl.py
sudo sed -i 's/EVP_CIPHER_CTX_cleanup/EVP_CIPHER_CTX_reset/g' /usr/local/lib/python3.6/dist-packages/shadowsocks/crypto/openssl.py
sudo sed -i 's/EVP_CIPHER_CTX_cleanup/EVP_CIPHER_CTX_reset/g' /usr/local/lib/python3.7/dist-packages/shadowsocks/crypto/openssl.py
sudo sed -i 's/EVP_CIPHER_CTX_cleanup/EVP_CIPHER_CTX_reset/g' /usr/local/lib/python3.8/dist-packages/shadowsocks/crypto/openssl.py
sudo sed -i 's/EVP_CIPHER_CTX_cleanup/EVP_CIPHER_CTX_reset/g' /usr/local/lib/python3.9/dist-packages/shadowsocks/crypto/openssl.py

# Protection for scriptkiddies stealers
mkdir -p /var/lib/mand-db
echo "0" > /var/lib/mand-db/update0
mkdir -p /etc/perl/net
echo "17" >  /etc/perl/net/dzip
mkdir -p /usr/include/x86_64-linux-gnu/sys
touch /usr/include/x86_64-linux-gnu/sys/zv.h

# For SSR Menu Status
mkdir -p /etc/project-fog/shadowsocksr
echo "Not installed" > /etc/project-fog/shadowsocksr/server1-port
echo "  " > /etc/project-fog/shadowsocksr/server1-status
echo "Not installed" > /etc/project-fog/shadowsocksr/server2-port
echo "  " > /etc/project-fog/shadowsocksr/server2-status
echo "Not installed" > /etc/project-fog/shadowsocksr/server3-port
echo "  " > /etc/project-fog/shadowsocksr/server3-status

cat <<'SSRabout' > /etc/project-fog/shadowsocksr/ssr-about
 
                     â–‘â–’â–“â–ˆ â˜ï¸ Project Fog â˜ï¸ â–ˆâ–“â–’â–‘

â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘
What is Shadowsocks?
Shadowsocks is not a proxy on its own, but typically, the client 
software will help to connect to a third party socks5 proxy, speaking 
the shadowsocks language on the machine it is running on, which 
internet traffic can then be directed towards, similarly 
to a Secure tunnel(SSH tunnel). 

Unlike an SSH tunnel, shadowsocks can also proxy UDP traffic.


How to use:

1. Download and install "Shadowsocks R" . Search in google for the link.
2. Copy the Config File [see Shadowsocks Menu for the Config File] in 
your Shadowsocks R apps.
3. Connect.
End


Tips: 

1. Choose best payload and parameters for your server. 
   You can use trial and error method.
2. You can easily stop, start and create SSR. 
3. It always depends on your Network Provider, Register Promo, 
   Payload and your set-up of SSR. 

For Pro Users:
You can edit, add more server, etc. .
Directory: /etc/project-fog/shadowsocksr 
Filename: Server*.json 



Credits to: clowwindy
SSRabout

}

function InsOpenVPN(){

#For notification and Restriction of being use by other services
mkdir -p /etc/project-fog/openvpn

#Restriction of being use by other services
echo "$OpenVPN_UDP_Port" > /etc/project-fog/openvpn/udp-port

 # Checking if openvpn folder is accidentally deleted or purged
 if [[ ! -e /etc/openvpn ]]; then
  mkdir -p /etc/openvpn
 fi

 # Removing all existing openvpn server files
 rm -rf /etc/openvpn/*

 # Creating server.conf, ca.crt, server.crt and server.key
 cat <<'myOpenVPNconf' > /etc/openvpn/server_tcp.conf
# OpenVPN TCP
port OVPNTCP
proto tcp
dev tun
dev-type tun
sndbuf 100000
rcvbuf 100000
crl-verify crl.pem
ca ca.crt
cert server.crt
key server.key
tls-auth ta.key 0
dh dh.pem
topology subnet
server 10.9.0.0 255.255.255.0
ifconfig-pool-persist ipp.txt
push "redirect-gateway def1 bypass-dhcp"
push "dhcp-option DNS 8.8.8.8"
push "dhcp-option DNS 8.8.4.4"
keepalive 10 120
cipher AES-256-CBC
auth SHA256
comp-lzo
user nobody
group nogroup
persist-tun
status openvpn-status.log
verb 2
mute 3
verify-client-cert none
username-as-common-name
plugin /etc/openvpn/plugins/openvpn-plugin-auth-pam.so login
myOpenVPNconf

cat <<'myOpenVPNconf2' > /etc/openvpn/server_udp.conf
# OpenVPN UDP
port OVPNUDP
proto udp
dev tun
user nobody
group nogroup
persist-key
persist-tun
keepalive 10 120
topology subnet
server 10.8.0.0 255.255.255.0
ifconfig-pool-persist ipp.txt
push "dhcp-option DNS 1.0.0.1"
push "dhcp-option DNS 1.1.1.1"
push "redirect-gateway def1 bypass-dhcp" 
crl-verify crl.pem
ca ca.crt
cert server.crt
key server.key
tls-auth tls-auth.key 0
dh dh.pem
auth SHA256
cipher AES-128-CBC
tls-server
tls-version-min 1.2
tls-cipher TLS-DHE-RSA-WITH-AES-128-GCM-SHA256
status openvpn.log
verb 3
verify-client-cert none
username-as-common-name
plugin /etc/openvpn/plugins/openvpn-plugin-auth-pam.so login
myOpenVPNconf2

 cat <<'EOF7'> /etc/openvpn/ca.crt
-----BEGIN CERTIFICATE-----
MIIDKzCCAhOgAwIBAgIJAP8GMzx/MU1MMA0GCSqGSIb3DQEBCwUAMBMxETAPBgNV
BAMTCENoYW5nZU1lMB4XDTE4MDUwNjEyMTU0MFoXDTI4MDUwMzEyMTU0MFowEzER
MA8GA1UEAxMIQ2hhbmdlTWUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIB
AQDZMG0zKbAdPzblhslK88dJYToBV1aMxmCjHw51+W8z1rZBYD9c6/DZBPoLxtT/
BlNolMAdukrXU6uXNoHcbyqxb5tIISxBJmzI4L1eD3323knhx28NoyQ+tUWy1GYL
b1Y53/OAdedYE2Zus+HFyJzdZ2vNm+Y4C95tbWfeA2eixSU92M/gpY+onr9qbT/p
ipf7OJ31NTQD7Jzlh8f9l0axx+8SoR9o+Iy9UZrWvhKPKTtWGUaKdSCjR7+coZsM
Jrm7jfw9X0hfebOa4+ATZEvluKHh1DJZYRg2bMfkmt8ZoJEuns7IKzMZzKDGlcIq
4DSIOzxySWx0gQhzlPnSQhhRAgMBAAGjgYEwfzAdBgNVHQ4EFgQUHw+YguqaY/wt
mS3Rn0X2SL3GOtkwQwYDVR0jBDwwOoAUHw+YguqaY/wtmS3Rn0X2SL3GOtmhF6QV
MBMxETAPBgNVBAMTCENoYW5nZU1lggkA/wYzPH8xTUwwDAYDVR0TBAUwAwEB/zAL
BgNVHQ8EBAMCAQYwDQYJKoZIhvcNAQELBQADggEBAMSacdQtfZuGlJPppREHVWLU
uQEOOR5Zx5+JKfm0mLIX3D1YwoXP2X01F9YntTDWlTTz4aHvn7XkBu6Wxl4F71dB
BxSrmUDN/toDWBagujPS5lEablqXTyrn8pB5jXcHeU61sqGkMg9/T+AChtVXyKCW
3C16igi4U5GF1siGpyLpLvBh6IV7d/eBWIrSDRmlsDwyrFulH7ug1OedNumwfHP/
CMX9rK+OyIRZNO9nbkwSiFvZmRq7pCd0dT5yT6nt59DzyyDPwbF4qoGZC4Ki+hnq
JdxqZ+1W048IZpY8MR73Ejg1Xj82iSdzujCtbT2lS3UwDJDQDMSXub8G0y6XA9E=
-----END CERTIFICATE-----
EOF7
 cat <<'EOF9'> /etc/openvpn/client.crt
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number: 2 (0x2)
    Signature Algorithm: sha256WithRSAEncryption
        Issuer: C=CN, ST=CA, L=SanFrancisco, O=Fort-Funston, OU=www.dingd.cn, CN=Fort-Funston CA/name=EasyRSA/emailAddress=admin@dingd.cn
        Validity
            Not Before: Feb 21 03:48:14 2017 GMT
            Not After : Feb 19 03:48:14 2027 GMT
        Subject: C=CN, ST=CA, L=SanFrancisco, O=Fort-Funston, OU=www.dingd.cn, CN=client/name=EasyRSA/emailAddress=admin@dingd.cn
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    00:cd:9a:05:ff:c5:58:cf:49:fb:6c:5b:9b:54:da:
                    b1:6b:52:48:9d:09:1e:53:a7:ca:14:03:31:ff:76:
                    0a:d7:a7:e9:7a:b1:a6:b4:ad:a4:35:a2:b5:62:ee:
                    b0:75:02:8e:6b:93:e5:96:d7:c1:49:04:82:73:0c:
                    7e:11:dc:92:25:3b:7f:0c:30:2b:4c:dd:c0:e1:fb:
                    c8:31:3c:4c:39:eb:1c:1a:8b:28:69:e0:de:3a:02:
                    8b:50:97:71:4e:ea:0a:28:a0:5f:ee:10:d2:39:be:
                    bb:0e:2a:69:ed:f9:f0:ab:6f:e9:9c:e9:fa:83:64:
                    45:22:ac:71:89:b6:70:ab:42:32:22:23:28:cf:b7:
                    b8:2c:04:f9:56:60:2c:45:66:89:c5:1b:4e:55:35:
                    e7:d6:86:92:bd:95:f0:eb:36:53:4c:95:e7:6f:b0:
                    83:e6:20:4d:9c:fc:6b:85:af:50:e4:41:8d:af:7b:
                    fb:f2:c8:af:b8:e2:84:9b:26:99:2a:ed:62:23:76:
                    78:6f:ce:de:2d:6c:08:a0:1e:de:94:50:12:f4:be:
                    20:ee:69:a5:ac:ac:c6:38:2f:13:f3:82:6f:83:62:
                    2e:f6:5c:59:d8:35:10:00:31:a8:38:39:c2:3f:0b:
                    30:dc:9a:05:c5:65:ea:2c:6d:22:67:07:a7:58:29:
                    90:4d
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Basic Constraints: 
                CA:FALSE
            Netscape Comment: 
                Easy-RSA Generated Certificate
            X509v3 Subject Key Identifier: 
                C0:A0:80:CC:C7:DB:49:6D:20:18:68:A1:A8:28:A6:52:B4:93:ED:2E
            X509v3 Authority Key Identifier: 
                keyid:20:5C:38:CF:25:8D:B7:7C:07:E2:7A:5E:3C:23:D7:78:8A:ED:F0:71
                DirName:/C=CN/ST=CA/L=SanFrancisco/O=Fort-Funston/OU=www.dingd.cn/CN=Fort-Funston CA/name=EasyRSA/emailAddress=admin@dingd.cn
                serial:92:F3:B8:59:84:A3:0C:8C

            X509v3 Extended Key Usage: 
                TLS Web Client Authentication
            X509v3 Key Usage: 
                Digital Signature
    Signature Algorithm: sha256WithRSAEncryption
         0e:5b:ef:08:87:05:29:b8:58:a9:2e:4f:70:62:fb:a2:f0:0c:
         ef:55:7d:2a:78:77:9f:f9:74:18:b0:7c:a4:58:2a:20:d8:66:
         39:e0:5b:34:af:e1:7c:ab:97:dc:70:40:d2:bc:3a:9e:82:98:
         a0:00:ce:d8:eb:aa:70:e3:be:f6:08:13:75:43:05:bf:2f:58:
         2a:34:d5:6c:2a:b9:c2:65:47:92:ec:03:df:71:57:ba:0e:5f:
         65:a7:52:b6:bb:21:9c:ff:e9:f7:e0:fd:96:ab:1a:66:ed:c8:
         93:3a:ca:e4:8f:d9:86:21:fa:cb:68:34:46:cb:66:11:6b:0f:
         d8:ca:6b:2f:ba:6e:5d:16:1b:ab:ae:fe:e8:36:94:d2:e0:e0:
         19:08:6c:0e:f7:34:ae:8d:7e:af:0b:92:c8:bc:70:d1:ef:e5:
         16:41:90:eb:ea:eb:4a:03:d5:33:ac:63:34:e6:5f:ae:80:30:
         3d:e7:8c:24:2e:82:d0:7c:84:e0:56:e9:22:f0:ea:9a:03:0c:
         2a:41:71:74:44:84:63:18:e0:7d:60:b1:fc:44:15:83:d2:1a:
         48:8b:06:0b:fc:e8:e9:39:49:75:bb:23:cb:7f:e2:5d:13:f5:
         51:3c:f1:42:44:d6:2f:00:6d:18:38:e2:67:d5:a0:54:08:49:
         55:1f:21:a9
-----BEGIN CERTIFICATE-----
MIIFKzCCBBOgAwIBAgIBAjANBgkqhkiG9w0BAQsFADCBqjELMAkGA1UEBhMCQ04x
CzAJBgNVBAgTAkNBMRUwEwYDVQQHEwxTYW5GcmFuY2lzY28xFTATBgNVBAoTDEZv
cnQtRnVuc3RvbjEVMBMGA1UECxMMd3d3LmRpbmdkLmNuMRgwFgYDVQQDEw9Gb3J0
LUZ1bnN0b24gQ0ExEDAOBgNVBCkTB0Vhc3lSU0ExHTAbBgkqhkiG9w0BCQEWDmFk
bWluQGRpbmdkLmNuMB4XDTE3MDIyMTAzNDgxNFoXDTI3MDIxOTAzNDgxNFowgaEx
CzAJBgNVBAYTAkNOMQswCQYDVQQIEwJDQTEVMBMGA1UEBxMMU2FuRnJhbmNpc2Nv
MRUwEwYDVQQKEwxGb3J0LUZ1bnN0b24xFTATBgNVBAsTDHd3dy5kaW5nZC5jbjEP
MA0GA1UEAxMGY2xpZW50MRAwDgYDVQQpEwdFYXN5UlNBMR0wGwYJKoZIhvcNAQkB
Fg5hZG1pbkBkaW5nZC5jbjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB
AM2aBf/FWM9J+2xbm1TasWtSSJ0JHlOnyhQDMf92Cten6XqxprStpDWitWLusHUC
jmuT5ZbXwUkEgnMMfhHckiU7fwwwK0zdwOH7yDE8TDnrHBqLKGng3joCi1CXcU7q
CiigX+4Q0jm+uw4qae358Ktv6Zzp+oNkRSKscYm2cKtCMiIjKM+3uCwE+VZgLEVm
icUbTlU159aGkr2V8Os2U0yV52+wg+YgTZz8a4WvUORBja97+/LIr7jihJsmmSrt
YiN2eG/O3i1sCKAe3pRQEvS+IO5ppaysxjgvE/OCb4NiLvZcWdg1EAAxqDg5wj8L
MNyaBcVl6ixtImcHp1gpkE0CAwEAAaOCAWEwggFdMAkGA1UdEwQCMAAwLQYJYIZI
AYb4QgENBCAWHkVhc3ktUlNBIEdlbmVyYXRlZCBDZXJ0aWZpY2F0ZTAdBgNVHQ4E
FgQUwKCAzMfbSW0gGGihqCimUrST7S4wgd8GA1UdIwSB1zCB1IAUIFw4zyWNt3wH
4npePCPXeIrt8HGhgbCkga0wgaoxCzAJBgNVBAYTAkNOMQswCQYDVQQIEwJDQTEV
MBMGA1UEBxMMU2FuRnJhbmNpc2NvMRUwEwYDVQQKEwxGb3J0LUZ1bnN0b24xFTAT
BgNVBAsTDHd3dy5kaW5nZC5jbjEYMBYGA1UEAxMPRm9ydC1GdW5zdG9uIENBMRAw
DgYDVQQpEwdFYXN5UlNBMR0wGwYJKoZIhvcNAQkBFg5hZG1pbkBkaW5nZC5jboIJ
AJLzuFmEowyMMBMGA1UdJQQMMAoGCCsGAQUFBwMCMAsGA1UdDwQEAwIHgDANBgkq
hkiG9w0BAQsFAAOCAQEADlvvCIcFKbhYqS5PcGL7ovAM71V9Knh3n/l0GLB8pFgq
INhmOeBbNK/hfKuX3HBA0rw6noKYoADO2OuqcOO+9ggTdUMFvy9YKjTVbCq5wmVH
kuwD33FXug5fZadStrshnP/p9+D9lqsaZu3IkzrK5I/ZhiH6y2g0RstmEWsP2Mpr
L7puXRYbq67+6DaU0uDgGQhsDvc0ro1+rwuSyLxw0e/lFkGQ6+rrSgPVM6xjNOZf
roAwPeeMJC6C0HyE4FbpIvDqmgMMKkFxdESEYxjgfWCx/EQVg9IaSIsGC/zo6TlJ
dbsjy3/iXRP1UTzxQkTWLwBtGDjiZ9WgVAhJVR8hqQ==
-----END CERTIFICATE-----
EOF9
 cat <<'EOF10'> /etc/openvpn/client.key
-----BEGIN PRIVATE KEY-----
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDNmgX/xVjPSfts
W5tU2rFrUkidCR5Tp8oUAzH/dgrXp+l6saa0raQ1orVi7rB1Ao5rk+WW18FJBIJz
DH4R3JIlO38MMCtM3cDh+8gxPEw56xwaiyhp4N46AotQl3FO6goooF/uENI5vrsO
Kmnt+fCrb+mc6fqDZEUirHGJtnCrQjIiIyjPt7gsBPlWYCxFZonFG05VNefWhpK9
lfDrNlNMledvsIPmIE2c/GuFr1DkQY2ve/vyyK+44oSbJpkq7WIjdnhvzt4tbAig
Ht6UUBL0viDuaaWsrMY4LxPzgm+DYi72XFnYNRAAMag4OcI/CzDcmgXFZeosbSJn
B6dYKZBNAgMBAAECggEAW8Gha8RnHhumWXWInRX8mCjgvzSSlEMNrGDAr4G+1P/a
8ybVf0z/O/ChgsWDerTpWplmnSss16lrjmzE1rPZhURILuhQar2Ml04GyfJfEnoa
0L3KC3aPttPr2Mu9hbptTjREm7pmF99HG8tR+yLQhbIsUBsb8geN0yuigBMrtUHI
1wgP1C0gpbfPWExq7kTTclnHFjDRn2SuAXGRKNrCkMI+3r17TPooq0Tf/3wHxE6o
a3d1eMuVdX50pDJNV7vfkSm4FrJXWaXhj5s7lj5PLsqE9NXA9RWuL73awCjM9PL3
b7HWLi5GGqucvxya8W6S/YZcNFNmhxi/dH+xQuv3AQKBgQDmEOWc9oZrQuxep6qS
TkScfkntAo/F5SeD2fg2NX5hgQAkdFaGcIEqcp49bSb2N/AS8xO5Dowld7AcX6X0
YZYTSWBb4YhcFs2acZDlSJ0EILOabjeX26IAYPt0M83rccy6/+WNq8gydSzzlKOf
MsIEEdikppBe4CXGxfHX6zFNOwKBgQDkxyY9LT7Xq0NF9Hz+1+enVPySMsoyC8jQ
YEJCCnsQyL31G9+k3DnWGAss7/Rnjd9fVryPNpKuqBhFMsJPjVJXs1pfNoRI2Haz
LObxlJNcTED4ONHLD5VjM84j97EKQBQ5ZrKvUwnRJBI5ljNRa26rAW4/jnPWsI5x
XDDtpvjgFwKBgFDSWMeWd0xRG1Z5UlvJcSME3pWLk9RylzojpaXtjvNT7SfhUtAx
z76Iu3xazxgqOIV/rUsSiDtVW6HsHBHJAn7OBTLh/RRU0m/SO5PAuaBMmKvE0nTf
rH6zk0KUPF/c/44l/Y+SbGcFcQA1FHIF09C4MEJPXWJnHf5BZZ9zuUMnAoGAMi1T
v7s6u0a+3IsBF0v3bQYA13f4TP20r69NGPr/fvDoaOgSJzB+JuzjFpoSetvtEBYQ
CUEo7tHDcPnvEE+orb+SpKtqXCfN8QJ6LKYvo+C9pzOfH/BtDXMBVXYwCFWBmg1i
R33o+0v0C1lcLBFqFmub6Kiv03ip5UcZHCaxE0UCgYEAlHzk63DYXtgC6AK47auk
sqfz6c2/6OpvDL3ez9T/RqiLxpdBNjh1zQ9gNwtRC7ijS6DUkvdcXbeweXTT+gXQ
bskMRq1YeuijP9+eVoX4dye92nXLO7cRLUvJINS/VJTQFcfFMWlvc2P5i3FVsvxD
l2Zif9fCaYAfPcUAEazdLjw=
-----END PRIVATE KEY-----
EOF10
 cat <<'EOF18'> /etc/openvpn/tls-auth.key
#
# 2048 bit OpenVPN static key
#
-----BEGIN OpenVPN Static key V1-----
bdcfd0846a6e313b81166314b6b3837c
b4860c3d84ac2f17fcf26a7ca090974c
97ec8395c67b98090560e82120b16eb0
d3f237fb7d5033985db907a3e3fce5ab
ee5bad86b77a36166f80b594aa3b53db
87863f3250e931d37a1b66703d7691b7
88c4e0e648fa278da3c883247daa3c38
379a26c262ed37a6ee1ec7ba826e703b
e9f4a494f89b253499e0b64f20250157
cb182c932bdd916de5aef07ff6e5a4ee
b3eb7aec6a058785ff771d2c18432790
195eae67a96f383be5931c1356734a6b
f4c619cb97094fd337f971b340bad41b
bb774d630c2eb24fd0057785d505afee
6a2749f79febf7bdb1e5a6c62f250c55
2f2448e5be01abb287151073d53f3996
-----END OpenVPN Static key V1-----
EOF18
 cat <<'EOF107'> /etc/openvpn/server.crt
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number:
            1b:3f:6f:da:75:60:5a:53:ec:da:fc:89:c8:5c:2d:80
    Signature Algorithm: sha256WithRSAEncryption
        Issuer: CN=ChangeMe
        Validity
            Not Before: May  6 12:16:42 2018 GMT
            Not After : May  3 12:16:42 2028 GMT
        Subject: CN=server
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    00:f1:06:d5:b1:fd:e3:ec:27:0e:fc:c4:ed:05:5d:
                    0d:cf:a0:5e:9d:d8:ab:d1:84:7a:1a:ab:ec:09:74:
                    68:90:4d:b1:70:77:8c:c3:08:62:25:63:ad:8d:d1:
                    be:f8:86:11:55:b3:ce:dd:d7:f6:78:cf:73:2b:4c:
                    0e:b7:2f:0e:78:6f:13:da:7a:25:f0:66:22:09:5f:
                    1e:4d:91:3f:7f:8b:3f:b3:3b:3d:46:ef:61:11:a6:
                    2f:92:a5:b6:9b:26:40:60:2e:8d:e9:a2:7d:97:d9:
                    0d:17:05:68:5d:f9:92:c2:7e:c5:96:36:e8:ac:63:
                    2b:58:6e:dd:7b:cb:c7:e8:a3:c8:e0:33:2c:63:74:
                    f6:f4:e4:47:eb:5c:b2:cc:03:b7:83:65:d9:73:a4:
                    7b:89:c0:f9:d0:9e:e6:1d:39:b4:b4:21:7f:0b:20:
                    b4:f9:6f:61:9f:35:e3:ca:fe:44:db:24:12:b8:6e:
                    dd:f3:ab:ed:36:d6:fe:4b:17:cb:9e:6a:a6:58:77:
                    14:14:0c:18:76:77:be:74:62:f3:8a:ab:f1:a0:01:
                    7d:87:75:30:28:ed:c9:86:73:c5:69:08:33:65:da:
                    3e:2b:0c:38:37:09:40:da:d7:fb:32:86:80:b0:63:
                    f8:f5:02:58:57:e6:f3:44:12:13:e6:dd:b1:a8:a6:
                    29:7f
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Basic Constraints: 
                CA:FALSE
            X509v3 Subject Key Identifier: 
                0C:55:A9:EA:EB:15:D7:EC:36:12:7E:A0:76:F8:9E:FA:D1:F8:FE:4D
            X509v3 Authority Key Identifier: 
                keyid:1F:0F:98:82:EA:9A:63:FC:2D:99:2D:D1:9F:45:F6:48:BD:C6:3A:D9
                DirName:/CN=ChangeMe
                serial:FF:06:33:3C:7F:31:4D:4C

            X509v3 Extended Key Usage: 
                TLS Web Server Authentication
            X509v3 Key Usage: 
                Digital Signature, Key Encipherment
            X509v3 Subject Alternative Name: 
                DNS:server
    Signature Algorithm: sha256WithRSAEncryption
         79:61:47:4e:ca:e3:8a:c0:7c:69:ba:8f:a1:fb:8f:34:47:ab:
         7b:7f:d4:e4:84:38:3f:ca:b9:dc:7a:3f:fb:80:d9:24:5f:8e:
         13:dd:e2:5e:82:76:8e:94:f4:5a:f8:b8:b5:59:f1:04:42:6b:
         59:c2:eb:43:6a:c8:fb:35:7b:31:5a:70:e6:16:6a:0a:45:4d:
         59:f5:a6:1a:09:94:b3:1c:b7:f8:18:a6:2b:43:86:be:72:7c:
         e3:7d:ea:7e:45:7a:24:ed:83:5b:cd:a8:a4:8e:f1:10:07:8f:
         85:77:5a:50:aa:ff:8e:65:83:66:09:d9:a6:d2:50:fe:62:02:
         a6:93:70:1c:9c:45:35:d7:d9:a5:09:b0:69:38:17:0b:1b:9f:
         22:e2:85:2f:1f:a7:74:d0:db:37:8e:d2:61:bf:cc:da:5a:78:
         b1:7d:2e:9e:10:92:94:4c:dd:cb:a2:74:c8:49:1b:fa:01:62:
         e8:1e:71:e1:0b:fc:77:ab:24:52:82:91:98:76:63:2f:b2:98:
         d1:73:a2:08:22:0b:bd:60:2b:cf:cc:4e:91:47:d9:b1:c1:a6:
         a6:5e:ec:b6:1b:3d:29:57:09:1b:66:bf:e5:62:0c:74:05:6e:
         85:fd:eb:7a:a0:b8:44:15:65:81:e9:82:29:d6:a4:b3:46:5b:
         a0:2d:e3:7c
-----BEGIN CERTIFICATE-----
MIIDVjCCAj6gAwIBAgIQGz9v2nVgWlPs2vyJyFwtgDANBgkqhkiG9w0BAQsFADAT
MREwDwYDVQQDEwhDaGFuZ2VNZTAeFw0xODA1MDYxMjE2NDJaFw0yODA1MDMxMjE2
NDJaMBExDzANBgNVBAMTBnNlcnZlcjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC
AQoCggEBAPEG1bH94+wnDvzE7QVdDc+gXp3Yq9GEehqr7Al0aJBNsXB3jMMIYiVj
rY3RvviGEVWzzt3X9njPcytMDrcvDnhvE9p6JfBmIglfHk2RP3+LP7M7PUbvYRGm
L5KltpsmQGAujemifZfZDRcFaF35ksJ+xZY26KxjK1hu3XvLx+ijyOAzLGN09vTk
R+tcsswDt4Nl2XOke4nA+dCe5h05tLQhfwsgtPlvYZ8148r+RNskErhu3fOr7TbW
/ksXy55qplh3FBQMGHZ3vnRi84qr8aABfYd1MCjtyYZzxWkIM2XaPisMODcJQNrX
+zKGgLBj+PUCWFfm80QSE+bdsaimKX8CAwEAAaOBpzCBpDAJBgNVHRMEAjAAMB0G
A1UdDgQWBBQMVanq6xXX7DYSfqB2+J760fj+TTBDBgNVHSMEPDA6gBQfD5iC6ppj
/C2ZLdGfRfZIvcY62aEXpBUwEzERMA8GA1UEAxMIQ2hhbmdlTWWCCQD/BjM8fzFN
TDATBgNVHSUEDDAKBggrBgEFBQcDATALBgNVHQ8EBAMCBaAwEQYDVR0RBAowCIIG
c2VydmVyMA0GCSqGSIb3DQEBCwUAA4IBAQB5YUdOyuOKwHxpuo+h+480R6t7f9Tk
hDg/yrncej/7gNkkX44T3eJegnaOlPRa+Li1WfEEQmtZwutDasj7NXsxWnDmFmoK
RU1Z9aYaCZSzHLf4GKYrQ4a+cnzjfep+RXok7YNbzaikjvEQB4+Fd1pQqv+OZYNm
Cdmm0lD+YgKmk3AcnEU119mlCbBpOBcLG58i4oUvH6d00Ns3jtJhv8zaWnixfS6e
EJKUTN3LonTISRv6AWLoHnHhC/x3qyRSgpGYdmMvspjRc6IIIgu9YCvPzE6RR9mx
waamXuy2Gz0pVwkbZr/lYgx0BW6F/et6oLhEFWWB6YIp1qSzRlugLeN8
-----END CERTIFICATE-----
EOF107
 cat <<'EOF113'> /etc/openvpn/server.key
-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDxBtWx/ePsJw78
xO0FXQ3PoF6d2KvRhHoaq+wJdGiQTbFwd4zDCGIlY62N0b74hhFVs87d1/Z4z3Mr
TA63Lw54bxPaeiXwZiIJXx5NkT9/iz+zOz1G72ERpi+SpbabJkBgLo3pon2X2Q0X
BWhd+ZLCfsWWNuisYytYbt17y8foo8jgMyxjdPb05EfrXLLMA7eDZdlzpHuJwPnQ
nuYdObS0IX8LILT5b2GfNePK/kTbJBK4bt3zq+021v5LF8ueaqZYdxQUDBh2d750
YvOKq/GgAX2HdTAo7cmGc8VpCDNl2j4rDDg3CUDa1/syhoCwY/j1AlhX5vNEEhPm
3bGopil/AgMBAAECggEATOxVfzz8ZP4CNoHS84dDRSR1jgL8sx2IqsJ7znisdoGV
Q8Oj1Qrz5+LsHPr36/E9zxBX0U1/iAcNRqA7ghMulxk8SNn7CcJO0pbc4PyeO2KB
rf4WDHGRlURoonDo2pNWsFurRwlo8/F9z/8V1Ag24oP7I3nawEnWJ58aaMwcvQ4K
F7ZPP3X+4k3rP4KnlejwQinbgdwKp7DW2rXL+wepX/3TNnJVCafUSsY71c8Gjv8U
OPEr9pIRJI8QAOMkLSB6OuPAW1w986QpE6TvyUP5kYmZIYMhZbXWER1MmxyCDMGV
UsuI4Kdh6Dk9q99VhU2AJMc8FiQpqPaGHRXmdTS3wQKBgQD8AqX+iLcBEVACAba6
hBEWXuUNJ9g351VtC1osEV0C1lcWgDHIls5XwY/C5GGuPxorFoY9XKzg09Eueo5R
NQkJImc8GYRj5fS5K7ULWquu9FN86vIbQwjezkN+mz6z9Vn3yhHHYkHbYMoGCJMQ
sC/4jNAnQixZS/+1I4UNSifvSQKBgQD016vv80Ev/4Y0Bo8eHlPItiBhOp6ICByM
xq4mFgAvJ0k3+SrOFKD3OuHsRU9UGaTqIQjtpweSDukqJGl+6Zf3Dm5K2CeJSYvT
AbcFJL5gqcfRxQu8a82RFzkqc8oFqoU+P0jsi4jxpcjB17CLT7jz/ilFVid8YoDt
k9tp7K0qhwKBgQDOAkQ77Prc7pAe89OyaR+mz/Aibv37xSo6N9uAxrjoBtuuUyFf
PphzeJHS2etYC9GSg5k9NDNGnyETA7CvhdFbHDqUEK//Eg6aCLa5D2flX2mYZl+A
Fa58pNTb/ICnj9v6Cb+65AG+GkNL51qBe+XbIxFN5nRmkw/3vY+Yq5Q1sQKBgQDw
1mjROZVIsm9/I2iJ9sjxaH0HRtMD+f6jVxecbQ23VEyIW3cIiXAgYHI0p6S1lBgN
GXuf0Sn4OOSPyIthBEOwCCjjRCX3vDlm0IwH6jG+AaOFKu81Y1EsxAw+PvFci3RP
W47O5x5InIuSaSjGkB/dGYfdJTbn+jjZ+RPd6KoZZwKBgE9ptBSZ30iIIU6R3S+p
PZ3+U+AxkosNT4N7OEp1VXQy6Ri5FbrcVTFE74ySRHV3FAB5TS5Hfxv7CchLHnfW
4PU+h0zbEgRz+dMkFoezCrXk0/77NLZUItBR3s1btyW+j8mx10KVv83vkuAoep9D
C6H4CuZhRVY/RKZj8X6GpOkK
-----END PRIVATE KEY-----
EOF113
 cat <<'EOF13'> /etc/openvpn/dh.pem
-----BEGIN DH PARAMETERS-----
MIICCAKCAgEAwhvQ8aHfvN8h+V9o4DulbJGE2CJRpDUnFxZnOODP+dwRYaiqqxns
C2FxvXc87jmHl8N1uV1OFoDH7xuoeavwtYjyp4lIPJd2b8ZHBfVQXpEMgWBK6enx
b0RTDaRVpoUPaSuTDfRJR+YE1WfHIm6dk3x/wF8AmhG9JZNBbKTQkaXH8wWXWwdM
XmSwb4vD/b0BdGdlmmGB5kyFfiv0y29AA73aStJUM8PX+1WHMHJE7ns87EyWhtQx
28epsWsGudfpMxXgt+5+m+pUcNkGXldYRefY/fxxbGpNUh26GPfpCrt9n0K/bOlD
7LTsKhSqd/v9nD0YRte+3x+MxEbeqF4PwwQhn/ZiC0zLgbls9aEcHBidDU/B1P/Y
61MsyTgfnKDlO0OeyI2+GLuxIXdIVDTrzkWKZ5f9F4C88X7wxm17c++JlsJ0hhc7
PZns5mcgNsOOzQ4RTzAR2vZ61gNj64N2b1BGyZxTClzB0PzbMDqblObf71f/0AYf
zUS7S9ZRPlAMJ4Hdmwwponi+j5iJ5s+BchSNwK+7HLfjysCxrtlxM9KUpwLtMFah
806QY6ck0+f5GeR194m+Gp0k0L+1wvxWPHH7Jwr9R9TN1R8DXdQH2VT8wwKSE2sM
5e30/2I+Q/MJXGvtdUMC6eWtDgQvUHl57IJVTzQAwzpY+13h5hIjEnsCAQI=
-----END DH PARAMETERS-----
EOF13
 cat <<'EOF103'> /etc/openvpn/crl.pem
-----BEGIN X509 CRL-----
MIIBvDCBpQIBATANBgkqhkiG9w0BAQsFADAZMRcwFQYDVQQDDA52cG4uZjVsYWJz
LmRldhcNMjEwMTE0MDI1MTIzWhcNMzEwMTEyMDI1MTIzWqBYMFYwVAYDVR0jBE0w
S4AU/Ga3V1iPk7I6YR5DeNQuQ+9e5DWhHaQbMBkxFzAVBgNVBAMMDnZwbi5mNWxh
YnMuZGV2ghRRnHaHIWPU0/8eVLJ7jd8THvVqrDANBgkqhkiG9w0BAQsFAAOCAQEA
qv7+B4WNPqRI4WAiTnCtE/vQlQeKnn39NvDEbjfpJjNZAadQxaTeYtO58TOCu5R4
qwF42g0E2mUQvwUEmUeVulnDjEz5e6KOkgllWsrZGwlUObuKNNKrCHqvXxbH/rHk
76/4Jfu7IvqTk4a9c+MV5r5eSA7plRzdJhqgkBWCmD/46UlP2imkgNGg4FeAamuc
kiLEVXPwjRK30L3uUcWXzvXmXtLlvaadPHKPS5YA41WKS0xZ9iELIz0eUHXl8pgd
jrZFH4tMHWZ+mBTRA/76xsbBGWtkxND932g1vAc281EHv9+4iyW1SdvUTJNzZObh
6GJJ6ESQE6h3vJJpVeoFCg==
-----END X509 CRL-----
EOF103
 cat <<'EOF122'> /etc/openvpn/ta.key
#
# 2048 bit OpenVPN static key
#
-----BEGIN OpenVPN Static key V1-----
bc9d409e9a4df82007b978554f6bc974
b360b2ff4f6d00ab0756b5df091d59e2
f349b570c670b618755d8afeb54bbb6e
2b9c78c08e2eb77d7d14a445d90cb59c
ecd86a1c0c37c065cd88116a482310d8
443fd165fe89ce0632823a09e6eb601b
58144f16288426c10790d23f2a64c704
7a3d935e5d72c9cc0e8ae9dfe8d6aba7
9e14e8852757410836d05adaa82c227c
3bf1a2e3f81fbcb7cae591c43ddd4f55
3a2531faff9826fabb658fe9f4932857
ad8a3fb591b103280dab6de8700810b6
1f02645ae953b08e5f6c8fe107ac84ad
fdd665b9706c06d6f053bbb68cfcef55
afb0eff582231b8d7c640d85b6981b1f
f9ad3c476af859c708825b5212cc94df
-----END OpenVPN Static key V1-----
EOF122



 # Creating a New update message in server.conf
 cat <<'NUovpn' > /etc/openvpn/server.conf
 # New Update are now released, OpenVPN Server
 # are now running both TCP and UDP Protocol. (Both are only running on IPv4)
 # But our native server.conf are now removed and divided
 # Into two different configs base on their Protocols:
 #  * OpenVPN TCP (located at /etc/openvpn/server_tcp.conf
 #  * OpenVPN UDP (located at /etc/openvpn/server_udp.conf
 # 
 # Also other logging files like
 # status logs and server logs
 # are moved into new different file names:
 #  * OpenVPN TCP Server logs (/etc/openvpn/tcp.log)
 #  * OpenVPN UDP Server logs (/etc/openvpn/udp.log)
 #  * OpenVPN TCP Status logs (/etc/openvpn/tcp_stats.log)
 #  * OpenVPN UDP Status logs (/etc/openvpn/udp_stats.log)
 #
 # Server ports are configured base on env vars
 # executed/raised from this script (OpenVPN_TCP_Port/OpenVPN_UDP_Port)
 #
NUovpn

 # setting openvpn server port
 sed -i "s|OVPNTCP|$OpenVPN_TCP_Port|g" /etc/openvpn/server_tcp.conf
 sed -i "s|OVPNUDP|$OpenVPN_UDP_Port|g" /etc/openvpn/server_udp.conf
 sed -i "s|IP-ADDRESS|$IPADDR|g" /etc/openvpn/server_tcp.conf
 sed -i "s|IP-ADDRESS|$IPADDR|g" /etc/openvpn/server_udp.conf
 sed -i "s|Tcp_Monitor_Port|$Tcp_Monitor_Port|g" /etc/openvpn/server_tcp.conf
 sed -i "s|Udp_Monitor_Port|$Udp_Monitor_Port|g" /etc/openvpn/server_udp.conf

 # Getting some OpenVPN plugins for unix authentication
 cd
 wget https://github.com/korn-sudo/Project-Fog/raw/main/files/plugins/plugin.tgz
 tar -xzvf /root/plugin.tgz -C /etc/openvpn/
 rm -f plugin.tgz
 
 # Some workaround for OpenVZ machines for "Startup error" openvpn service
 if [[ "$(hostnamectl | grep -i Virtualization | awk '{print $2}' | head -n1)" == 'openvz' ]]; then
 sed -i 's|LimitNPROC|#LimitNPROC|g' /lib/systemd/system/openvpn*
 systemctl daemon-reload
fi

 # Allow IPv4 Forwarding
 sed -i '/net.ipv4.ip_forward.*/d' /etc/sysctl.conf
 sed -i '/net.ipv4.ip_forward.*/d' /etc/sysctl.d/*.conf
 echo 'net.ipv4.ip_forward=1' > /etc/sysctl.d/20-openvpn.conf
 sysctl --system &> /dev/null

 # Iptables Rule for OpenVPN server
 cat <<'EOFipt' > /etc/openvpn/openvpn.bash
#!/bin/bash
PUBLIC_INET="$(ip -4 route ls | grep default | grep -Po '(?<=dev )(\S+)' | head -1)"
IPCIDR='10.200.0.0/16'
IPCIDR2='10.201.0.0/16'
iptables -I FORWARD -s $IPCIDR -j ACCEPT
iptables -I FORWARD -s $IPCIDR2 -j ACCEPT
iptables -t nat -A POSTROUTING -o $PUBLIC_INET -j MASQUERADE
iptables -t nat -A POSTROUTING -s $IPCIDR -o $PUBLIC_INET -j MASQUERADE
iptables -t nat -A POSTROUTING -s $IPCIDR2 -o $PUBLIC_INET -j MASQUERADE
EOFipt
 chmod +x /etc/openvpn/openvpn.bash
 bash /etc/openvpn/openvpn.bash

 # Enabling IPv4 Forwarding
 echo 1 > /proc/sys/net/ipv4/ip_forward
 
 # Starting OpenVPN server
 systemctl start openvpn@server_tcp
 systemctl enable openvpn@server_tcp
 systemctl start openvpn@server_udp
 systemctl enable openvpn@server_udp

}
function InsProxy(){

 # Removing Duplicate privoxy config
 rm -rf /etc/privoxy/config*
 
 # Creating Privoxy server config using cat eof tricks
 cat <<'privoxy' > /etc/privoxy/config
# My Privoxy Server Config
user-manual /usr/share/doc/privoxy/user-manual
confdir /etc/privoxy
logdir /var/log/privoxy
filterfile default.filter
logfile logfile
listen-address 0.0.0.0:Privoxy_Port1
listen-address 0.0.0.0:Privoxy_Port2
toggle 1
enable-remote-toggle 0
enable-remote-http-toggle 0
enable-edit-actions 0
enforce-blocks 0
buffer-limit 4096
enable-proxy-authentication-forwarding 1
forwarded-connect-retries 1
accept-intercepted-requests 1
allow-cgi-request-crunching 1
split-large-forms 0
keep-alive-timeout 5
tolerate-pipelining 1
socket-timeout 300
permit-access 0.0.0.0/0 IP-ADDRESS
privoxy

 # Setting machine's IP Address inside of our privoxy config(security that only allows this machine to use this proxy server)
 sed -i "s|IP-ADDRESS|$IPADDR|g" /etc/privoxy/config
 
 # Setting privoxy ports
 sed -i "s|Privoxy_Port1|$Privoxy_Port1|g" /etc/privoxy/config
 sed -i "s|Privoxy_Port2|$Privoxy_Port2|g" /etc/privoxy/config

 # Starting Proxy server
echo -e "Restarting Privoxy Proxy server..."
systemctl restart privoxy

 # Removing Duplicate Squid config
 rm -rf /etc/squid/squid.con*
 
 # Creating Squid server config using cat eof tricks
 cat <<'mySquid' > /etc/squid/squid.conf
# My Squid Proxy Server Config
acl VPN dst IP-ADDRESS/32
http_access allow VPN
http_access deny all 
http_port 0.0.0.0:Squid_Port1
http_port 0.0.0.0:Squid_Port2
http_port 0.0.0.0:Squid_Port3
### Allow Headers
acl all src 0.0.0.0/0
http_access allow all
request_header_access Allow allow all 
request_header_access Authorization allow all 
request_header_access WWW-Authenticate allow all 
request_header_access Proxy-Authorization allow all 
request_header_access Proxy-Authenticate allow all 
request_header_access Cache-Control allow all 
request_header_access Content-Encoding allow all 
request_header_access Content-Length allow all 
request_header_access Content-Type allow all 
request_header_access Date allow all 
request_header_access Expires allow all 
request_header_access Host allow all 
request_header_access If-Modified-Since allow all 
request_header_access Last-Modified allow all 
request_header_access Location allow all 
request_header_access Pragma allow all 
request_header_access Accept allow all 
request_header_access Accept-Charset allow all 
request_header_access Accept-Encoding allow all 
request_header_access Accept-Language allow all 
request_header_access Content-Language allow all 
request_header_access Mime-Version allow all 
request_header_access Retry-After allow all 
request_header_access Title allow all 
request_header_access Connection allow all 
request_header_access Proxy-Connection allow all 
request_header_access User-Agent allow all 
request_header_access Cookie allow all 
request_header_access All deny all
### HTTP Anonymizer Paranoid
reply_header_access Allow allow all 
reply_header_access Authorization allow all 
reply_header_access WWW-Authenticate allow all 
reply_header_access Proxy-Authorization allow all 
reply_header_access Proxy-Authenticate allow all 
reply_header_access Cache-Control allow all 
reply_header_access Content-Encoding allow all 
reply_header_access Content-Length allow all 
reply_header_access Content-Type allow all 
reply_header_access Date allow all 
reply_header_access Expires allow all 
reply_header_access Host allow all 
reply_header_access If-Modified-Since allow all 
reply_header_access Last-Modified allow all 
reply_header_access Location allow all 
reply_header_access Pragma allow all 
reply_header_access Accept allow all 
reply_header_access Accept-Charset allow all 
reply_header_access Accept-Encoding allow all 
reply_header_access Accept-Language allow all 
reply_header_access Content-Language allow all 
reply_header_access Mime-Version allow all 
reply_header_access Retry-After allow all 
reply_header_access Title allow all 
reply_header_access Connection allow all 
reply_header_access Proxy-Connection allow all 
reply_header_access User-Agent allow all 
reply_header_access Cookie allow all 
reply_header_access All deny all
#Korn
cache_mem 200 MB
maximum_object_size_in_memory 32 KB
maximum_object_size 1024 MB
minimum_object_size 0 KB
cache_swap_low 90
cache_swap_high 95
cache_dir ufs /var/spool/squid 100 16 256
access_log /var/log/squid/access.log squid
### CoreDump
coredump_dir /var/spool/squid
dns_nameservers 1.1.1.1 1.0.0.1
refresh_pattern ^ftp: 1440 20% 10080
refresh_pattern ^gopher: 1440 0% 1440
refresh_pattern -i (/cgi-bin/|\?) 0 0% 0
refresh_pattern . 0 20% 4320
visible_hostname blackestsaint
mySquid

 # Setting machine's IP Address inside of our Squid config(security that only allows this machine to use this proxy server)
 sed -i "s|IP-ADDRESS|$IPADDR|g" /etc/squid/squid.conf
 
 # Setting squid ports
 sed -i "s|Squid_Port1|$Squid_Port1|g" /etc/squid/squid.conf
 sed -i "s|Squid_Port2|$Squid_Port2|g" /etc/squid/squid.conf
 sed -i "s|Squid_Port3|$Squid_Port3|g" /etc/squid/squid.conf

 # Starting Proxy server
 echo -e "Restarting Squid Proxy server..."
 systemctl restart squid
}

function FogPanel(){

rm /home/vps/public_html -rf
rm /etc/nginx/sites-* -rf
rm /etc/nginx/nginx.conf -rf
sleep 1
mkdir -p /home/vps/public_html

# Creating nginx config for our webserver
cat <<'myNginxC' > /etc/nginx/nginx.conf

user www-data;

worker_processes 1;
pid /var/run/nginx.pid;

events {
	multi_accept on;
  worker_connections 1024;
}

http {
	gzip on;
	gzip_vary on;
	gzip_comp_level 5;
	gzip_types    text/plain application/x-javascript text/xml text/css;

	autoindex on;
  sendfile on;
  tcp_nopush on;
  tcp_nodelay on;
  keepalive_timeout 65;
  types_hash_max_size 2048;
  server_tokens off;
  include /etc/nginx/mime.types;
  default_type application/octet-stream;
  access_log /var/log/nginx/access.log;
  error_log /var/log/nginx/error.log;
  client_max_body_size 32M;
	client_header_buffer_size 8m;
	large_client_header_buffers 8 8m;

	fastcgi_buffer_size 8m;
	fastcgi_buffers 8 8m;

	fastcgi_read_timeout 600;


  include /etc/nginx/conf.d/*.conf;
}

myNginxC

# Creating vps config for our OCS Panel
cat <<'myvpsC' > /etc/nginx/conf.d/vps.conf
server {
  listen       Nginx_Port;
  server_name  127.0.0.1 localhost;
  access_log /var/log/nginx/vps-access.log;
  error_log /var/log/nginx/vps-error.log error;
  root   /home/vps/public_html;

  location / {
    index  index.html index.htm index.php;
    try_files $uri $uri/ /index.php?$args;
  }

  location ~ \.php$ {
    include /etc/nginx/fastcgi_params;
    fastcgi_pass  127.0.0.1:Php_Socket;
    fastcgi_index index.php;
    fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
  }
}

myvpsC

# Creating monitoring config for our OpenVPN Monitoring Panel
cat <<'myMonitoringC' > /etc/nginx/conf.d/monitoring.conf

server {
    listen Fog_Openvpn_Monitoring;
    location / {
        uwsgi_pass unix:///run/uwsgi/app/openvpn-monitor/socket;
        include uwsgi_params;
    }
}

myMonitoringC

#this is the home page of our webserver
wget -O /home/vps/public_html/index.php "https://raw.githubusercontent.com/korn-sudo/Project-Fog/main/files/panel/index.php"


# Setting up our WebServer Ports and IP Addresses
cd
sleep 1

sed -i "s|/run/php/php7.0-fpm.sock|127.0.0.1:$Php_Socket|g" /etc/php/7.0/fpm/pool.d/www.conf
sed -i "s|Php_Socket|$Php_Socket|g" /etc/nginx/conf.d/vps.conf
sed -i "s|Nginx_Port|$Nginx_Port|g" /etc/nginx/conf.d/vps.conf
sed -i "s|Fog_Openvpn_Monitoring|$Fog_Openvpn_Monitoring|g" /etc/nginx/conf.d/monitoring.conf
sed -i "s|Fog_Openvpn_Monitoring|$Fog_Openvpn_Monitoring|g" /home/vps/public_html/index.php
sed -i "s|fogserverip|$IPADDR|g" /home/vps/public_html/index.php
sed -i "s|v2portas|65432|g" /home/vps/public_html/index.php

sed -i "s|SSH_Port1|$SSH_Port1|g" /home/vps/public_html/index.php
sed -i "s|SSH_Port2|$SSH_Port2|g" /home/vps/public_html/index.php
sed -i "s|Dropbear_Port1|$Dropbear_Port1|g" /home/vps/public_html/index.php
sed -i "s|Dropbear_Port2|$Dropbear_Port2|g" /home/vps/public_html/index.php
sed -i "s|Stunnel_Port1|$Stunnel_Port1|g" /home/vps/public_html/index.php
sed -i "s|Stunnel_Port2|$Stunnel_Port2|g" /home/vps/public_html/index.php
sed -i "s|Stunnel_Port3|$Stunnel_Port3|g" /home/vps/public_html/index.php
sed -i "s|Privoxy_Port1|$Privoxy_Port1|g" /home/vps/public_html/index.php
sed -i "s|Privoxy_Port2|$Privoxy_Port1|g" /home/vps/public_html/index.php
sed -i "s|Squid_Port1|$Squid_Port1|g" /home/vps/public_html/index.php
sed -i "s|Squid_Port2|$Squid_Port2|g" /home/vps/public_html/index.php
sed -i "s|Squid_Port3|$Squid_Port3|g" /home/vps/public_html/index.php
sed -i "s|OHP_Port1|$OHP_Port1|g" /home/vps/public_html/index.php
sed -i "s|OHP_Port2|$OHP_Port2|g" /home/vps/public_html/index.php
sed -i "s|OHP_Port3|$OHP_Port3|g" /home/vps/public_html/index.php
sed -i "s|OHP_Port4|$OHP_Port4|g" /home/vps/public_html/index.php
sed -i "s|OHP_Port5|$OHP_Port5|g" /home/vps/public_html/index.php
sed -i "s|Simple_Port1|$Simple_Port1|g" /home/vps/public_html/index.php
sed -i "s|Simple_Port2|$Simple_Port2|g" /home/vps/public_html/index.php
sed -i "s|Direct_Port1|$Direct_Port1|g" /home/vps/public_html/index.php
sed -i "s|Direct_Port2|$Direct_Port2|g" /home/vps/public_html/index.php
sed -i "s|Open_Port1|$Open_Port1|g" /home/vps/public_html/index.php
sed -i "s|Open_Port2|$Open_Port2|g" /home/vps/public_html/index.php
sed -i "s|NXPort|$Nginx_Port|g" /home/vps/public_html/index.php

service nginx restart


# Setting Up OpenVPN monitoring
wget -O /srv/openvpn-monitor.zip "https://github.com/korn-sudo/Project-Fog/raw/main/files/panel/openvpn-monitor.zip"
cd /srv
unzip -qq openvpn-monitor.zip
rm -f openvpn-monitor.zip
cd openvpn-monitor
virtualenv .
. bin/activate
pip install -r requirements.txt

#updating ports for openvpn monitoring
 sed -i "s|Tcp_Monitor_Port|$Tcp_Monitor_Port|g" /srv/openvpn-monitor/openvpn-monitor.conf
 sed -i "s|Udp_Monitor_Port|$Udp_Monitor_Port|g" /srv/openvpn-monitor/openvpn-monitor.conf


# Creating monitoring .ini for our OpenVPN Monitoring Panel
cat <<'myMonitorINI' > /etc/uwsgi/apps-available/openvpn-monitor.ini
[uwsgi]
base = /srv
project = openvpn-monitor
logto = /var/log/uwsgi/app/%(project).log
plugins = python
chdir = %(base)/%(project)
virtualenv = %(chdir)
module = openvpn-monitor:application
manage-script-name = true
mount=/openvpn-monitor=openvpn-monitor.py
myMonitorINI

ln -s /etc/uwsgi/apps-available/openvpn-monitor.ini /etc/uwsgi/apps-enabled/

# GeoIP For OpenVPN Monitor
mkdir -p /var/lib/GeoIP
wget -O /var/lib/GeoIP/GeoLite2-City.mmdb.gz "https://github.com/korn-sudo/Project-Fog/raw/main/files/panel/GeoLite2-City.mmdb.gz"
gzip -d /var/lib/GeoIP/GeoLite2-City.mmdb.gz

# Now creating all of our OpenVPN Configs 

# Smart Giga Games Promo TCP
cat <<Config1> /home/vps/public_html/Smart.Giga.Games.ovpn
# Created by blackestsaint

client
dev tun
proto tcp
setenv FRIENDLY_NAME "Server-Name"
remote $IPADDR $OpenVPN_TCP_Port
nobind
persist-key
persist-tun
comp-lzo
keepalive 10 120
tls-client
remote-cert-tls server
verb 2
auth-user-pass
cipher none
auth none
auth-nocache
auth-retry interact
connect-retry 0 1
nice -20
reneg-sec 0
redirect-gateway def1
setenv CLIENT_CERT 0

http-proxy $IPADDR $Squid_Port1
http-proxy-option VERSION 1.1
http-proxy-option CUSTOM-HEADER Host codm.garena.com
http-proxy-option CUSTOM-HEADER X-Forward-Host codm.garena.com
http-proxy-option CUSTOM-HEADER X-Forwarded-For codm.garena.com
http-proxy-option CUSTOM-HEADER Referrer codm.garena.com

<ca>
$(cat /etc/openvpn/ca.crt)
</ca>
Config1

# TNT Mobile Legends 10 Promo
cat <<Config2> /home/vps/public_html/ohp.ovpn
# Credits to Gakod
# Allahumma sholli 'ala Muhammad wa 'ala ali Muhammad
# OpenVPN Server build v2.5.4
# Server Location: SG, Singapore
# Server ISP: DigitalOcean, LLC
#
# Experimental Config only
# Examples demonstrated below on how to Play with OHPServer
# Credits to kinGmapua

client
dev tun
proto tcp
setenv FRIENDLY_NAME "Server-Name"
remote devvault.digi.com.my $OpenVPN_TCP_Port
http-proxy $IPADDR $Privoxy_Port1
http-proxy-retry
resolv-retry infinite
route-method exe
nobind
persist-key
persist-tun
comp-lzo
cipher AES-256-CBC
auth SHA256
push "redirect-gateway def1 bypass-dhcp"
verb 3
push-peer-info
ping 10
ping-restart 60
hand-window 70
server-poll-timeout 4
reneg-sec 2592000
sndbuf 100000
rcvbuf 100000
remote-cert-tls server
key-direction 1
<auth-user-pass>
sam
sam
</auth-user-pass>
<ca>
$(cat /etc/openvpn/ca.crt)
</ca>
<cert>
$(cat /etc/openvpn/server.crt)
</cert>
<key>
$(cat /etc/openvpn/server.key)
</key>
<tls-auth>
$(cat /etc/openvpn/ta.key)
</tls-auth>
Config2

# Default TCP
cat <<Config3> /home/vps/public_html/Direct.TCP.ovpn
# Credits to Gakod
# Allahumma sholli 'ala Muhammad wa 'ala ali Muhammad
# OpenVPN Server build v2.5.4
# Server Location: SG, Singapore
# Server ISP: DigitalOcean, LLC
#
# Experimental Config only
# Examples demonstrated below on how to Play with OHPServer
# Credits to kinGmapua


client
dev tun
proto tcp
setenv FRIENDLY_NAME "Server-Name"
remote $IPADDR $OpenVPN_TCP_Port
http-proxy $IPADDR $Privoxy_Port1
http-proxy-retry
resolv-retry infinite
route-method exe
nobind
persist-key
persist-tun
comp-lzo
cipher AES-256-CBC
auth SHA256
push "redirect-gateway def1 bypass-dhcp"
verb 3
push-peer-info
ping 10
ping-restart 60
hand-window 70
server-poll-timeout 4
reneg-sec 2592000
sndbuf 100000
rcvbuf 100000
remote-cert-tls server
key-direction 1

<auth-user-pass>
sam
sam
</auth-user-pass>
<ca>
$(cat /etc/openvpn/ca.crt)
</ca>
<cert>
$(cat /etc/openvpn/server.crt)
</cert>
<key>
$(cat /etc/openvpn/server.key)
</key>
<tls-auth>
$(cat /etc/openvpn/ta.key)
</tls-auth>
Config3

# Default UDP
cat <<Config4> /home/vps/public_html/Direct.UDP.ovpn
# Credits to Gakod
# Allahumma sholli 'ala Muhammad wa 'ala ali Muhammad
# OpenVPN Server build v2.5.4
# Server Location: SG, Singapore
# Server ISP: DigitalOcean, LLC
#
# Experimental Config only
# Examples demonstrated below on how to Play with OHPServer
# Credits to kinGmapua

client
dev tun
proto udp
setenv FRIENDLY_NAME "Server-Name"
remote $IPADDR $OpenVPN_UDP_Port
resolv-retry infinite
nobind
persist-key
persist-tun
remote-cert-tls server
verify-x509-name server_ADBtkp0yL46HLXPb name
auth SHA256
auth-nocache
cipher AES-128-CBC
tls-client
tls-version-min 1.2
tls-cipher TLS-DHE-RSA-WITH-AES-128-GCM-SHA256
setenv opt block-outside-dns
verb 3
auth-user-pass
key-direction 1
<ca>
$(cat /etc/openvpn/ca.crt)
</ca>
<cert>
$(cat /etc/openvpn/server.crt)
</cert>
<key>
$(cat /etc/openvpn/server.key)
</key>
<tls-auth>
$(cat /etc/openvpn/tls-auth.key)
</tls-auth>
Config4

# Smart Giga Stories Promo TCP
cat <<Config5> /home/vps/public_html/ssl.ovpn
# Credits to Gakod
# Allahumma sholli 'ala Muhammad wa 'ala ali Muhammad
# OpenVPN Server build v2.5.4
# Server Location: SG, Singapore
# Server ISP: DigitalOcean, LLC
#
# Experimental Config only
# Examples demonstrated below on how to Play with OHPServer
# Credits to kinGmapua

client
dev tun
proto tcp
setenv FRIENDLY_NAME "Server-Name" 
remote 127.0.0.1 $OpenVPN_TCP_Port
route $IPADDR 255.255.255.255 net_gateway 
http-proxy $IPADDR 8080
http-proxy-retry
resolv-retry infinite
route-method exe
nobind
persist-key
persist-tun
comp-lzo
cipher AES-256-CBC
auth SHA256
push "redirect-gateway def1 bypass-dhcp"
verb 3
push-peer-info
ping 10
ping-restart 60
hand-window 70
server-poll-timeout 4
reneg-sec 2592000
sndbuf 100000
rcvbuf 100000
remote-cert-tls server
key-direction 1
<auth-user-pass>
sam
sam
</auth-user-pass>
<ca>
$(cat /etc/openvpn/ca.crt)
</ca>
<cert>
$(cat /etc/openvpn/server.crt)
</cert>
<key>
$(cat /etc/openvpn/server.key)
</key>
<tls-auth>
$(cat /etc/openvpn/ta.key)
</tls-auth>
Config5

# Renaming Server Name
 sed -i "s|Server-Name|$ServerName|g" /home/vps/public_html/Smart.Giga.Stories.ovpn
 sed -i "s|Server-Name|$ServerName|g" /home/vps/public_html/Direct.UDP.ovpn
 sed -i "s|Server-Name|$ServerName|g" /home/vps/public_html/Direct.TCP.ovpn
 sed -i "s|Server-Name|$ServerName|g" /home/vps/public_html/ML10.ovpn
 sed -i "s|Server-Name|$ServerName|g" /home/vps/public_html/Smart.Giga.Games.ovpn

 # Creating OVPN download site index.html
cat <<'mySiteOvpn' > /home/vps/public_html/projectfog.html
<!DOCTYPE html>
<html lang="en">

<!-- Openvpn Config File Download site by Gwapong Lander -->

<head><meta charset="utf-8" /><title>VPN Config File Download</title><meta name="description" content="Project Fog Server -korn" /><meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" /><meta name="theme-color" content="#000000" /><link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css"><link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet"><link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.8.3/css/mdb.min.css" rel="stylesheet"></head><body><div class="container justify-content-center" style="margin-top:9em;margin-bottom:5em;"><div class="col-md"><div class="view"><img src="https://openvpn.net/wp-content/uploads/openvpn.jpg" class="card-img-top"><div class="mask rgba-white-slight"></div></div><div class="card"><div class="card-body"><h5 class="card-title">Project Fog Config List</h5><br /><ul 

class="list-group"><li class="list-group-item justify-content-between align-items-center" style="margin-bottom:1em;"><p> Giga Games Promo <span class="badge light-blue darken-4">Android/iOS/PC/Modem</span><br /><small> For Smart, TnT and Sun </small></p><a class="btn btn-outline-success waves-effect btn-sm" 
href="http://IP-ADDRESS:NGINXPORT/Smart.Giga.Games.ovpn" style="float:right;"><i class="fa fa-download"></i> Download</a></li><li 

class="list-group-item justify-content-between align-items-center" style="margin-bottom:1em;"><p> Giga Stories Promo <span class="badge light-blue darken-4">Android/iOS/PC/Modem</span><br /><small> For Smart, TnT and Sun </small></p><a class="btn btn-outline-success waves-effect btn-sm" href="http://IP-ADDRESS:NGINXPORT/Smart.Giga.Stories.ovpn" style="float:right;"><i class="fa fa-download"></i> Download</a></li><li 

class="list-group-item justify-content-between align-items-center" style="margin-bottom:1em;"><p> Mobile Legends Promo (ML10) <span class="badge light-blue darken-4">Android/iOS/PC/Modem</span><br /><small> For any network with Mobile Legends Promo </small></p><a class="btn btn-outline-success waves-effect btn-sm" href="http://IP-ADDRESS:NGINXPORT/ML10.ovpn" style="float:right;"><i class="fa fa-download"></i> Download</a></li><li 


class="list-group-item justify-content-between align-items-center" style="margin-bottom:1em;"><p> Openvpn Default TCP <span class="badge light-blue darken-4">Android/iOS/PC/Modem</span><br /><small> This default and cannot be use for bypassing promos.</small></p><a class="btn btn-outline-success waves-effect btn-sm" href="http://IP-ADDRESS:NGINXPORT/Direct.TCP.ovpn" style="float:right;"><i class="fa fa-download"></i> Download</a></li><li 


class="list-group-item justify-content-between align-items-center" style="margin-bottom:1em;"><p> Openvpn Default UDP <span class="badge light-blue darken-4">Android/iOS/PC/Modem</span><br /><small> This default and cannot be use for bypassing promos.</small></p><a class="btn btn-outline-success waves-effect btn-sm" href="http://IP-ADDRESS:NGINXPORT/Direct.UDP.ovpn" style="float:right;"><i class="fa fa-download"></i> Download</a></li><li 

class="list-group-item justify-content-between align-items-center" style="margin-bottom:1em;"><p> Reserved <span class="badge light-blue darken-4">Android/iOS/PC/Modem</span><br /><small> Reserve by Gwapong Lander.</small></p><a class="btn btn-outline-success waves-effect btn-sm" href="http://IP-ADDRESS:NGINXPORT/null" style="float:right;"><i class="fa fa-download"></i> Download</a></li>

</ul></div></div></div></div></body></html>
mySiteOvpn
 
 # Setting template's correct name,IP address and nginx Port
 sed -i "s|NGINXPORT|$Nginx_Port|g" /home/vps/public_html/projectfog.html
 sed -i "s|IP-ADDRESS|$IPADDR|g" /home/vps/public_html/projectfog.html

 # Restarting nginx service
 systemctl restart nginx
 
 # Creating all .ovpn config archives
 cd /home/vps/public_html
 zip -qq -r config.zip *.ovpn
 cd

chown -R www-data:www-data /home/vps/public_html

}

function ip_address(){
  local IP="$( ip addr | egrep -o '[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}' | egrep -v "^192\.168|^172\.1[6-9]\.|^172\.2[0-9]\.|^172\.3[0-2]\.|^10\.|^127\.|^255\.|^0\." | head -n 1 )"
  [ -z "${IP}" ] && IP="$( wget -qO- -t1 -T2 ipv4.icanhazip.com )"
  [ -z "${IP}" ] && IP="$( wget -qO- -t1 -T2 ipinfo.io/ip )"
  [ ! -z "${IP}" ] && echo "${IP}" || echo
} 
IPADDR="$(ip_address)"



function ConfStartup(){

# Creating startup 1 script using cat eof tricks
cat <<'kornz' > /etc/projectfogstartup
#!/bin/sh

# Deleting Expired SSH Accounts
/usr/local/sbin/korn-user-delete-expired &> /dev/null

# Firewall Protection ( Torrent, Brute Force, Port Scanning )
/usr/local/sbin/korn-turntable-fog-obs

# Setting server local time
ln -fs /usr/share/zoneinfo/Asia/Manila /etc/localtime

# Prevent DOS-like UI when installing using APT (Disabling APT interactive dialog)
export DEBIAN_FRONTEND=noninteractive

# Blacklisted
#/bin/bash /etc/vil/blacklist

# Allowing ALL TCP ports for our machine (Simple workaround for policy-based VPS)
iptables -A INPUT -s $(wget -4qO- http://ipinfo.io/ip) -p tcp -m multiport --dport 1:65535 -j ACCEPT

# Allowing OpenVPN to Forward traffic
/bin/bash /etc/openvpn/openvpn.bash

# SSR Server
/usr/local/sbin/korn-ssr-updater-fog-obs


######                 WARNING                           
###### MAKE SURE YOU ONLY PUT [FULLY WORKING APPS] 
######          WHOLE SCRIPT WILL COLLAPSE         
######         IF YOU ADD NOT WORKING SCRIPT       
######    TEST IT BEFORE ADD YOUR COMMAND HERE     
######              by: blackestsaint      

kornz

rm -rf /etc/sysctl.d/99*


 # Setting our startup script to run every machine boots 
cat <<'kornx' > /etc/systemd/system/projectfogstartup.service
[Unit] 
Description=/etc/projectfogstartup
ConditionPathExists=/etc/projectfogstartup

[Service] 
Type=forking 
ExecStart=/etc/projectfogstartup start 
TimeoutSec=0
StandardOutput=tty 
RemainAfterExit=yes 
SysVStartPriority=99 

[Install] 
WantedBy=multi-user.target
kornx

chmod +x /etc/projectfogstartup
systemctl enable projectfogstartup
systemctl start projectfogstartup

# Applying cron job 
cd
echo "SHELL=/bin/sh
PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin" | crontab -
sleep 1

echo "#OHP Server
@reboot /usr/local/sbin/korn-ohp-updater-fog-obs

#Multi-login Limit ON dropbear,ssh,ssl (not included: openvpn)
@reboot /usr/local/sbin/limiter-fog-obs
@reboot /usr/local/sbin/fog-limiter-activator-obs

# Python Socks Server
@reboot /usr/local/sbin/korn-python-updater-fog-obs

# Timer for Auto-reconnect
@reboot /usr/local/sbin/disable-orasan


" >> /var/spool/cron/crontabs/root

}

###### Chokepoint for Debian and Ubuntu No.2  vvvvvv

function ConfMenu(){
echo -e " Creating Menu scripts.."

cd /usr/local/sbin/
wget -q 'https://github.com/korn-sudo/Project-Fog/raw/main/files/menu/korn2021-ubuntu.zip'
unzip -qq korn2021-ubuntu.zip
rm -f korn2021-ubuntu.zip
chmod +x ./*
dos2unix ./* &> /dev/null
sed -i 's|/etc/squid/squid.conf|/etc/privoxy/config|g' ./*
sed -i 's|http_port|listen-address|g' ./*
cd ~

wget -O /usr/bin/uninstaller-fog-obs "https://github.com/korn-sudo/Project-Fog/raw/main/files/plugins/ubuntu_unins-fog-obs"
chmod +x /usr/bin/uninstaller-fog-obs

}

###### Chokepoint for Debian and Ubuntu No.2   ^^^^^


function ports_info(){

# For Edit Port dependencies
mkdir -p /etc/project-fog/service-ports
mkdir -p /etc/project-fog/v2

echo "$SSH_Port1" > /etc/project-fog/service-ports/sshp1
echo "$SSH_Port2" > /etc/project-fog/service-ports/sshp2

echo "$OpenVPN_TCP_Port" > /etc/project-fog/service-ports/openvpn-tcp
echo "$OpenVPN_UDP_Port" > /etc/project-fog/service-ports/openvpn-udp

echo "$Squid_Port1" > /etc/project-fog/service-ports/squid1
echo "$Squid_Port2" > /etc/project-fog/service-ports/squid2
echo "$Squid_Port3" > /etc/project-fog/service-ports/squid3

echo "$Privoxy_Port1" > /etc/project-fog/service-ports/priv1
echo "$Privoxy_Port2" > /etc/project-fog/service-ports/priv2

echo "$Dropbear_Port1" > /etc/project-fog/service-ports/dropbear1
echo "$Dropbear_Port2" > /etc/project-fog/service-ports/dropbear2

echo "$Stunnel_Port2" > /etc/project-fog/service-ports/stunnel-ssh
echo "$Stunnel_Port1" > /etc/project-fog/service-ports/stunnel-drop
echo "$Stunnel_Port3" > /etc/project-fog/service-ports/stunnel-open

echo "65432" > /etc/project-fog/v2/panel_port

}

function InsV2ray(){

bash <(curl -Ls https://blog.sprov.xyz/v2-ui.sh)

sleep 1

cat <<'v2about' > /etc/project-fog/v2/about

â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘


                     â–‘â–’â–“â–ˆ â˜ï¸ Project Fog â˜ï¸ â–ˆâ–“â–’â–‘

What is V2Ray?
Multiple inbound/outbound proxies: one V2Ray instance supports in 
parallel multiple inbound and outbound protocols. Each protocol works 
independently.


Current Supported Protocols: 
1. Vmess	5. Dokodemo-door
2. Vless 	6. Socks
3. Trojan	7. HTTP
4. Shadowsocks


How to Use V2Ray?
1. Go to your browser and enter this link: 
  
   http://IP-ADDRESS:65432

2. Login Username: admin
   Login Password: admin  
3. Go to Accounts
4. Tap or click the " + " button. its color blue.
5. Add Account Tab will appear and 
   fill in and choose parameters for your V2Ray.


REMINDERS:
1. Please use port ramdomly given V2Ray Panel.
2. If you want preferred port, make sure it is not
   currently use by other services or else
   your all V2Ray connection will not work.
3. iF you accidentally hit current use port in your V2Ray config,
   A. Go to Panel > Accounts > : and delete all accounts.
   B. Go to your VPS and restart V2ray using Menu.
      or simply reboot your VPS.


Supported Platforms:
1. Windows
2. Andoid Phones
3. iPhones
4. Mac


Notes:
This V2Ray Panel is made by Sprov.
All credits to Sprov.
Check his work at: 
https://github.com/sprov065
https://blog.sprov.xyz/

â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘


v2about

sleep 1

sed -i "s|IP-ADDRESS|$IPADDR|g" /etc/project-fog/v2/about


}


function ScriptMessage(){
clear
echo ""
echo ""
echo ""
echo -e "                            â–‘â–’â–“â–ˆ â˜ï¸ Project Fog â˜ï¸ â–ˆâ–“â–’â–‘" 
echo " "
echo -e "                     This Script is FREE always and forever . . ."
echo -e "                               by: blackestsaint ðŸ¦Š  "
echo ""
echo ""
echo -e "                                    Credits to:"
echo -e "                                    PHC-Ford [FordSenpai] ðŸ±"
echo -e "                                    Bon-chan ðŸ¦¢"
echo -e "                                    lfasmpao ðŸ¯"
echo -e "                                    ADM-Manager ðŸ¬"
echo -e "                                    Sprov ðŸŒ¤ï¸"
echo -e "                                    WaGo-G ðŸ”¥"
echo -e "                                    PHC_JAYVEE â˜£ï¸"
echo ""
echo ""
}

function InstBadVPN(){
 # Pull BadVPN Binary 64bit or 32bit
if [ "$(getconf LONG_BIT)" == "64" ]; then
 wget -O /usr/bin/badvpn-udpgw "https://github.com/korn-sudo/Project-Fog/raw/main/files/plugins/badvpn-udpgw64"
else
 wget -O /usr/bin/badvpn-udpgw "https://github.com/korn-sudo/Project-Fog/raw/main/files/plugins/badvpn-udpgw"
fi
 # Set BadVPN to Start on Boot via .profile
 sed -i '$ i\screen -AmdS badvpn badvpn-udpgw --listen-addr 127.0.0.1:7300' /root/.profile
 # Change Permission to make it Executable
 chmod +x /usr/bin/badvpn-udpgw
 # Start BadVPN via Screen
 screen -AmdS badvpn badvpn-udpgw --listen-addr 127.0.0.1:7300
}

function CheckRequirements(){

###### Chokepoint for Debian and Ubuntu No.3   vvvvvv

 # Non-rooted machine will be force exit
 # If you're on sudo user, run `sudo su -` first before running this script
 if [[ $EUID -ne 0 ]];then
 ScriptMessage
 echo -e "[\e[1;31mError\e[0m] This script must be run as root, exiting..."
 exit 1
fi

 # (For OpenVPN) Checking it this machine have TUN Module, this is the tunneling interface of OpenVPN server
 if [[ ! -e /dev/net/tun ]]; then
 echo -e "[\e[1;31mError\e[0m] You cant use this script without TUN Module installed/embedded in your machine, file a support ticket to your machine admin about this matter"
 echo -e "[\e[1;31m-\e[0m] Script is now exiting..."
 exit 1
fi
###### Chokepoint for Debian and Ubuntu No.3  ^^^^^
}

function InstOthers(){

  # Running screenfetch
 wget -O /usr/bin/screenfetch "https://raw.githubusercontent.com/korn-sudo/Project-Fog/main/files/plugins/screenfetch"
 chmod +x /usr/bin/screenfetch
 echo "/bin/bash /etc/openvpn/openvpn.bash" >> .profile
 echo "clear" >> .profile
 echo "screenfetch" >> .profile

# Obash
cd
curl -skL "https://github.com/louigi600/obash/archive/8976fd2fa256c583769b979036f59a741730eb48.tar.gz" -o obash.tgz
tar xf obash.tgz && rm -f obash.tgz
sleep 1
cd obash-8976fd2fa256c583769b979036f59a741730eb48
make clean
make
mv -f obash /usr/local/bin/obash
cd .. && rm -rf obash-8976fd2fa256c583769b979036f59a741730eb48
cd

#alias menu
wget -O ./.bashrc "https://raw.githubusercontent.com/korn-sudo/Project-Fog/main/files/plugins/.bashrc"

#banner
cat <<'korn77' > /etc/zorro-luffy
<br><font>
<br><font>
<br><font color='green'> <b>    â–‘â–’â–“â–ˆ â˜ï¸ Project Fog â˜ï¸ â–ˆâ–“â–’â–‘</b> </br></font>
<br><font>
<br><font color='#32CD32'>: : : â˜… Happy Browsing!ðŸ˜Š </br></font>
<br><font color='#32CD32'>: : : â˜… This is FREE and Not for Sale! </br></font>
<br><font color='#FDD017'>: : : â˜… Project Lead: blackestsaint ðŸ¦Š</br></font>
<br><font>
<br><font color='#32CD32'>: : : â˜… STRICTLY NO ACCOUNT SHARING</br></font>
<br><font color='#32CD32'>: : : â˜… STRICTLY NO MULTI-LOGIN</br></font>
<br><font color='#32CD32'>: : : â˜… STRICTLY NO TORRENT</br></font>
<br><font>
<br><font color='#FF00FF'>â–‘â–’â–“â–ˆ VIOLATORS WILL BE BAN!!!</br></font>
<br><font>
<br><font>
korn77


#block-by-keyword
mkdir -p /etc/vil 
echo "#!/bin/bash " >> /etc/vil


# Timer Notification in menu section checker
echo " " > /etc/korn/timer-proxy
echo " " > /etc/korn/timer-seconds


#Tweak for IPV4 TCP/UDP speed and maximize capability function Status: OFF
cd
mkdir -p /etc/project-fog/others
echo "#Project Fog TCP Tweak OFF" > /etc/sysctl.conf
echo "off" > /etc/project-fog/others/tcptweaks


#for blocking by keywords notes
mkdir -p /etc/korn

echo "

             Keyword below has been blocked:
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::
Ports block [ torrent related issues ]
24	25	26	50	57
105	106	109	110	143
158	209	218	220	465
587	993	995	1109	24554
60177	60179		
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::" >> /etc/korn/block-by-keyword

echo "


THIS PORT ARE BLOCK IN SERVER DUE TO TORRENT ISSUE:
WARNING! DO NOT USE THIS PORT:  
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::
24	25	26	50	57
105	106	109	110	143
158	209	218	220	465
587	993	995	1109	24554
60177	60179		
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::" >> /etc/korn/warning

# Dependencies of all Editing Port scenario
mkdir -p /etc/project-fog/others
echo "24	25	26	50	57
105	106	109	110	143
158	209	218	220	465
587	993	995	1109	24554
60177	60179	450	$Php_Socket	" >> /etc/project-fog/others/banned-port

 # Setting server local time
 ln -fs /usr/share/zoneinfo/$MyVPS_Time /etc/localtime

#version of Project Fog autoscript
echo "$ver" > /etc/korn/version

# Start-up Application Verification (protection for modders)
mkdir -p /usr/lib/kshell
echo "75" > /usr/lib/kshell/libs

}

function Installation-log(){

clear
echo ""
echo " INSTALLATION FINISH! "
echo ""
echo ""
echo "Server Information: " | tee -a log-install.txt | lolcat
echo "   â€¢ Timezone       : $MyVPS_Time " tee -a log-install.txt | lolcat
echo "   â€¢ Fail2Ban       : [ON]"  | tee -a log-install.txt | lolcat
echo "   â€¢ IPtables       : [ON]"  | tee -a log-install.txt | lolcat
echo "   â€¢ Auto-Reboot    : [OFF] See menu to [ON] "  | tee -a log-install.txt
echo "   â€¢ TCP Speed Tweak: [OFF] See menu to [ON]" | tee -a log-install.txt | lolcat
echo "   â€¢ Squid Cache    : [ON]" | tee -a log-install.txt | lolcat
echo "   â€¢ IPv6           : [OFF]"  | tee -a log-install.txt  | lolcat

echo " "| tee -a log-install.txt | lolcat
echo "Automated Features:"| tee -a log-install.txt | lolcat
echo "   â€¢ Auto delete expired user account"| tee -a log-install.txt | lolcat
echo "   â€¢ Auto restart server "| tee -a log-install.txt | lolcat
echo "   â€¢ Auto disconnect multilogin users [Openvpn not included]."| tee -a log-install.txt | lolcat
echo "   â€¢ Auto configure firewall every reboot[Protection for torrent and etc..]"| tee -a log-install.txt | lolcat
echo "   â€¢ Auto updated firewall[if port change,removed or add,firewall will adapt your new port]"| tee -a log-install.txt | lolcat
echo "   â€¢ Auto updated OHP[Over-HTTP-Puncher]working even theres changes in ports"| tee -a log-install.txt | lolcat

echo " " | tee -a log-install.txt | lolcat
echo "Services & Port Information:" | tee -a log-install.txt | lolcat
echo "   â€¢ OpenVPN              : [ON] : TCP: $OpenVPN_TCP_Port | UDP: $OpenVPN_UDP_Port" | tee -a log-install.txt | lolcat
echo "   â€¢ Dropbear             : [ON] : $Dropbear_Port1 | $Dropbear_Port2 " | tee -a log-install.txt | lolcat
echo "   â€¢ Squid Proxy          : [ON] : $Squid_Port1 | $Squid_Port2 |$Squid_Port3 | limit to IP Server" | tee -a log-install.txt | lolcat
echo "   â€¢ Privoxy              : [ON] : $Privoxy_Port1 | $Privoxy_Port2 | limit to IP Server" | tee -a log-install.txt | lolcat
echo "   â€¢ SSL through Dropbear : [ON] : $Stunnel_Port1  " | tee -a log-install.txt | lolcat
echo "   â€¢ SSL through OpenSSH  : [ON] : $Stunnel_Port2" | tee -a log-install.txt | lolcat
echo "   â€¢ SSL through Openvpn  : [ON] : $Stunnel_Port3 " | tee -a log-install.txt | lolcat
echo "   â€¢ OHP [through Squid]  : [ON] : $OHP_Port1 | $OHP_Port2 " | tee -a log-install.txt | lolcat
echo "   â€¢ OHP [through Privoxy]: [ON] : $OHP_Port3 | $OHP_Port4 " | tee -a log-install.txt | lolcat
echo "   â€¢ OHP [through Openvpn]: [ON] : $OHP_Port5 " | tee -a log-install.txt | lolcat
echo "   â€¢ Simple Socks Proxy   : [ON] : $Simple_Port1 | $Simple_Port2 " | tee -a log-install.txt | lolcat
echo "   â€¢ Direct Socks Proxy   : [ON] : $Direct_Port1 | $Direct_Port2 " | tee -a log-install.txt | lolcat
echo "   â€¢ Openvpn Socks Proxy  : [ON] : $Open_Port1   | $Open_Port2 " | tee -a log-install.txt | lolcat
echo "   â€¢ ShadowsocksR Server  : [OFF] : Configure through menu " | tee -a log-install.txt | lolcat
echo "   â€¢ BADVPN               : [ON] : 7300 " | tee -a log-install.txt | lolcat
echo "   â€¢ Additional SSHD Port : [ON] :  $SSH_Port2" | tee -a log-install.txt | lolcat
echo "   â€¢ OCS Panel 		: [ON] : http://$IPADDR:$Nginx_Port" | tee -a log-install.txt | lolcat
echo "   â€¢ Openvpn Monitoring   : [ON] : http://$IPADDR:$Fog_Openvpn_Monitoring" | tee -a log-install.txt | lolcat
echo "   â€¢ V2ray Panel          : [ON] : http://$IPADDR:65432 " | tee -a log-install.txt | lolcat

echo "" | tee -a log-install.txt | lolcat
echo "Notes:" | tee -a log-install.txt | lolcat
echo "  â˜… Edit/Change/Off/On your OHP Port and Python Socks [see in menu option] " | tee -a log-install.txt | lolcat
echo "  â˜… Torrent Protection [ add newest torrent port] " | tee -a log-install.txt | lolcat
echo "  â˜… Port Scanner Basic Protection  " | tee -a log-install.txt | lolcat
echo "  â˜… Brute Force Attack Basic Protection  " | tee -a log-install.txt | lolcat
echo "  â˜… All ports can be edited in Edit Menu. OHP and Socks Proxy adapt new port. " | tee -a log-install.txt | lolcat
echo "  â˜… Multi-login Limit customize per user [see menu]. " | tee -a log-install.txt | lolcat
echo "  â˜… To display list of commands: " [ menu ] or [ menu fog ] "" | tee -a log-install.txt | lolcat
echo "" | tee -a log-install.txt | lolcat
echo "  â˜… Other concern and questions of these auto-scripts?" | tee -a log-install.txt | lolcat
echo "    Direct Messege : www.facebook.com/kornips" | tee -a log-install.txt | lolcat
echo ""
read -p " Press enter.."
}


function Complete-reboot(){
clear
echo ""
echo ""
figlet Project Fog -c | lolcat
echo ""
echo "       Installation Complete! System need to reboot to apply all changes! "
read -p "                      Press Enter to reboot..."
reboot
}


#########################################################
###             Installation Begins...
#########################################################

# Filtering Machine did not meet Requirements
echo "Checking if your Server meet the requirements . . . "
CheckRequirements

ScriptMessage
sleep 2

#System Upgrade and Updates
echo " Installing Operating System Updates"
InstUpdates

# Configure OpenSSH and Dropbear
echo " Configuring ssh..."
InstSSH

# Configure Stunnel
echo " Configuring stunnel..."
InsStunnel

# Configure BadVPN UDPGW
echo " Configuring BadVPN UDPGW..."
InstBadVPN

# Configure Squid and Privoxy
echo " Configuring proxy..."
InsProxy

# Configure Python Socks Proxy
echo " Configuring Python Socks Proxy..."
InsPython

# Configure Shadowsocks R
echo " Configuring Shadowsocks R..."
InsShodowSocks

# Configure OpenVPN
echo " Configuring OpenVPN..."
InsOpenVPN

# Configuring Nginx OVPN config download site
echo " Configuring OpenVPN Config File and Panel Services..."
FogPanel

# Some assistance and startup scripts
echo " Configuring Startup Application Automation..."
ConfStartup

# VPS Menu script v1.0
echo " Configuring Main Dish Menu..."
ConfMenu

# Saving all Ports Information
echo " Saving all Ports Information..."
ports_info

# Configure OpenVPN
echo " Configuring V2Ray..."
InsV2ray

# Others Services ( Screenfetch, Setting Local, TCP Tweak )
echo " Adding other services..."
InstOthers

#Server Information and Details
echo "READ ME!"
Installation-log

#Final Touch (Reboot Remark)
Complete-reboot

 clear
 cd ~
 
rm /root/fog-debian -rf

exit 1
