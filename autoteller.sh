#!/bin/bash

SHELL_DIR=`pwd`
echo $SHELL_DIR
RELATIVE_SH_DIR=$(dirname $0)
SH_FILE_DIR=$SHELL_DIR/$RELATIVE_SH_DIR
echo $SH_FILE_DIR
cd $RELATIVE_SH_DIR/AutoTeller
nohup mvn tomcat7:run > $SH_FILE_DIR/autoteller.out &
AUTOTELLER_PID=$!
echo $AUTOTELLER_PID
echo $AUTOTELLER_PID > $SH_FILE_DIR/autoteller.pid

#cd $SH_FILE_DIR/OpsMonitor
#nohup mvn tomcat7:run > $SH_FILE_DIR/opsmonitor.out &
