#!/bin/sh

echo    Adempiere Database Export       $Revision: 1.5 $

# $Id: ExportReference.sh,v 1.5 2005/12/20 07:12:17 jjanke Exp $

echo Saving reference database reference@$ADEMPIERE_DB_NAME to $IDEMPIERE_HOME/data/Adempiere.dmp

if [ "$IDEMPIERE_HOME" = "" -o  "$ADEMPIERE_DB_NAME" = "" ]
  then
    echo "Please make sure that the environment variables are set correctly:"
    echo "      IDEMPIERE_HOME  e.g. /idempiere"
    echo "      ADEMPIERE_DB_NAME       e.g. adempiere.adempiere.org"
    exit 1
fi

echo -------------------------------------
echo Re-Create DataPump directory
echo -------------------------------------
sqlplus $3@$ADEMPIERE_DB_SERVER:$ADEMPIERE_DB_PORT/$ADEMPIERE_DB_NAME @$IDEMPIERE_HOME/utils/$ADEMPIERE_DB_PATH/CreateDataPumpDir.sql $IDEMPIERE_HOME/data
chgrp dba $IDEMPIERE_HOME/data
chmod 770 $IDEMPIERE_HOME/data

sqlplus $3@$ADEMPIERE_DB_SERVER:$ADEMPIERE_DB_PORT/$ADEMPIERE_DB_NAME <<!
alter session set "_enable_rename_user"=true;
alter system enable restricted session;
ALTER USER $1 RENAME TO REFERENCE IDENTIFIED BY "$2";
alter system disable restricted session;
!

# Export
expdp REFERENCE/$2@$ADEMPIERE_DB_SERVER:$ADEMPIERE_DB_PORT/$ADEMPIERE_DB_NAME DIRECTORY=ADEMPIERE_DATA_PUMP_DIR DUMPFILE=Adempiere.dmp LOGFILE=Adempiere.log EXCLUDE=STATISTICS SCHEMAS=REFERENCE

sqlplus $3@$ADEMPIERE_DB_SERVER:$ADEMPIERE_DB_PORT/$ADEMPIERE_DB_NAME <<!
alter session set "_enable_rename_user"=true;
alter system enable restricted session;
ALTER USER REFERENCE RENAME TO $1 IDENTIFIED BY "$2";
alter system disable restricted session;
!

cd $IDEMPIERE_HOME/data
jar cvfM Adempiere.jar Adempiere.dmp Adempiere.log
