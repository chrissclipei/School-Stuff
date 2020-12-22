#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
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
#include <ctype.h>
#include <vector>
#include <bits/stdc++.h>
#include <iostream>
#define PORT 80

using namespace std;

vector<string> cache_filename;
vector<string> cache_content;
vector<int> cache_size;

void logging(int log_fd, char *log_header, char *buffer, 
    int valread, int caching, int cflag)
{
    char const *nl = "\n";
    char const *eof = "========\n";
    char request[5];
    char filename[30];
    char hex[10];
    char temp[100];
    sscanf(log_header, "%s %s", request, filename);
    if (cflag == 1 && caching == 1)
    {
        sprintf(temp, "%s %s length 0 ", request, filename);
        sprintf(temp + strlen(temp), "[was in cache]\n");
        write(log_fd, temp, strlen(temp));
    } else
    {
        sprintf(temp, "%s %s length %d ", request, filename, valread);
        if (cflag == 1 && caching == 0) 
            sprintf(temp + strlen(temp), "[was not in cache]\n");
        else
            sprintf(temp + strlen(temp), "\n");
        write(log_fd, temp, strlen(temp));
        if (valread > 0)
        {
            sprintf(hex, "%08d ", 0);
            write(log_fd, hex, strlen(hex));
            for (int i = 0; i < valread; i++)
            {
                if (i % 20 == 0 && i != 0)
                {
                    write(log_fd, nl, strlen(nl));
                    sprintf(hex, "%08d ", i);
                    write(log_fd, hex, strlen(hex));
                }
                sprintf(hex, "%02X ", buffer[i]);
                write(log_fd, hex, strlen(hex));
            }
            write(log_fd, nl, strlen(nl));
        }
    }
    write(log_fd, eof, strlen(eof));
}

