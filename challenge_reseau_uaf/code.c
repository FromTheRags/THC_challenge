#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <sys/socket.h>
#include <sys/ioctl.h>
#include <netinet/in.h>
#include <net/if.h>
#include <arpa/inet.h>
//gcc -fPIE -fstack-protector-all -D_FORTIFY_SOURCE=2 -Wl,-z,now -Wl,-z,relro -o uaf uaf.c
/* but: écrire le shellcode dans le name interface+ free it+ new interface avec name =65 caractères (shellcode[60]+@)//allocate(malloc) physical connexion with name=0x pointing to begin @ of info #ASLR leak,
+call turnOn sur la 1ere interface => exec shellcode
*/
//medium+ one (our)
//strndup interesting one
#define BUFLEN 64
/* old id
struct physical{
   char info[58];
   char name[8];
};*/
typedef struct interface
{
    char name[59];
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
    strncpy(inter->name, name, 59);
    inter->ip=strndup(ip, strlen(ip));
    inter->turnOn=on;
    return inter;
}
//leakage position last champ de la structure #mauvais %p avec & en trop => on peut retrouver le debut de buffer en faisant -60
void showInterface(Interface *i)
{
    printf("name: %s \n ip: %s turnOn: %p",i->name,i->ip, &i->turnOn);
}
void endOfLine(char* line)
{
    while(*line != '\n')
        line++;
    *line = 0;
}

void destroy(Interface* inter){
    free(inter->name);
    free(inter);
    puts("I forgot that interface !");
}
void turn_on_old_school(Interface *inter){
    puts("Set up the real Interface associated old school");
    puts("TODO: manage the IP !");
    int fd;
    struct ifreq ifr;
    struct sockaddr_in *addr;
    fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (fd < 0)
        return;
    memset(&ifr, 0, sizeof ifr);
    //16
    strncpy(ifr.ifr_name, inter->name , IFNAMSIZ);
    ifr.ifr_flags |= IFF_UP;
    ioctl(fd, SIOCSIFFLAGS, &ifr);
    close(fd);
    puts("Interface up !");
    /*
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
}
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
 2: Turn down Interface \n \
 3: Turn on Interface(apply to real) \n \
 4: Forget selected virtual interface \n \
 5: Show current Interface configuration \n \
 Your choice ?  \n");
        //\n5: Select Physical Interface info \n6: Attach Interface to physical \n7:Forget physical interface\n0: Quit");
        order = getc(stdin);
        nl = getc(stdin);
        if(nl != '\n')
        {
            exit(0);
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
                exit(0);
            }
            //(int) strtol(str, (char **)NULL, 10)
            int nb=(int)turn;
            //printf("nb: %d", nb);
            if(nb >57 || nb <48)
            {
                puts("\nBad number of interface, aborting.");
                exit(0);
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
            char name[BUFLEN] = {0};
            fgets(name, BUFLEN-1, stdin);
            endOfLine(name);

            char ip[BUFLEN*3] = {0};
            puts("Give it an ip: ");
            fgets(ip, BUFLEN*3 -1, stdin);
            endOfLine(ip);

            puts("Select a 'turn on' method for the virtual interface:\n1:old school 2:modern school ?(default)\n");
            char t=getc(stdin);
            if(getc(stdin) != '\n')
            {
                puts("Bad number, aborting.");
                exit(0);
            }
            puts("Select a free interface to save it:(0-9) \n");
            char t2=getc(stdin);
            if(getc(stdin) != '\n')
            {
                 puts("Too long, aborting.");
                exit(0);
            }
            //(int) strtol(str, (char **)NULL, 10)
            int nb=(int)t2;
            //printf("nb: %d", nb);
            if(nb >57 || nb <48)
            {
                puts("\nBad number of interface, aborting.");
                exit(0);
            }
            if (inters[nb-48]!=NULL){
                inters[nb-48]=inter;
            }
            else{
                    ///TODO: goto aborting to free goodly all the memory (à la place des exit(0));
                    free(inter->name);
                    free(inter);
            }
            inter=newInterface(name,ip, (t=='1'?turn_on_old_school:turn_on_modern_school));
            break;
        case '2':
            if(!inter)
            {
                puts("You must select an interface first !\n");
                break;
            }
            reset(inter);
            break;
        case '3':
            if(!inter)
            {
                puts("You must select an interface first !");
                break;
            }
            inter->turnOn(inter);
            break;
        case '4':
            if(!inter)
            {
                puts("You must select an interface first !");
                break;
            }
            destroy(inter);
            break;
        case '5':
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
    return 0;
}
