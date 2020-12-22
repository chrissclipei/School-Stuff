#include<stdio.h>
// Importing the POSIX regex library
#include <regex.h> 
int main() {
    regex_t regex;
    int return_value;
    return_value = regcomp(&regex,"[^a-zA-Z0-9_-]",0);
    if (return_value == 0){
        printf("Regular expression compiled successfully.");
    }
    else{
        printf("Compilation error.");
    }
    return 0;
}