int main (int argc, char *argv[]) {
    int server_fd, fd, log_fd, new_socket, valread, c, file_length;
    int lflag = 0;
    int cflag = 0;
    struct sockaddr_in address;
    struct stat fileStat;
    int addrlen = sizeof(address);
    char buffer[1024] = {0};
    char log_header[60];
    char content_header[30];
    char protocol[5];
    char http[10];
    char filesize[10];
    char c_length[50];
    char const *ok = "HTTP/1.1 200 OK\r\n\n";
    char const *created = "HTTP/1.1 201 Created\r\n\n";
    char const *bad = "HTTP/1.1 400 Bad Request\r\n\n";
    char const *forbidden = "HTTP/1.1 403 Forbidden\r\n\n";
    char const *not_found = "HTTP/1.1 404 Not Found\r\n\n";
    char const *ise = "HTTP/1.1 Internal Server Error\r\n\n";
    regex_t regex;


    cache_filename.reserve(4);
    cache_content.reserve(4);
    cache_size.reserve(4);

    opterr = 0;
    //parse for options on the command line
    while ((c = getopt(argc, argv, "l:c")) != -1)
        switch (c)
    {
        case 'l':
        lflag = 1;
        log_fd = open(optarg, O_TRUNC | O_WRONLY | O_CREAT, 0666);
        break;
        case 'c':
        cflag = 1;
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
    //creating a while loop to allow for multiple requests 
    while (1){
        if ((new_socket = accept(server_fd, (struct sockaddr *)&address,
           (socklen_t*)&addrlen)) < 0)
        {
            perror("accept");
            exit(EXIT_FAILURE);
        }
        memset(buffer, 0, sizeof(buffer));
        valread = recv(new_socket, buffer, sizeof(buffer), 0);
        //parsing for protocol and file name
        sscanf(buffer, "%s %s %s", protocol, content_header, http);
        sprintf(log_header, "%s %s %s", protocol, content_header, http);
        //printf("%s\n", &buffer[118]);
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
                continue;
            }
            fd = open(content_header, O_RDONLY);
            fstat(fd, &fileStat);
            //checking if the file exists
            if (fd == -1)
            {
                send(new_socket, not_found, strlen(not_found), 0);
                close(new_socket);
                continue;
            }
            //checking read permissions
            if (access(content_header, R_OK) == -1)
            {
                send(new_socket, not_found, strlen(not_found), 0);
                close(new_socket);
                continue;
            }
            write(1, buffer, strlen(buffer)-1);
            memset(buffer, 0, sizeof(buffer));
            int check = 0;
            //Check for cached request
            if (cflag == 1 && cache_filename.size() != 0)
            {
                auto j = cache_content.begin();
                auto k = cache_size.begin();
                //Iterate through the vector to check for request
                for (auto i = cache_filename.begin(); 
                        i != cache_filename.end() && check != 1; ++i)
                {
                    if (strcmp(content_header, i->c_str()) == 0)
                    {
                        sprintf(c_length, "Content-Length: %d\n\n", *k);
                        write(1, c_length, strlen(c_length));
                        send(new_socket, ok, strlen(ok), 0);
                        send(new_socket, j->c_str(), *k, 0);
                        if (lflag == 1) 
                            logging(log_fd, log_header, NULL, valread, 
                                    1, cflag);
                        check = 1;
                    }
                    j++;
                    k++;
                }
            }
            //If cached request found, then continue to next request
            if (check == 1)
            {
                close(new_socket);
                continue;
            }
            valread = read(fd, buffer, fileStat.st_size);
            //used for returning Content-Length
            sprintf(c_length, "Content-Length: %d\n\n", valread);
            write(1, c_length, strlen(c_length));
            //If caching enabled, then erase or add to vectors accordingly
            if (cflag == 1){
                if (cache_filename.size() == 4)
                {
                    cache_filename.erase(cache_filename.begin());
                    cache_content.erase(cache_content.begin());
                    cache_size.erase(cache_size.begin());
                }
                cache_filename.push_back(content_header);
                cache_content.push_back(buffer);
                cache_size.push_back(valread);
            }
            if (lflag == 1) 
                logging(log_fd, log_header, buffer, valread, 0, cflag);
            send(new_socket, ok, strlen(ok), 0);
            send(new_socket, buffer, valread, 0);
            close(fd);
        }
        else if (strcmp(protocol, "PUT") == 0)
        {
            sscanf(&buffer[118], "%s", filesize);
            file_length = atoi(filesize);
            if (reti == 0 || strlen(content_header) < 27 || 
                strlen(content_header) > 27)
            {
                send(new_socket, bad, strlen(bad), 0);
                close(new_socket);
                continue;
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
                continue;
            }
            else
            {
                send(new_socket, ok, strlen(ok), 0);
            }
            write(1, buffer, strlen(buffer));
            fd = open(content_header, O_TRUNC | O_WRONLY | O_CREAT);
            int check = 0;
            //Checking for cached request 
            if (cflag == 1 && cache_filename.size() != 0)
            {
                auto j = cache_content.begin();
                auto k = cache_size.begin();
                //Iterating through the vectors to check for cached request
                for (auto i = cache_filename.begin(); 
                        i != cache_filename.end() && check != 1; ++i)
                {
                    if (strcmp(content_header, i->c_str()) == 0 && 
                        file_length == *k)
                    {
                        write(fd, j->c_str(), strlen(j->c_str()));
                        close(fd);
                        if (lflag == 1) 
                            logging(log_fd, log_header, NULL, valread, 
                                    1, cflag);
                        check = 1;
                    }
                    ++j;
                    ++k;
                }
            }
            if (check == 1)
            {
                close(new_socket);
                continue;
            }
            memset(buffer, 0, sizeof(buffer));
            valread = recv(new_socket, buffer, file_length, 0);
            write(fd, buffer, valread);
            check = 0;
            if (cflag == 1){
                auto j = cache_content.begin();
                auto k = cache_size.begin();
                //Iterating through to update cached request
                for (auto i = cache_filename.begin(); 
                        i != cache_filename.end() && check != 1; ++i)
                {
                    if (strcmp(content_header, i->c_str()) == 0 && 
                        file_length != *k)
                    {
                        cache_filename.erase(i);
                        cache_content.erase(j);
                        cache_size.erase(k);
                        check = 1;
                    }
                    ++j;
                    ++k;
                }
                if (cache_filename.size() == 4)
                {
                    cache_filename.erase(cache_filename.begin());
                    cache_content.erase(cache_content.begin());
                    cache_size.erase(cache_size.begin());
                }
                cache_filename.push_back(content_header);
                cache_content.push_back(buffer);
                cache_size.push_back(valread);
            }
            if (lflag == 1) 
                logging(log_fd, log_header, buffer, valread, 0, cflag);
            close(fd);
        }
        else 
        //if any other requests besides PUT or GET
        {
            send(new_socket, ise, strlen(ise), 0);
        }
        close(new_socket);
    }
    return 0;
}
