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

// xoa chuoi con trong 1 string
char* pop_child_str(char* str1,char* str2){
    const int len = strlen(str2);
    char str3[128] = "";
    char *p1 = str1, *p2;
    
    /* Tạo vòng lặp để xóa hết chuỗi con */
    while((p2 = strstr(p1,str2)) != NULL) { /*Tìm vị trí chuỗi con bằng hàm strstr*/
        strncat(str3,p1,p2 - p1);   /* Nối các phần không chứa chuỗi con */ 
        p1 = p2 + len;      /* Dịch chuyển con trỏ sang vị trí tìm kiếm tiếp theo */
    }
    strcat(str3,p1);
    printf("%s\n",str3);    
}

bool checkuserpass(char* msg) {

    return true;
}

char* getservmsg(char *msg) {
    char *header;
    char servmsg[MAXLINE];
    header = strtok(smg, " ");
    pop_child_str(msg, header);

    if (strcmp(header, "USERPASS") == 0) {
        if(checkuserpass(msg)) {
            servmsg = "REG_SUCCESS <true>";
        } else {
            servmsg = "REG_SUCCESS <false>";
        }
    } else if (strcmp(header, "SYMP_REQ") == 0) {

    } else if (strcmp(header, "SYMP_SUBMIT") == 0) {

    } else if (strcmp(header, "ANS_REQ") == 0) {

    } else if(strcmp(header, "ANS_SUBMIT") == 0) {

    } else if(strcmp(header, "DIG_REQ") == 0) {

    } else if(strcmp(header, "QUIT") == 0) {

    } else if(strcmp(header, "CONSULT_REQ") == 0) {

    } else if(strcmp(header, "MSG_SEND") == 0) {

    } else if(strcmp(header, "MSG_RECV_SUCCESS") == 0) {

    } else {
        printf("Header not exist!!! Check format header message!");
        servmsg[MAXLINE] = "";
    }

    return servmsg;
}

// send message to current client
void sendtoclient(char *servmsg, int curr) {
    pthread_mutex_lock(&mutex);
    for (int i=0; i<n; ++i) {
        if (clients[i] == curr) {
            if(send(clients[i], servmsg, strlen(msg), 0) < 0) {
                printf("Sending failure\n");
                continue;
            }
        }
    }
    pthread_mutex_unlock(&mutex);
}

// server action: send + recv messages
void *dealmsg(void *connfd) {
    int sock = *((int *)connfd);
    char msg[MAXLINE];
    char servmsg[MAXLINE];
    int len;
    while((len = recv(sock, msg, MAXLINE, 0)) > 0) {
        printf("%s", "Server's received from client: ");
        msg[len] = "";
        printf("%s\n", msg);

        // get server message arcording client send
        servmsg = getservmsg(msg);

        // send msg to each client
        printf("Server sends message: %s\n", servmsg);
        sendtoclient(servmsg, sock);
    }
}

int main(int argc, char **argv) {
    int listenfd, connfd, n;
    pid_t childpid;
    socklen_t clien;
    char buf[MAXLINE];
    struct sockadd_in cliaddr, servaddr;
    pthread_t recvt;

    // creaation of socket
    listenfd = socket(AF_INET, SOCK_STREAM, 0);

    // preparation of socket address
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(SERV_PORT);

    // bind
    bind(listenfd, (struct sockaddr *) &servaddr, sizeof(servaddr));
    if(bind(listenfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) == -1)
        printf("Cannot bind, ERROR!!\n");

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

        printf("%s\n", "Received request ...");
        pthread_mutex_lock(&mutex);
        clients[clinum] = connfd;
        clinum++;

        // login + registration

        // Symptoms

        // Answer form

        // Diagnosis

        // Consoluting: chat
        printf("Waiting for assinging a doctor ...\n");

        // creating a thread for each client
        pthread_create(&recvt, NULL, (void *) dealmsg, &connfd);

        // release mutex
        pthread_mutex_unlock(&mutex);

        // close listen socket
        close(connfd);
    }
    close(listenfd);
    return 0;
}
