// This is a shell that can handle bg and fg processess. Fg processes are waited on with waitpid. Bg processes are waited
// for with waitpid and WNHOHANG. They can be detected by just calling waitpid or receiving SIGCHILD (with NOCLDSTOP flag)
// and then read waitpid depending on the SIGNALDETECTION macro. Two builtins are employes, cd for changing workdir and exit
// to exit. A handler for SIGINT is employed with the SA_RESTART flag, so that SIGINT will be dropped in the parent process,
// but sent to the child processes (automaticly since they are in the same project group), so that all of them stops.


#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/wait.h>
#include <errno.h>
#include <string.h>
#include <signal.h>
#include <sys/time.h>

#ifndef SIGNALDETECTION
	#define SIGNALDETECTION 1
#endif

int compareStrings(char* word1, char* word2) {
	// This function compare two strings and returns 1 if they are the same
	if (word1==0 || word2==0) {return 0;}

	int i=0;
	while (word1[i]==word2[i]) {
		i++;
		if (word1[i]==0 || word2[i]==0) {break;}
	}

	if (word1[i]==0 && word2[i]==0) {return 1;}
	else {return 0;}
}


void handleSigchild(int sig) {
	// Handle function for SIGCHILD. It waits on every terminated background process in the loop
	int childPID,childExitStatus;
	printf("SIGCHILD received\n");

	while(1) {
		childPID=waitpid(-1,&childExitStatus,WNOHANG);
		if (childPID>0) {
			if (childExitStatus==2) {printf("Background process: %d%s",childPID," terminated by SIGINT\n");}
			else if (childExitStatus!=0) {printf("Background process: %d%s",childPID," unknown command\n");}
			printf("Background process: %d%s\n",childPID," has exited");
		}
		else {break;}
	}
}

void handleSigInt(int sig) {
	// Handle function for SIGINT. When parent receives SIGINT, all its children will receive SIGINT (and terminate)
	// SIGINT will be sent to all processes in the process group = all child processes. We dont want SIGINT to terminate
	// the parent, so nothing here is done. If a fg process is running, waitpid in the fg section will reap the terminated process
	// bg processes will be reaped by the bg wait loop before the new shell prompt.
	printf("SIGINT received\n");
}


