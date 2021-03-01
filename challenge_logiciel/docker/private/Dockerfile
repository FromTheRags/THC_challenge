FROM generic_alpine

WORKDIR /root
RUN echo "PS1='\u@private:\w\$ '" >> .bashrc
RUN apk update && apk add openssh-server
COPY set_route.sh ./
COPY add_users_private.sh ./
RUN sh add_users_private.sh && rm -f add_users_private.sh
COPY keys/ouioui_ssh_key.pub ./
RUN mkdir /home/ouioui/.ssh; cat ouioui_ssh_key.pub >> /home/ouioui/.ssh/authorized_keys; rm -f ouioui_ssh_key.pub; chown -R ouioui /home/ouioui/.ssh && chmod -R 700 /home/ouioui/.ssh
RUN rc-status && touch /run/openrc/softlevel

ENTRYPOINT rc-service sshd stop; rc-service sshd start; sleep infinity

