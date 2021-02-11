#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>

char* read_cmdline(void);
char** parse_cmdline(const char*);

int main(){
    char* cmd;
    char** args;
    char* dollar = "$ ";

    while(1){
        write(STDOUT_FILENO, dollar, strlen(dollar));

        int cmd_size = 0;
        char symbol;

        do{
            ssize_t read_stat = read(STDIN_FILENO, &symbol, 1);

            if (read_stat == 0){
                free(cmd);
                break;
            }

            if (read_stat == -1){
                free(cmd);
                perror("read");
                break;
            }

            cmd_size ++;
            cmd = (char*) realloc(cmd, cmd_size);

            cmd[cmd_size-1] = symbol;
        }
        while(symbol != '\n');


        if (cmd == NULL)
            return 0;

        args = parse_cmdline(cmd);

        int stat;
        pid_t curr_pid;

        curr_pid = fork();

        if (curr_pid < 0)
            perror("fork");

        else if (curr_pid == 0){
            if (execv(*args, args) == -1)
                perror(*args);

            int i = 0;
            while(args[i] != NULL){
                free(args[i]);
                i ++;
            }

            free(args);
            free(cmd);
            return 0;
        }

        else{
            pid_t w_stat = waitpid(curr_pid, &stat, 0);

            if (w_stat == -1)
                perror("wait");
        }

        int i = 0;
        while(args[i] != NULL){
            free(args[i]);
            i ++;
        }

        free(args);
        free(cmd);
    }


    return 0;
}

char** parse_cmdline(const char* cmdline){
    char** parsed_cmdline = NULL;
    int args_num = 0, sngl_arg_size = 0, has_space = 0;

    for(int idx = 0; cmdline[idx] != '\n'; idx ++){
        if (cmdline[idx] == ' ' || idx == 0){
            if (!has_space){
                args_num ++;
                parsed_cmdline = (char**) realloc(parsed_cmdline, args_num*sizeof(char*));
                parsed_cmdline[args_num-1] = NULL;
                sngl_arg_size = 0;

                if (idx == 0){
                    sngl_arg_size ++;
                    parsed_cmdline[args_num-1] = (char*) realloc(parsed_cmdline[args_num-1], sngl_arg_size*sizeof(char));
                    parsed_cmdline[args_num-1][sngl_arg_size-1] = cmdline[idx];
                }
            }

            has_space = 1;
        }
        else{
            sngl_arg_size ++;
            parsed_cmdline[args_num-1] = (char*) realloc(parsed_cmdline[args_num-1], sngl_arg_size*sizeof(char));
            parsed_cmdline[args_num-1][sngl_arg_size-1] = cmdline[idx];
            has_space = 0;
        }
    }

    args_num ++;
    parsed_cmdline = (char**) realloc(parsed_cmdline, args_num*sizeof(char*));
    parsed_cmdline[args_num-1] = NULL;

    return parsed_cmdline;
}
