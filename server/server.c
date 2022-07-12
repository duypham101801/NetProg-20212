//
// Created by Manh Dao on 7/5/2022.
//

#include<stdlib.h>
#include<stdio.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<string.h>
#include<arpa/inet.h>
#include<pthread.h>

#define MAXLINE 10000  /*max text line length*/
#define SERV_PORT 8080 /*port*/
#define LISTENQ 10 /*maximum number of client*/

pthread_mutex_t mutex;
int clients[100];  /*list store clients*/
int clinum = 0;  /*current number clients*/
int n = 0;

int listenfd, connfd, n;
pid_t childpid;
socklen_t clien;
char buf[MAXLINE];
struct sockaddr_in cliaddr, servaddr;
pthread_t recvt;  /*deal with multi clients*/

// xoa chuoi con trong 1 string
char* pop_child_str(char* str1,char* str2){
    const int len = strlen(str2);
    char str3[256] = "";
    char *p1 = str1, *p2;

    /* Tạo vòng lặp để xóa hết chuỗi con */
    while((p2 = strstr(p1,str2)) != NULL) { /*Tìm vị trí chuỗi con bằng hàm strstr*/
        strncat(str3,p1,p2 - p1);   /* Nối các phần không chứa chuỗi con */
        p1 = p2 + len;      /* Dịch chuyển con trỏ sang vị trí tìm kiếm tiếp theo */
    }
    strcat(str3,p1);
    printf("%s\n",str3);
}

int checkuserpass(char* msg) {

    return 1;
}

// send message to current client
void sendtoclient(char *servmsg, int curr) {
    pthread_mutex_lock(&mutex);
    for (int i=0; i<clinum; ++i) {
        if (clients[i] == curr) {
            if(send(clients[i], servmsg, strlen(servmsg), 0) < 0) {
                printf("Sending failure\n");
                continue;
            }
        }
    }
    // close(connfd);
    pthread_mutex_unlock(&mutex);
}

// server action: send + recv messages
void *dealmsg(void *connfd) {
    int sock = *((int *)connfd);
    char msg[MAXLINE];  /*message to server*/
    char servmsg[MAXLINE];  /*message from server*/
    int len;
    while((len = recv(sock, msg, MAXLINE, 0)) > 0) {
        printf("Server's received from client [%d]: ", (clinum-1));
        strncpy(msg, "\0", strlen("\0"));
        printf("%s\n", msg);

        // get server message arcording client send
        char *header;
        header = strtok(msg, " ");
        printf("Header is: %s\n", header);
    	// pop_child_str(msg, header);
    	// printf("Message now is: %s\n", msg);
    	// printf("Check comparision: %d\n", strcmp(header, "USERPASS"));
        

    	if (strcmp(header, "USERPASS") == 0) {
    	   /*
           if(checkuserpass(msg)) {
                servmsg = "REG_SUCCESS <true>";
           } else {
                servmsg = "REG_SUCCESS <false>";
           }
           */
           
           /*char*** header_response{{"SYMP_REQ", "SYMP_FORM <index symptom> <symptom> - <index sympton> <symptom> - <> -"}};
           for (int i=0; i< sizeof(header_response)/sizeof(header_response[0]); ++i)
           	if (strcmp(header, header_response[i][0]) == 0)
           		strncpy(servmsg, header_response[i][1], strlen(header_response[i][1]);*/
           		 
           strncpy(servmsg, "REG_SUCCESS <true>", strlen("REG_SUCCESS <true>"));
    	} else if (strcmp(header, "SYMP_REQ") == 0) {
	   strncpy(servmsg, "SYMP_FORM <index symptom> <symptom> - <index sympton> <symptom> - <> -", strlen("SYMP_FORM <index symptom> <symptom> - <index sympton> <symptom> - <> -"));
        } else if (strcmp(header, "SYMP_SUBMIT") == 0) {
	   strncpy(servmsg, "SYMP_SUCCESS <true>", strlen("SYMP_SUCCESS <true>"));
    	} else if (strcmp(header, "ANS_REQ") == 0) {
	   strncpy(servmsg, "ANS_FORM <index symptom 1> <ques 1> - <index symptom 2> <ques 2> - <> <> -", strlen("ANS_FORM <index symptom 1> <ques 1> - <index symptom 2> <ques 2> - <> <> -"));
    	} else if(strcmp(header, "ANS_SUBMIT") == 0) {
	   strncpy(servmsg, "ANS_SUCCESS <true>", strlen("ANS_SUCCESS <true>"));
    	} else if(strcmp(header, "DIG_REQ") == 0) {
           strncpy(servmsg, "DIG_ANS <index symtom 1> <result 1> - <index symtom 2> <result 2> - <> <> - …", strlen("DIG_ANS <index symtom 1> <result 1> - <index symtom 2> <result 2> - <> <> - "));
    	} else if(strcmp(header, "QUIT") == 0) {
	   strncpy(servmsg, "QUIT_ACCEPT", strlen("QUIT_ACCEPT"));
    	} else if(strcmp(header, "CONSULT_REQ") == 0) {
	   strncpy(servmsg, "CONSULT_PAT <success>", strlen("CONSULT_PAT <success>"));
    	} else if(strcmp(header, "MSG_SEND") == 0) {

    	} else if(strcmp(header, "MSG_RECV_SUCCESS") == 0) {

    	} else {
            printf("Header not exist!!! Check format header message!");
            strncpy(servmsg, "", strlen(""));
    	}
    	
    	memset(msg, 0, strlen(msg));
    	strncpy(msg, "", strlen(""));
    	len = 0;
    	
        // send msg to each client
        printf("Server sends message: %s\n\n", servmsg);
        sendtoclient(servmsg, sock);
        memset(servmsg, 0, strlen(servmsg));
        
    }
}

int main(int argc, char **argv) {
    
    // creaation of socket
    listenfd = socket(AF_INET, SOCK_STREAM, 0);
    if (listenfd == -1) {
    	printf("Could not create socket!\n");
    }
    puts("Socket created!");

    // preparation of socket address
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(SERV_PORT);

    // bind
    // bind(listenfd, (struct sockaddr *) &servaddr, sizeof(servaddr));
    // printf("%d\n", bind(listenfd, (struct sockaddr *) &servaddr, sizeof(servaddr)));
    if(bind(listenfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) == -1) {
    	printf("Cannot bind, ERROR!!\n");
    }
    puts("Bind done!");

    // listen
    listen(listenfd, LISTENQ);
    if(listen(listenfd, LISTENQ) == -1)
        printf("Listening failed, ERROR!!\n");

    printf("%s\n", "Server running ... waiting for connections.");

    for (;;) {
        clien = sizeof(cliaddr);
        connfd = accept(listenfd, (struct sockaddr *)&cliaddr, &clien);
        if(connfd < 0)
            printf("Accepted failed, ERROR!!\n");

        printf("%s\n\n", "Received request ...");

        // start thread
        pthread_mutex_lock(&mutex);
        printf("Client [%d] with ip [%d] connected successful!\n", clinum, inet_ntoa(servaddr.sin_addr));
        clients[clinum] = connfd;
        clinum++;

        // creating a thread for each client
        pthread_create(&recvt, NULL, (void *) dealmsg, &connfd);

        // release mutex
        pthread_mutex_unlock(&mutex);

        // close listen socket
        // close(connfd);
    }
    // close(listenfd);
    return 0;
}
