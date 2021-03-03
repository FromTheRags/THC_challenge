SYSTEM REQUIREMENTS :

Debian / Ubuntu (wsl2 supported)


LIST OF COMMANDS :

Install openssh (needed to generate keys), docker and its dependencies :

	./install.sh

Start the docker daemon :

	./start_docker.sh

Build the containers and their networks :

	./build_network.sh

Start the containers :

	./start_network.sh

Stop the containers (the proper way) :

	./stop_network.sh

Restart the containers (stop and start) :

	./restart_network.sh

Kill the containers (the dirty way) :

	./kill_network.sh

Delete the containers and networks (undo what build_network.sh has done) :

	./delete_network.sh

Update the containers (kill, delete, build and start again) :

	./update_network.sh

Access a shell inside a container :

	./enter_<machine_name>.sh	# <machine_name> = "firewall", "attack", "ftp" or "private"


MOST COMMON WORKFLOW :

The first time :

	./install.sh		# only if docker is not installed
	./start_docker.sh
	./build_network.sh
	./start_network.sh

When you changed the configuration (Dockerfile or script used by Dockerfile) :
#nice
	./update_network.sh

