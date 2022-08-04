//
// Created by Manh Dao on 7/5/2022.
//

#include<stdlib.h>
#include<stdio.h>
#include<ctype.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<string.h>
#include<arpa/inet.h>
#include<pthread.h>
#include<sqlite3.h>

#define MAXLINE 1000000  /*max text line length*/
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


typedef struct {
	char* uid;
	char* acc;
	char* uname;
	char* pwd;
	char* role;
}client;
int patCount = 0;  /*number of current patient*/
int docCount = 0;  /*number of current doctor*/
client pat[MAXLINE], doc[MAXLINE];  /*list of patient and doctor*/
char listSymp[MAXLINE];  /*list of symptom index*/
char copyListSymp[MAXLINE];
char listAns[MAXLINE];  /*list of answer that said yes*/
char copyListAns[MAXLINE];
char listDiag[MAXLINE]; /*list diagnosis*/

char rightParent = '<';
char leftParent = '>';
char nga = '~';
char ngang = '-';

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

// send message to other client - doctor
void sendtoother(char *servmsg, int curr) {
    pthread_mutex_lock(&mutex);
    for (int i=0; i<clinum; ++i) {
        if (clients[i] != curr) {
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
    char copyMsg[MAXLINE];
    char servmsg[MAXLINE];  /*message from server*/
    int len;
    
    int flag = 0;
    int userCount = 0;
    int passCount = 0;
    
    // open database
    sqlite3 *db;
    int rc = sqlite3_open("../db/symptomchecker.db", &db);  /*Change dir to db here*/
    sqlite3_stmt *res;
    
    if(rc != SQLITE_OK) {
        fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
        sqlite3_close(db);
        // return 1;
    } else {
        fprintf(stderr, "Opened database successfully\n");
    }
    
    
    while((len = recv(sock, msg, MAXLINE, 0)) > 0) {
        printf("Server's received from client [%d]: ", (clinum-1));
        strncpy(msg, "\0", strlen("\0"));
        printf("%s\n", msg);

        // get server message arcording client send
        strncpy(copyMsg, msg, strlen(msg));
        char *header;
        header = strtok(copyMsg, " ");
        printf("Header is: %s\n", header);
        // printf("Message now is: %s", msg);

	/*char*** header_response{{"SYMP_REQ", "SYMP_FORM <index symptom> <symptom> - <index sympton> <symptom> - <> -"}};
           for (int i=0; i< sizeof(header_response)/sizeof(header_response[0]); ++i)
           	if (strcmp(header, header_response[i][0]) == 0)
           		strncpy(servmsg, header_response[i][1], strlen(header_response[i][1]);*/
    	if (strcmp(header, "USERPASS") == 0) {
    	   char username[MAXLINE], password[MAXLINE];
    	   // get username & password
    	   for(int i=0; i<strlen(msg); ++i) {
    	       if(msg[i] == '<') {
    	           flag += 1;
	       } else if (msg[i] == '>') {
	       	   flag += 1;
	       }
	       
	       if (flag == 1) userCount += 1;
	       if (flag == 3) passCount += 1;
    	   }
    	   
    	   strncpy(username, msg+15, userCount-1);
    	   strncpy(password, msg+15+userCount+7, passCount-1);
    	   printf("Username is: %s\n", username);
    	   printf("Password is: %s\n", password);
    	   
    	   // check username & password in db
    	   char *sql = "SELECT * FROM user WHERE uname = ? AND pwd = ?;";
    	   rc = sqlite3_prepare_v2(db, sql, -1, &res, 0);
    	   if (rc == SQLITE_OK) {
    	       sqlite3_bind_text(res, 1, username, strlen(username), NULL);
    	       sqlite3_bind_text(res, 2, password, strlen(password), NULL);
    	   } else {
    	       fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
	   }
	   
	   int step = sqlite3_step(res);
	   if (step == SQLITE_ROW) {
	   	printf("User id is: %s\n", sqlite3_column_text(res, 0));
	   	printf("Role id is: %s\n", sqlite3_column_text(res, 4));
	   	// add patient to patient list
	   	if (strcmp(sqlite3_column_text(res, 4), "pat") == 0) {
	   		/*strcpy(pat[patCount].uid, sqlite3_column_text(res, 0));
	   		printf("Run to here\n");
	   		strcpy(pat[patCount].acc, sqlite3_column_text(res, 1));
	   		strcpy(pat[patCount].pwd, sqlite3_column_text(res, 2));
	   		strcpy(pat[patCount].uname, sqlite3_column_text(res, 3));
	   		strcpy(pat[patCount].role, sqlite3_column_text(res, 4));*/
	   		patCount++;
		} else if (strcmp(sqlite3_column_text(res, 4), "doc") == 0) {
		// add doctor to doctor list
			/*strcpy(doc[docCount].uid, sqlite3_column_text(res, 0));
			printf("Run to here\n");
			strcpy(doc[docCount].acc, sqlite3_column_text(res, 1));
			strcpy(doc[docCount].pwd, sqlite3_column_text(res, 2));
			strcpy(doc[docCount].uname, sqlite3_column_text(res, 3));
			strcpy(doc[docCount].role, sqlite3_column_text(res, 4));*/
			docCount++;
		}
	   	
	   	printf("Number of doctor: %d\n", docCount);
	   	printf("Number of patient: %d\n", patCount);
	   	// get messgage according to username & password
	   	if (strcmp(sqlite3_column_text(res, 4), "doc") == 0) {
		   	strncpy(servmsg, "REG_SUCCESS <true> <doc>", strlen("REG_SUCCESS <true> <doc>"));
	   	} else {
	   		strncpy(servmsg, "REG_SUCCESS <true> <pat>", strlen("REG_SUCCESS <true> <pat>"));
	   	}
	   } else {
	   	printf("Failed to access to server! Check usename or password!\n");
	   	// get messgage according to username & password   		
	   	strncpy(servmsg, "REG_SUCCESS <false>", strlen("REG_SUCCESS <false>"));
	   }
	   
	   sqlite3_finalize(res);
    	   
    	} else if (strcmp(header, "SYMP_REQ") == 0) {
    	
	   strncpy(servmsg, "SYMP_FORM <1><abdominal pain> - <2><ankle problems> - <3><cold and flu> - <4><cough> - <5><hair loss>", strlen("SYMP_FORM <1><abdominal pain> - <2><ankle problems> - <3><cold and flu> - <4><cough> - <5><hair loss>"));
	   
        } else if (strcmp(header, "SYMP_SUBMIT") == 0) {
           for(int i=0; i<strlen(msg); i++) {
           	if(msg[i] == '<' && msg[i+1] != '>') {
           		strncat(listSymp, &msg[i+1], 1);
		}
           }
           printf("List of symptom index is: %s\n", listSymp);
           if(strlen(listSymp) != 0) {
           	strncpy(servmsg, "SYMP_SUCCESS <true>", strlen("SYMP_SUCCESS <true>"));
           	strcpy(copyListSymp, listSymp);
           	printf("Copy list of symptom is: %s\n", copyListSymp);
           	printf("Length of copy list of symptom is: %d\n", strlen(copyListSymp));
           	memset(listSymp, 0, sizeof(listSymp));
           } else {
           	strncpy(servmsg, "SYMP_SUCCESS <false>", strlen("SYMP_SUCCESS <false>"));
           }
	   
    	} else if (strcmp(header, "ANS_REQ") == 0) {
    	   // initialize servmsg
    	   strncpy(servmsg, "ANS_FORM ", strlen("ANS_FORM "));
    	   char *sql = "SELECT qid, detail FROM question where sid = ?";
    	   rc = sqlite3_prepare_v2(db, sql, -1, &res, 0);
    	   
    	   for(int i=0; i<strlen(copyListSymp); ++i) {
    	   	strncat(servmsg, &rightParent, 1);
		strncat(servmsg, &copyListSymp[i], 1);
		strncat(servmsg, &leftParent, 1);
		
    	   	// printf("Index of symptom is: %s\n", indexSymp);
    	   	if(rc == SQLITE_OK) {
    	   		char tmp[2];
    	   		tmp[0] = copyListSymp[i];
    	   		tmp[1] = '\0';
    	   		sqlite3_bind_text(res, 1, tmp, strlen(tmp), NULL);
    	   	} else {
    	   		fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
		}
		
		// int step = sqlite3_step(res);
		while(sqlite3_step(res) == SQLITE_ROW) {
			// printf("Ans needs is: %s\n", sqlite3_column_text(res, 0));
			strncat(servmsg, &rightParent, 1);
			strcat(servmsg, sqlite3_column_text(res, 0));
			strncat(servmsg, &leftParent, 1);
			strncat(servmsg, &rightParent, 1);
			strcat(servmsg, sqlite3_column_text(res, 1));
			strncat(servmsg, &leftParent, 1);
			strncat(servmsg, &nga, 1);
		}
		strncat(servmsg, &ngang, 1);
                // strcat(sermsg, '\n');
    	   }
    	   sqlite3_finalize(res);
    	   // printf("Server msg now: %s\n", servmsg);
	   // strncpy(servmsg, "ANS_FORM <index symptom 1> <ques 1> - <index symptom 2> <ques 2> - <> <> -", strlen("ANS_FORM <index symptom 1> <ques 1> - <index symptom 2> <ques 2> - <> <> -"));
	   
    	} else if(strcmp(header, "ANS_SUBMIT") == 0) {
    	   for(int i=0; i<strlen(msg); ++i) {
    	   	if(msg[i] == 'y') {
    	   		if(isdigit(msg[i-3]) && isdigit(msg[i-4])) {
    	   			strncat(listAns, &msg[i-4], 1);
    	   			strncat(listAns, &msg[i-3], 1);
    	   			strncat(listAns, &ngang, 1);
			} else if(isdigit(msg[i-3])) {
				strncat(listAns, &msg[i-3], 1);
    	   			strncat(listAns, &ngang, 1);
			}
		}
    	   }
    	   printf("List indec answer 'yes' is: %s\n", listAns);
    	   strcpy(copyListAns, listAns);
    	   if(strlen(listAns) != 0) {
	   	strncpy(servmsg, "ANS_SUCCESS <true>", strlen("ANS_SUCCESS <true>"));
	   } else {
	   	strncpy(servmsg, "ANS_SUCCESS <false>", strlen("ANS_SUCCESS <false>"));
	   }
	   memset(listAns, 0, sizeof(listAns));
	   
    	} else if(strcmp(header, "DIG_REQ") == 0) {
    	   strncpy(servmsg, "DIG_ANS ", strlen("DIG_ANS "));
    	   char *sql = "SELECT dname FROM diagnosis where qid = ?";
    	   rc = sqlite3_prepare_v2(db, sql, -1, &res, 0);
    	   
    	   for(int i=0; i<strlen(copyListAns); ++i) {
    	   	char tmp[3];
    	   	
    	   	if(copyListAns[i] == '-') {
    	   		if(isdigit(copyListAns[i-1]) && isdigit(copyListAns[i-2])) {
    	   			strncat(tmp, &copyListAns[i-2], 1);
    	   			strncat(tmp, &copyListAns[i-1], 1);
    	   		} else if(isdigit(copyListAns[i-1])) {
    	   			strncat(tmp, &copyListAns[i-1], 1);
    	   		}
    	   	}
    	   	tmp[strlen(tmp)] = '\0';
    	   	
    	   	if(strlen(tmp) != 0) {
    	   		// printf("tmp is: %s\n", tmp);
    	   		strncat(servmsg, &rightParent, 1);
			strcat(servmsg, tmp);
			strncat(servmsg, &leftParent, 1);
		
    	   		// printf("Index of symptom is: %s\n", indexSymp);
    	   		if(rc == SQLITE_OK) {
    	   			sqlite3_bind_text(res, 1, tmp, strlen(tmp), NULL);
    	   		} else {
    	   			fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(db));
			}
		
			// int step = sqlite3_step(res);
			if(sqlite3_step(res) == SQLITE_ROW) {
				// printf("Ans needs is: %s\n", sqlite3_column_text(res, 0));
				strncat(servmsg, &rightParent, 1);
				strcat(servmsg, sqlite3_column_text(res, 0));
				strncat(servmsg, &leftParent, 1);
			}
			strncat(servmsg, &ngang, 1);
		}
		memset(tmp, 0, sizeof(tmp));
    	   }
    	   
    	   sqlite3_finalize(res);
    	   // printf("Server msg now: %s\n", servmsg);
           // strncpy(servmsg, "DIG_ANS <index symtom 1> <result 1> - <index symtom 2> <result 2> - <> <> - …", strlen("DIG_ANS <index symtom 1> <result 1> - <index symtom 2> <result 2> - <> <> - "));
           
    	} else if(strcmp(header, "QUIT") == 0) {
    	
    	   // accept action for quit session
	   strncpy(servmsg, "QUIT_ACCEPT", strlen("QUIT_ACCEPT"));
	   
    	} else if(strcmp(header, "CONSULT_REQ") == 0) {
    	   
    	   // accept action chat
	   strncpy(servmsg, "CONSULT_PAT <success>", strlen("CONSULT_PAT <success>"));
	   
    	} else if(strcmp(header, "ASSIGN_REQ") == 0) {
    	    
    	    // accept assign doctor
	    strncpy(servmsg, "ASSIGN <success>", strlen("ASSIGN <success>"));
	    
    	} else if(strcmp(header, "DOC_SEND") == 0) {
	    
	    // deal with message doctor sends
	    char docmsg[MAXLINE], tmpDoc[MAXLINE];
	    memset(docmsg, 0, sizeof(docmsg));
	    memset(tmpDoc, 0, sizeof(tmpDoc));
	    
	    int doccount = 0;
	    int doctmp = 0;
	    strncpy(docmsg, "PAT_RECV ", strlen("PAT_RECV "));
	    strncat(docmsg, &rightParent, 1);

	    for (int i=0; i<strlen(msg); ++i) {
	    	if(msg[i] == '<') {
	    		doctmp = 1;
	    	} else if(msg[i] == '>') {
	    		doctmp = 0;
	    	}
	    	
	    	if(doctmp == 1) {
	    		doccount += 1;
	    	}
	    }


	    strncpy(tmpDoc, msg+10, doccount-1);
	    strcat(docmsg, tmpDoc);
	    strncat(docmsg, &leftParent, 1);
	    printf("Message doctor sends to patient: %s\n", docmsg);
	    sendtoother(docmsg, sock);
	    
    	} else if(strcmp(header, "PAT_SEND") == 0) {\
    	
    	    // deal with message patient sends
    	    char patmsg[MAXLINE], tmpPat[MAXLINE];
	    memset(patmsg, 0, sizeof(patmsg));
	    memset(tmpPat, 0, sizeof(tmpPat));
	    
	    int patcount = 0;
	    int pattmp = 0;
	    strncpy(patmsg, "DOC_RECV ", strlen("DOC_RECV "));
	    strncat(patmsg, &rightParent, 1);

	    for (int i=0; i<strlen(msg); ++i) {
	    	if(msg[i] == '<') {
	    		pattmp = 1;
	    	} else if(msg[i] == '>') {
	    		pattmp = 0;
	    	}
	    	
	    	if(pattmp == 1) {
	    		patcount += 1;
	    	}
	    }


	    strncpy(tmpPat, msg+10, patcount-1);
	    strcat(patmsg, tmpPat);
	    strncat(patmsg, &leftParent, 1);
	    printf("Message patient sends to doctor: %s\n", patmsg);
	    sendtoother(patmsg, sock);

    	} else {
            printf("Header not exist!!! Check format header message!");
            strncpy(servmsg, "", strlen(""));
    	}
    	
    	// clear header
    	memset(header, 0, sizeof(header));
    	
    	// clear buffer for message from clients
    	memset(msg, 0, sizeof(msg));
    	// strncpy(msg, "", strlen(""));
    	len = 0;
    	
        // send msg to each client
        printf("Server sends message: %s\n\n", servmsg);
        sendtoclient(servmsg, sock);
        // clear buffer for message to clients
        memset(servmsg, 0, sizeof(servmsg));
        
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
        
        // lock mutex
        pthread_mutex_lock(&mutex);
        printf("Client [%d] with ip [%s] connected successful!\n", clinum, inet_ntoa(cliaddr.sin_addr));
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
