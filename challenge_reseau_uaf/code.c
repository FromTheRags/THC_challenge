    #include <stdlib.h>
    #include <stdio.h>
    #include <string.h>
    #include <unistd.h>

     /* but: écrire le shellcode dans le name interface+ free it+ new interface avec name =65 caractères (shellcode[60]+@)//allocate(malloc) physical connexion with name=0x pointing to begin @ of info #ASLR leak,
     +call turnOn sur la 1ere interface => exec shellcode
     */
     //medium+ one (our)
     //strndup interesting one
    #define BUFLEN 64

    void (*reset)(struct Interface*);
    void (*destroy)(struct Interface*);
    void turn_on_old_school(Interface *);//#ifconfig
    void turn_on_modern_school(Interface *);//#ip

    //leakage position last champ de la structure #mauvais %p avec & en trop => on peut retrouver le debut de buffer en faisant -60
    void showInterface(Interface *i){
    	printf("name: %s \n ip: %s turnOn: %p",i->name,i->ip, &i->turnOn);
    }
    struct Interface{
        char name[59];
        char *ip; //taille variable selon v4 or v6
        void (*turnOn)();
    };
    struct Interface* newInterface(char* name,char *ip){
            struct Interface* inter = malloc(sizeof(struct Interface));
            strncpy(inter->name, name, 59);
            inter->ip=strndup(line, strlen(line+6)-1);
            return inter;
    }

     /* old id
    struct physical{
        char info[58];
        char name[8];
    };*/

    void endOfLine(char* line){
        while(*line != '\n') line++;
        *line = 0;
    }

    void reset(){

    }

    int main(){
    	printf("Warning: This is an alpha version not yet functionnal, it should'nt be used in production!");
        int end = 0;
        char order = -1;
        char nl = -1;
        //current
        struct Interface* inter = NULL;
        struct Interface* inters[10]={NULL};
        struct Physical* phys = NULL;
        while(!end){
            puts("-1: Quit 0: Select an virtual Interface 1: Create an virtual Interface\n2: Reset virtual Interface \n3: Turn on virtual Interface(apply to real) \n4: Forget selected virtual interface ");
            //\n5: Select Physical Interface info \n6: Attach Interface to physical \n7:Forget physical interface\n0: Quit");
            order = getc(stdin);
            nl = getc(stdin);
            if(nl != '\n'){
                exit(0);
            }
            fseek(stdin,0,SEEK_END);
            switch(order){
            case '0':
		puts("Choose which one (1-10):");
		char turn=getc(stdin);
		if(getc(stdin) != '\n'){
			exit(0);
		}
		//(int) strtol(str, (char **)NULL, 10)
		int nb=(int)turn;
		if(nb >10 || nb <1){
			puts("Bad number of interface, aborting.");
		}else if(inters[nb]!=NULL){
			inter=inters[nb];
		}else{
			puts("Not a valid interface, aborting.");
		}

            case '1':
            	char name[BUFLEN] = {0};
                puts("Give it a name: ");
                fgets(name, BUFLEN, stdin);
                endOfLine(name);

                char ip[BUFLEN*3] = {0};
                puts("Give it an ip: ");
		fgets(ip, BUFLEN*3, stdin);
                endOfLine(ip);


		puts("Turn on method: 1:old school 2:modern school ?(default) ");
		char turn=getc(stdin);
	    	if(getc(stdin) != '\n'){
			exit(0);
            	}
                inter=newInterface(name,ip, turn=='1'?turn_on_old_school:turn_on_modern_school);
                break;
            case '2':
                if(!dog){
                    puts("You do not have a dog.");
                    break;
                }
                dog->bark();
                break;
            case '3':
                if(!inter){
                    puts("No valid interface found");
                    break;
                }
                break;
            default:
                end = 1;
            }
        }
        return 0;
    }
