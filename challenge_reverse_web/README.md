# Reverse / Web challenge

This challenge mixes android web application reversing and web vulnerabilities (SQL injection, bad front-end / back-end segregation). The attacker must understand how a connection to the application shopping web site is established and discover a URL towards an older version of the site embedded into the application. The old site contains a SQL injection vulnerability. Once connected, the attacker will get a clue on the current site password by reading the user profile. Then, he will be able to connect on the new site and from there, he will be able to buy a flag item for free thanks to a bad design decision (price is not checked in the database, front-end is trusted).

## Installation

Run those commands to install the necessary tools and start the docker container :

	git clone https://github.com/SuperTeam1nsa/THC_challenge.git
	cd challenge_reverse_web/web/docker
	sh install.sh  # OR juste make sure you have installed all the tools in the "apt-get install" lines of the script
	sh build_server.sh
	sh start_server.sh
	sh enter_server.sh
	certbot --apache --non-interactive --agree-tos --register-unsafely-without-email --redirect --domain <domain_name>
	exit

Give this file to the attacker (this is the apk of the android application) :

	challenge_reverse_web/buyExpress.apk

## Attack

The attack walkthrough is described in :

	challenge_reverse_web/solution.docx

Annexes are available in :

	challenge_reverse_web/web/hack