int main(int argc, char ** argv) {

	// Sigsets for signal blocking
	sigset_t oldMask, newMask;
	sigemptyset(&newMask);
	sigaddset(&newMask,SIGCHLD);

	// Sigaction for SIGCHILD
	struct sigaction sigchildStruct;
	sigchildStruct.sa_handler = &handleSigchild;
//	sigemptyset(&sigchildStruct.sa_mask);
	sigchildStruct.sa_flags = SA_NOCLDSTOP;

	if (SIGNALDETECTION==1) { // Only register sigaction if the SICNALDETECTION macro is set
		if (sigaction(SIGCHLD, &sigchildStruct, 0) == -1) {
			printf("Couldnt register signal handler: %s\n",strerror(errno));
			exit(1);
		}
	}

	// Sigaction for SIGINT
	struct sigaction sigintStruct;
	sigintStruct.sa_handler = &handleSigInt;
//	sigemptyset(&sigintStruct.sa_mask);
	sigintStruct.sa_flags = SA_RESTART;

	if (sigaction(SIGINT, &sigintStruct, 0) == -1) {
	  printf("Couldnt register signal handler: %s\n",strerror(errno));
	  exit(1);
	}

	struct timeval startTime,stopTime,diff;
	int backgroundCommand=0,childPID,childExitStatus;
	char userInputArgArray[5][70];
	char userInput[70];

	while(1) {
		// First check if any background processes has exited. If SIGNALDETECTION==0, just loop waitpd(-1,&childExitStatus,WHNOHANG)
		// else unblock SIGCHILD and do the same thing in the SIGCHILD handler function
		if (SIGNALDETECTION==0 ) {
			// Wait and waitpid only return for terminated child processes by default

			while(1) {
				childPID=waitpid(-1,&childExitStatus,WNOHANG);
				// Use WNOHANG = dont wait for bg process to finish, just check if its finished
				if (childPID>0) {
					if (childExitStatus==2) {printf("Background process: %d%s",childPID," terminated by SIGINT\n");}
					else if (childExitStatus!=0) {printf("Background process: %d%s",childPID," unknown command\n");}
					printf("Background process: %d%s\n",childPID," has exited");
				}
				else {break;}
			}
		}
		else { // Use signal SIGCHILD detection
			// Remove the SIGCHILD block
			sigprocmask(SIG_SETMASK,&oldMask,NULL);
			// Add the SIGCHILD block
			sigprocmask(SIG_BLOCK,&newMask,&oldMask);
		}

		// Prompt
		printf("ID2206 shell: ");
		if (fgets(userInput,70,stdin)==0) {
			printf("Read error: %s\n",strerror(errno));
			continue;
		}

		if(userInput[0]==10) {continue;} // newline

		if(userInput[0]==32) { // no space allowed
			printf("Commands cannot start with space\n");
			continue;
		}

		// Make argument array
		int i=0,j=0,wordCount=0;
		while(1) {
			if (userInput[i]==10) { // newline
				userInputArgArray[wordCount][j]=0;
				wordCount++;
				break;
			}

			if (userInput[i]==32 || userInput[i]==10) { // space
				userInputArgArray[wordCount][j]=0;
				wordCount++;
				j=0;
				i++;
			}
			userInputArgArray[wordCount][j]=userInput[i];
			i++;
			j++;
		}

		// Check if command is background
		backgroundCommand=0;
		if (userInputArgArray[wordCount-1][0]==38) { // = &
			backgroundCommand=1;
			wordCount--; // Dont send the & as parameter
		}

		// Check if command is builtin
		if (compareStrings("exit",userInputArgArray[0])) {
			// Send SIGINT to all child processes and then reap them before exit
			kill(0,SIGINT);
			while(1) {
				childPID=waitpid(-1,&childExitStatus,0);
				if (childPID>0) {
					if (childExitStatus==2) {printf("Background process: %d%s",childPID," terminated by SIGINT\n");}
					else if (childExitStatus!=0) {printf("Background process: %d%s",childPID," unknown command\n");}
					printf("Background process: %d%s\n",childPID," has exited");
				}
				else {break;}
			}

			exit(0);
		}
		else if (compareStrings("cd",userInputArgArray[0])) {
			if (chdir(userInputArgArray[1])!=0) {
				printf("cd failed: %s\n",strerror(errno));
				chdir(getenv("HOME"));
			}
		}
		else {
			// Fork

			// Build an argument vector from the input array
			char** userInputArgVector = calloc(wordCount, sizeof(char *));
			for (i=0;i<wordCount;i++) {userInputArgVector[i]=userInputArgArray[i];}

			// Get command start time
			gettimeofday(&startTime,NULL);

			// Fork a new child process
			childPID=fork();
			if(-1==childPID) {
				printf("Fork failed");
				exit(1);
			}
			else if (0==childPID) {
				execvp(userInputArgVector[0],userInputArgVector);
				printf("Execvp failed\n");
				_exit(1);
			}

			if (backgroundCommand==0) {printf("Foregorund process: %d%s",childPID," has started\n");}
			else {printf("Background process: %d%s",childPID," has started\n");}

			if(backgroundCommand==0) {
				// Wait for the fg process, only wait for its PID, otherwise this catches bg processes aswell.
				waitpid(childPID,&childExitStatus,0);
				if (childExitStatus==2) {printf("Foreground process: %d%s",childPID," terminated by SIGINT\n");}
				else if (childExitStatus!=0) {printf("Foreground process: %d%s%s",childPID," unknown command: ",userInput);}

				gettimeofday(&stopTime,NULL); // Get command stop time
				timersub(&stopTime, &startTime, &diff); // Calculate difference

				printf("Foreground process: %d%s%ld%s%ld\n",childPID," has exited. Time: ", diff.tv_sec, ".", diff.tv_usec);
			}

			free(userInputArgVector);
		}
	}

	return 0;

}

