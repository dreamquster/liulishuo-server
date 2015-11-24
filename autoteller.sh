#!/bin/bash

SHELL_DIR=`pwd`
RELATIVE_SH_DIR=$(dirname $0)
SH_FILE_DIR=$SHELL_DIR/$RELATIVE_SH_DIR
echo $SH_FILE_DIR

check_ok_url() {
	url=$1
	curl -s --head $url | head -n 1 | grep "HTTP/1.[01] [23].."
	if [ $? -eq 0 ]; then
		echo "autoteller(8080) is running with OK status!"
	else
		/bin/echo -e "\e[1;31m""There is no webapp which serve "$url"\e[0m"
	fi
}

start_with_mvn_tomcat() {
	echo "start webapp "$2" in "$1
	mvn_proj_dir=$1
	name=$2
	cur_dir=`pwd`
	
	cd $mvn_proj_dir
	nohup java -jar libs/$2.jar > ./$name.out 2>&1 &
	echo $! > ./$name.pid
	echo "sucess start "$2
	cd $cur_dir
}

stop_tomcat() {
	echo "stop webapp "$2"in $1"
	mvn_proj_dir=$1
	name=$2
	cur_dir=`pwd`
	
	cd $mvn_proj_dir
	cat ./$name.pid | xargs kill -9
	
	cd $cur_dir
}

restart_proj() {
	mvn_proj_dir=$1
	name=$2
	cur_dir=`pwd`
	
	stop_tomcat $mvn_proj_dir $name
	start_with_mvn_tomcat $mvn_proj_dir $name
	cd $cur_dir
}

usage() {
	echo "autoteller.sh [start|stop|restart|status] "
}



main() {
	for key in "$@"
	do
	case $key in
		"start")
			echo "start liulishuo app"
			start_with_mvn_tomcat $SH_FILE_DIR AutoTeller
			start_with_mvn_tomcat $SH_FILE_DIR OpsMonitor
			;;
		"stop")
			echo "stop liulishuo app"
			stop_tomcat $SH_FILE_DIR AutoTeller
			stop_tomcat $SH_FILE_DIR OpsMonitor
			;;
		"restart")
			echo "restart app"
			restart_proj $SH_FILE_DIR AutoTeller
			restart_proj $SH_FILE_DIR opsmonitor
			;;
		"status")
			echo "test whether webapp which is listen on 8080 is ok"
			check_ok_url http://127.0.0.1:8080/ok
			;;
		*)
			usage
			;;
	esac
	done
}

main $*