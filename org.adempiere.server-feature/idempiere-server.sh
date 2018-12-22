#!/bin/sh
#
unset DISPLAY
BASE=`dirname $( readlink -f $0 )`
. $BASE/utils/myEnvironment.sh Server
if [ $JAVA_HOME ]; then
  JAVA=$JAVA_HOME/bin/java
else
  JAVA=java
  echo JAVA_HOME is not set.
  echo You may not be able to start the server
  echo Set JAVA_HOME to the directory of your local JDK.
fi

if [ "$1" = "debug" ]; then
  DEBUG="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=4554,server=y,suspend=n"
fi

echo ===================================
echo Starting iDempiere Server
echo ===================================

# if don't set from service get default value
TELNET_PORT=${TELNET_PORT:-12612}


VMOPTS="-Dorg.osgi.framework.bootdelegation=sun.security.ssl,org.w3c.dom.events
-Dosgi.compatibility.bootdelegation=true
-Djetty.home=$BASE/jettyhome
-Djetty.base=$BASE/jettyhome
-Djetty.etc.config.urls=etc/jetty.xml,etc/jetty-deployer.xml,etc/jetty-ssl.xml,etc/jetty-ssl-context.xml,etc/jetty-http.xml,etc/jetty-alpn.xml,etc/jetty-http2.xml,etc/jetty-https.xml
-Dosgi.console=localhost:$TELNET_PORT
-Dmail.mime.encodefilename=true
-Dmail.mime.decodefilename=true
-Dmail.mime.encodeparameters=true
-Dmail.mime.decodeparameters=true
--add-exports java.desktop/sun.awt=ALL-UNNAMED
--add-exports java.sql.rowset/com.sun.rowset=ALL-UNNAMED
--add-exports java.naming/com.sun.jndi.ldap=ALL-UNNAMED
--add-modules=ALL-SYSTEM"

$JAVA ${DEBUG} $IDEMPIERE_JAVA_OPTIONS $VMOPTS -jar $BASE/plugins/org.eclipse.equinox.launcher_1.*.jar -application org.adempiere.server.application
