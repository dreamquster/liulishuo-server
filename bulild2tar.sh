#!/bin/bash

SHELL_DIR=`pwd`
RELATIVE_SH_DIR=$(dirname $0)
SH_FILE_DIR=$SHELL_DIR/$RELATIVE_SH_DIR

package_proj() {
	proj=$1
	cd $SH_FILE_DIR/$proj
	mvn package
	JAR_FILE=$SH_FILE_DIR/$proj/target/$proj.jar;
	if [ -s $JAR_FILE ]; then
		echo "yes" | cp -rf $SH_FILE_DIR/$proj/target/$proj.jar $SH_FILE_DIR/libs
	else
		echo $proj" failed package"
		exit 1
	fi
}

if [ ! -d $SH_FILE_DIR/libs ]; then
	mkdir -p $SH_FILE_DIR/libs
fi

package_proj AutoTeller
package_proj OpsMonitor

cd $SH_FILE_DIR
tar -czvf liulishuo.tgz ./libs/* ./autoteller.sh