# Network / Use-after-free challenge

This challenge mixes a network vulnerability and a user-after-free vulnerability. The attacker must find a way to take control of the dmz server thanks to a vulnerable ftp service. Then, thanks to a network scanning, he will discover a machine on an internal network, he will have to change user and test ssh, to find a user having an access. Finally, on the last machine, the user must exploit a use-after-free vulnerability on a custom program to open a root shell.

## Installation

	git clone https://github.com/SuperTeam1nsa/THC_challenge.git
	cd challenge_reseau_uaf/docker
	sh install.sh  # OR make sure you have installed all the tools in the "apt-get install" lines of the script
	sh update_network.sh

## Attack

The attack walkthrough is described in :

	challenge_reseau_uaf/solution.txt

