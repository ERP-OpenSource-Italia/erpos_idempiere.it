#!/bin/bash
# run.sh - Start FitNesse.
# try "run.sh -h" for help.

function help
{
	echo "Usage: run.sh [-p port] [-d dir] [-r root] [-l logDir] [-e days] [-o] [-a userpass] [-J jvm_option ...]
	-e <days>                          Number of days before page versions expire (default: 14).
	-o omit updates                    Don't do updates from remote wikis, if any.
	-a {user:pwd | user-file-name}     Enable authentication.
	-J <jvm option>                    Arguments to pass to the JVM (can be repeated).
	-h                                 This help message. Exits after printing..."
}

declare -a jvm_args
declare -a fitnesse_args
while [ $# -gt 0 ]
do
	case $1 in
		-h|-help)
			help "$@"
			exit 0
			;;
		-J)
			shift
			jvm_args[${#jvm_args[*]}]=$1
			;;
		-[pdrleoa]*)
			fitnesse_args[${#fitnesse_args[*]}]=$1
			shift
			fitnesse_args[${#fitnesse_args[*]}]=$1
			;;
		*)
			echo "Unknown argument specified: $1"
			help "$@"
			exit 1
			;;
	esac
	shift
done

export fitnesse_home=`dirname $0`
eval "export `grep ADEMPIERE_WEB_PORT $fitnesse_home/../idempiereEnv.properties`"

jvm_args[${#jvm_args[*]}]="-jar"
jvm_args[${#jvm_args[*]}]="fitnesse.jar"

echo java ${jvm_args[*]} ${fitnesse_args[*]}
java -Xmx100M ${jvm_args[*]} -p 8089 -l log ${fitnesse_args[*]}
