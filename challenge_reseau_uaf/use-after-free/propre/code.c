#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <sys/socket.h>
#include <sys/ioctl.h>
#include <netinet/in.h>
#include <net/if.h>
#include <arpa/inet.h>

#define NAME_SIZE 88
 //64 sinon
typedef struct  __attribute__((__packed__))interface
{
    char name[NAME_SIZE];
	char *ip; //taille variable selon v4 or v6
    void (*turnOn)(struct interface*);
} Interface;
void showInterface(Interface *i);
void reset (Interface*);
void destroy (Interface*);
void turn_on_old_school(Interface *);//#ifconfig
void turn_on_modern_school(Interface *);//#ip
void endOfLine(char* line);
Interface* newInterface(char* name,char *ip, void (*on)(struct interface*))
{
    Interface* inter = malloc(sizeof(Interface));
    strncpy(inter->name, name, NAME_SIZE);
    inter->ip=strndup(ip, strlen(ip));
    inter->turnOn=on;
    return inter;
}

void showInterface(Interface *i)
{
    printf("\n name: %s ip: %s turnOn: %p \n",i->name,i->ip, &i->turnOn);
}
void endOfLine(char* line)
{
    while(*line != '\n')
        line++;
    *line = 0;
}

void destroy(Interface* inter){
    free(inter->ip);
    free(inter);
    puts("I forgot that interface !");
}
void turn_on_old_school(Interface *inter){
    puts("Set up the real Interface associated old school (test eth0 only now)");
    puts("TODO: manage the IP !");
    int fd;
    struct ifreq ifr;
    fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (fd < 0)
        return;
    memset(&ifr, 0, sizeof ifr);
    //16 inter->name
    strncpy(ifr.ifr_name, "eth0" , IFNAMSIZ);
    ifr.ifr_flags |= IFF_UP;
    ioctl(fd, SIOCSIFFLAGS, &ifr);
    close(fd);
    puts("Interface up !");
    /*    struct sockaddr_in *addr;
    ifr.ifr_addr.sa_family = AF_INET;
    strncpy(ifr.ifr_name, inter.name , IFNAMSIZ);
    addr=(struct sockaddr_in *)&ifr.ifr_addr;
    inet_pton(AF_INET,ip_address,&addr->sin_addr);
    ioctl(fd, SIOCSIFADDR, &ifr);
    close(fd);*/
}
void turn_on_modern_school(Interface *i){
    puts("YET TODO: use a more modern school way to do it");
}
/*
void reset(Interface* inter)
{
    int sockfd;
    struct ifreq ifr;
    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0)
        return;
    memset(&ifr, 0, sizeof ifr);
    strncpy(ifr.ifr_name, inter->name , IFNAMSIZ);
    //ifr.ifr_flags |= IFF_UP;
    ifr.ifr_flags &= ~IFF_UP;
    ioctl(sockfd, SIOCSIFFLAGS, &ifr);
    close(sockfd);
    //put down interface
    puts("Interface down !");
}*/
int main()
{
    printf("\n This my cool interfaceTool,I am still working on it ! \n \
The purpose of this is to speed up my network configuration.");
    int end = 0;
    char order = -1;
    char nl = -1;
    //current
    Interface* inter = NULL;
    Interface* inters[10]= {NULL};
    while(!end)
    {
        puts("\n \
 others: Quit \n \
 0: Select an virtual Interface \n \
 1: Create an virtual Interface \n \
 2: Turn on Interface(apply to real) \n \
 3: Forget selected virtual interface \n \
 4: Show current Interface configuration \n \
 Your choice ?  \n");
        order = getc(stdin);
        nl = getc(stdin);
        if(nl != '\n')
        {
            puts("Bad selection, aborting \n");
            goto EXIT;
        }
        fseek(stdin,0,SEEK_END);
        switch(order)
        {
        case '0':
            puts("Which virtual interface do you want to configure ? (0-9)\n");
            char turn=getc(stdin);
            if(getc(stdin) != '\n')
            {
                 puts("Too long, aborting.");
                goto EXIT;
            }
            //(int) strtol(str, (char **)NULL, 10)
            int nb=(int)turn;
            //printf("nb: %d", nb);
            if(nb >57 || nb <48)
            {
                puts("\nBad number of interface, aborting.");
                goto EXIT;
            }
            else if(inters[nb-48]!=NULL)
            {
                inter=inters[nb-48];
            }
            else
            {
                puts("\nNot a valid interface, please create it beforehand.");
            }
            break;
        case '1':
            puts("Give the name of the real interface associated: ");
            char name[NAME_SIZE] = {0};
            fgets(name, NAME_SIZE-1, stdin);
            endOfLine(name);

            char ip[NAME_SIZE*2] = {0};
            puts("Give it an ip: ");
            fgets(ip, NAME_SIZE*2 -1, stdin);
            endOfLine(ip);

            puts("Select a 'turn on' method for the virtual interface:\n1:old school 2:modern school ?(default)\n");
            char t=getc(stdin);
            if(getc(stdin) != '\n')
            {
                puts("Bad number, aborting.");
                goto EXIT;
            }
            puts("Select a free interface to save it:(0-9) \n");
            char t2=getc(stdin);
            if(getc(stdin) != '\n')
            {
                 puts("Too long, aborting.");
                goto EXIT;
            }
            int nbs=(int)t2;
            if(nbs >57 || nbs <48)
            {
                puts("\nBad number of interface, aborting.");
                goto EXIT;
            }
            inter=newInterface(name,ip, (t=='1'?turn_on_old_school:turn_on_modern_school));
            inters[nbs-48]=inter;
            //double free if freed here
            break;
        case '2':
            if(!inter)
            {
                puts("You must select an interface first !");
                break;
            }
            inter->turnOn(inter);
            break;
        case '3':
            if(!inter)
            {
                puts("You must select an interface first !");
                break;
            }
            destroy(inter);
            break;
        case '4':
            if(!inter)
            {
                puts("You must select an interface first !");
                break;
            }
            showInterface(inter);
            break;
        default:
            end = 1;
        }
    }
    EXIT:
    puts("Nb:todo remove space allocated at exit");
    return 0;
}
