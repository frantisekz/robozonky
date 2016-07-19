#!/bin/bash
BASEDIR=$(dirname "$0")
$JAVA_HOME/bin/java -Dlogback.configurationFile=logback.xml -Dfile.encoding="UTF-8" -Djava.net.preferIPv4Stack=true -Xmx64m -jar $BASEDIR/robozonky-app-${project.version}.jar "$@"
