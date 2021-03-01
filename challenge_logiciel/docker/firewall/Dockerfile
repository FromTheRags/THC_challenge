FROM generic_alpine

WORKDIR /root
COPY apply_rules.sh ./
RUN echo "PS1='\u@firewall:\w\$ '" >> .bashrc

ENTRYPOINT sleep infinity

