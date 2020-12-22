#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <string.h>
#include <regex.h>
#include <string.h>
#define PORT 8080

int main (int argc, char *argv[]) {
    int server_fd, fd, new_socket, valread;
    struct sockaddr_in address;
    int opt = 1;
    int addrlen = sizeof(address);
    char buffer[1024] = {0};
    char content_header[30];
    char protocol[5];
    char header[42];
    char c_length[10];
    char *ok = "HTTP/1.1 200 OK\n";
    char *content_length = "Content-Length: ";
    regex_t regex;
    
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) 
    {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }
    
    if (argc == 3)
    {
        int port = atoi(argv[2]);
        address.sin_port = htons(port);
    }
    else if (argc == 2)
    {
        address.sin_port = htons(PORT);
    }
    else 
    {
        perror("usage");
        exit(1);
    }

    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    

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
    while (1){
        if ((new_socket = accept(server_fd, (struct sockaddr *)&address,
                               (socklen_t*)&addrlen)) < 0)
        {
            perror("accept");
            exit(EXIT_FAILURE);
        }
        valread = recv(new_socket, buffer, sizeof(buffer), 0);
        sscanf(buffer, "%s %s", protocol, content_header);
        int reti = regcomp(&regex, "[^a-zA-Z0-9_-]", 0);
        reti = regexec(&regex, content_header, 0, NULL, 0);
        if (reti == 0 || strlen(content_header) < 27 || 
                            strlen(content_header) > 27)
        {
            perror("content header");
            exit(1);
        }
        write(1, buffer, sizeof(header));
        if (strcmp(protocol, "GET") == 0) 
        {
            fd = open(content_header, O_RDONLY);
            valread = read(fd, buffer, sizeof(buffer));
            itoa(valread, c_length, 0);
            printf("%s\n", c_length);
            send(new_socket, buffer, valread, 0); 
            send(new_socket, ok, strlen(ok), 0);
            close(fd);
        }
        if (strcmp(protocol, "PUT") == 0)
        {
            //printf("%s\n", content_header);
            valread = recv(new_socket, buffer, sizeof(buffer), 0);
            fd = open(content_header, O_TRUNC | O_WRONLY | O_CREAT);
            write(fd, buffer, valread);
            send(new_socket, ok, strlen(ok), 0);
            close(fd);
            //printf("Hello message sent\n");
        }
        close(new_socket);
    }
    return 0;
}
