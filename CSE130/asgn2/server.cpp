#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <netdb.h>
#include <string.h>
#include <regex.h>
#include <string.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <ctype.h>
#include <queue>
#include <iostream>
#define PORT 80
#define _OPEN_THREADS
#define MAX 16
#define DEFAULT 4

using namespace std;

queue<int> Q;

void *dispatcher(void *);

pthread_mutex_t mutex1 = PTHREAD_MUTEX_INITIALIZER;

void requestAction(int data)
{
    char buffer[1024] = {0};
    char content_header[30];
    char header[40];
    char protocol[5];
    char c_length[50];
    int valread, fd, new_socket;
    regex_t regex;
    char const *ok = "HTTP/1.1 200 OK\r\n\n";
    char const *created = "HTTP/1.1 201 Created\r\n\n";
    char const *bad = "HTTP/1.1 400 Bad Request\r\n\n";
    char const *forbidden = "HTTP/1.1 403 Forbidden\r\n\n";
    char const *not_found = "HTTP/1.1 404 Not Found\r\n\n";
    char const *ise = "HTTP/1.1 Internal Server Error\r\n\n";

    new_socket = data;
    valread = recv(new_socket, buffer, sizeof(buffer), 0);
    //parsing for protocol and file name
    sscanf(buffer, "%s %s", protocol, content_header);
    sprintf(header, "%s %s", protocol, content_header);
    memcpy(header, header, strlen(header));
    //creating a regex to account for improper file names
    int reti = regcomp(&regex, "[^a-zA-Z0-9_-]", 0);
    reti = regexec(&regex, content_header, 0, NULL, 0);
    if (strcmp(protocol, "GET") == 0) 
    {
            //sending a bad request
        if (reti == 0 || strlen(content_header) < 27 || 
            strlen(content_header) > 27)
        {
            send(new_socket, bad, strlen(bad), 0);
            close(new_socket);
        }
        fd = open(content_header, O_RDONLY);
            //checking if the file exists
        if (fd == -1)
        {
            send(new_socket, not_found, strlen(not_found), 0);
            close(new_socket);
        }
            //checking read permissions
        if (access(content_header, R_OK) == -1)
        {
            send(new_socket, not_found, strlen(not_found), 0);
            close(new_socket);
        }
        write(1, buffer, strlen(buffer)-1);
        valread = read(fd, buffer, sizeof(buffer));
            //used for returning Content-Length
        sprintf(c_length, "Content-Length: %d\n\n", valread);
        write(1, c_length, strlen(c_length));
        send(new_socket, ok, strlen(ok), 0);
        send(new_socket, buffer, valread, 0); 
        memset(buffer, 0, sizeof(buffer));
        close(fd);
    }
    else if (strcmp(protocol, "PUT") == 0)
    {
        if (reti == 0 || strlen(content_header) < 27 || 
            strlen(content_header) > 27)
        {
            send(new_socket, bad, strlen(bad), 0);
            close(new_socket);
        }
            //checking if the file exists
        if (access(content_header, F_OK) == -1)
        {
            send(new_socket, created, strlen(created), 0);
        }
            //checking write permissions
        else if (access(content_header, W_OK) == -1)
        {
            send(new_socket, forbidden, strlen(forbidden), 0);
            close(new_socket);
        }
        else
        {
            send(new_socket, ok, strlen(ok), 0);
        }
        write(1, buffer, strlen(buffer));
        fd = open(content_header, O_TRUNC | O_WRONLY | O_CREAT);
        valread = recv(new_socket, buffer, sizeof(buffer), 0);
        write(fd, buffer, valread);
        memset(buffer, 0, sizeof(buffer));
        close(fd);
    }
    else 
        //if any other requests besides PUT or GET
    {
        send(new_socket, ise, strlen(ise), 0);
    }
    close(new_socket);
}

int main (int argc, char *argv[]) {
    int server_fd, new_socket, c, nThreads;
    struct sockaddr_in address;
    int addrlen = sizeof(address);
    int lflag = 0;
    int nflag = 0;
    char *nThreadsArg = NULL;
    char *logfile = NULL;

    opterr = 0;
    //parse for options on the command line
    while ((c = getopt(argc, argv, "l:N:")) != -1)
        switch (c)
    {
        case 'l':
            lflag = 1;
            logfile = optarg;
            break;
        case 'N':
            nflag = 1;
            nThreadsArg = optarg;
            nThreads = *nThreadsArg - '0';
        break;
        case '?':
        if (optopt == 'c')
            fprintf(stderr, "Option -%c requires an argument.\n", optopt);
        else if (isprint (optopt))
            fprintf(stderr, "Uknown option `-%c'.\n", optopt);
        else
            fprintf(stderr, "Unknown option character `\\x%x'.\n", optopt);
        return 1;
        default:
            abort();
    }

    //creating the server
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) 
    {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }

    address.sin_family = AF_INET;
    
    //checking for the arguments prompted by the user
    if (argc >= 3)
    {
        //changing localhost to IP address
        if (strcmp(argv[optind], "localhost") == 0)
        {
            address.sin_addr.s_addr = inet_addr("127.0.0.1");
        } 
        //using IP address to establish connection with server
        //and client
        else
        {
            address.sin_addr.s_addr = inet_addr(argv[1]);
        }
        //setting desired port 
        if (argv[++optind] == NULL)
        {
            address.sin_port = htons(PORT);
        } else 
        {
            int port = atoi(argv[optind]);
            address.sin_port = htons(port);
        }
    }
    else if (argc == 2)
    {
        if (strcmp(argv[1], "localhost") == 0)
        {
            address.sin_addr.s_addr = inet_addr("127.0.0.1");
        } 
        else
        {
            address.sin_addr.s_addr = inet_addr(argv[1]);
        }
        //dafulting to defined port
        address.sin_port = htons(PORT);
    }
    else 
    {
        perror("usage");
        exit(1);
    }  
    //forcefully attaching socket to the port
    if (bind(server_fd, (struct sockaddr *)&address, 
        sizeof(address)) < 0) 
    {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }
    if (listen(server_fd, 3) < 0)
    {
        perror("listen");
        exit(EXIT_FAILURE);
    }
    pthread_t threads[MAX];
    if (nThreadsArg == NULL) nThreads = DEFAULT;
    for (int i = 0; i < nThreads; i++)
    {
        pthread_create(&threads[i], NULL, dispatcher, NULL);
    }
    //creating a while loop to allow for multiple requests 
    while (1)
    {
        if ((new_socket = accept(server_fd, (struct sockaddr *)&address,
         (socklen_t*)&addrlen)) < 0)
        {
            perror("accept");
            exit(EXIT_FAILURE);
        }
        //pushing sockets to a queue to allow threads to access
        Q.push(new_socket);

    }
    return 0;
}

//Dispatcher thread used to allow for multithreading
void *dispatcher(void *)
{
    while (1) 
    {
        //lock the mutex while in the critical region
        pthread_mutex_lock(&mutex1);
        if (Q.size() > 0)
        {
            int data = Q.front();
            requestAction(data);
            Q.pop();
        }
        //free up the mutex after exiting critical region
        pthread_mutex_unlock(&mutex1);
    }
}