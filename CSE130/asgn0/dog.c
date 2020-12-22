#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>

int main(int argc, char *argv[]) {
    
    //integer for file descriptor
    int fd;
    //max buffer range set
    char buf[32000];

    //if dog is the only argument
    if (argc == 1) {
        int rsize;
        //looping until Ctrl+D is inputted which signals an EOF
        while ((rsize = read(0, buf, sizeof(buf))) != 0) {
            write(1, buf, rsize);
        }
    } else {
        //setting errno to default for error message handling
        errno = 0;
        int rsize, wsize;
        for (int i = 1; i < argc; i++) {
            if (argv[i][0] == '-') {
                //prints back user's input until EOF is signaled
                while ((rsize = read(0, buf, sizeof(buf))) != 0) {
                    write(1, buf, rsize);
                }
                continue;
            }
            fd = open(argv[i], O_RDONLY);
            if (fd != -1) {
                rsize = read(fd, buf, sizeof(buf));
                fprintf(stderr, "size of read %s\n", &buf[0]);
                //handles the case in which read returns an error
                if (rsize == -1) {
                    fprintf(stderr, "dog: %s: ", argv[i]);
                    perror("");
                } else {
                    wsize = write(1, buf, rsize);
                    //handles the case in which write returns less data than what is read
                    if (wsize < rsize) {
                        //input/output error
                        errno = 5;
                        fprintf(stderr, "dog: write error: ");
                        perror("");
                    }
                }
            } else {
                //if any other errors occur while opening the file
                fprintf(stderr, "dog: %s: ", argv[i]);
                perror("");
            }
        }
    }
    return 0;
}
