#!/bin/sh

sudo docker cp ../exploit/exploit.sh attack_container:/root
sudo docker cp ../exploit/soluce.py attack_container:/root
#sudo docker exec attack_container "/bin/bash /root/exploit.sh"

