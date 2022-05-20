@Echo	Synchronize iDempiere Database

@Rem 
@Echo Upgrading database %1@%ADEMPIERE_DB_NAME%

@if (%IDEMPIERE_HOME%) == () goto environment
@if (%ADEMPIERE_DB_NAME%) == () goto environment
@if (%ADEMPIERE_DB_SERVER%) == () goto environment
@if (%ADEMPIERE_DB_PORT%) == () goto environment
@Rem Must have parameter: userAccount
@if (%1) == () goto usage

@Echo WARNING: THIS PROGRAM IS NOT DEVELOPED FOR WINDOWS YET - YOU NEED TO APPLY MANUALLY THE PENDING MIGRATION SCRIPTS

@Rem TODO: port oracle/SyncDB.sh to windows syntax

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		IDEMPIERE_HOME	e.g. D:\Adempiere
@Echo		ADEMPIERE_DB_NAME 	e.g. adempiere.adempiere.org

:usage
@echo Usage:	%0 <userAccount>
@echo Examples:	%0 adempiere adempiere

:end
