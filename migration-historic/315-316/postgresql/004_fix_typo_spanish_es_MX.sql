UPDATE ad_message_trl
   SET msgtext = 'de'
 WHERE ad_message_id = 620 AND ad_language = 'es_MX';

UPDATE ad_process_trl
   SET NAME = 'Crear Columnas desde BD'
 WHERE ad_process_id = 173 AND ad_language = 'es_MX';

UPDATE ad_field_trl
   SET NAME = 'Crear Columnas desde BD'
 WHERE ad_field_id = 5126 AND ad_language = 'es_MX';

UPDATE ad_menu_trl
   SET NAME = 'Factura de solicitud'
 WHERE ad_menu_id = 535 AND ad_language = 'es_MX';

UPDATE ad_element_trl
   SET NAME = 'Direcci�n',
       printname = 'Direcci�n',
       description =
               'Direcci�n de la Tarjeta de Cr�dito o el Poseedor de la cuenta'
 WHERE ad_element_id = 1356 AND ad_language = 'es_MX';

UPDATE ad_column_trl
   SET NAME = 'Direcci�n'
 WHERE ad_column_id = 5234 AND ad_language = 'es_MX';

UPDATE ad_field_trl
   SET NAME = 'Direcci�n',
       description =
               'Direcci�n de la Tarjeta de Cr�dito o el Poseedor de la cuenta'
 WHERE ad_field_id = 4102 AND ad_language = 'es_MX';

UPDATE ad_element_trl
   SET NAME = 'Descripci�n del Contacto',
       printname = 'Descripci�n del Contacto'
 WHERE ad_element_id = 1907 AND ad_language = 'es_MX';

UPDATE ad_field_trl
   SET NAME = 'Descripci�n del Contacto'
 WHERE ad_field_id = 5946 AND ad_language = 'es_MX';

UPDATE ad_workflow_trl
   SET NAME = 'Configuraci�n de Replicaci�n',
       istranslated = 'Y',
       description = 'Configuraci�n de replicaci�n de datos',
       HELP =
          'La replicaci�n de datos le permite sincronizar datos de instancias remotas con una oficina central.  Todas las acciones son inicializadas desde la oficina central.<p>
<b>Sistema Central:</b><br>
- configurar el sistema con todas las organizaciones, roles, etc.<br>
- correr la migraci�n para asegurar que todo est� actualizado<br>
- exportar el sistema central
<p>
<b>Sistema Remoto:</b>
- instalar la versi�n exacta como en el sistema central<br>
- importar datos del sistema central en las localizaciones remotas
<p>
<b>Sistema Central:</b>
- Definir Replicaci�n por cada Remoto<br>
- Asegurarse que el rango de IDs es �nico por remoto - de lo contrario se perder�n transacciones!<br>
- Inicialmente empezar la corrida de replicaci�n para fijar el Remoto
<p>
** Ingresar Transacciones **
<p>
<b>Sistema Central:</b><br>
- Empezar la corrida de replicaci�n cuando se requiera'
 WHERE ad_workflow_id = 109 AND ad_language = 'es_MX';

UPDATE ad_menu_trl
   SET NAME = 'Configuraci�n de Replicaci�n'
 WHERE ad_menu_id = 395 AND ad_language = 'es_MX';

UPDATE ad_wf_node_trl
   SET HELP =
          'Los usuarios pueden registrarse en el sistema y tener acceso a la funcionalidad via uno o m�s roles. Esto permite al usuario ser tratado como representante de ventas en el sistema.'
 WHERE ad_wf_node_id = 140 AND ad_language = 'es_MX';

UPDATE ad_menu_trl
   SET NAME = 'Algoritmo Conciliaci�n'
 WHERE ad_menu_id = 437 AND ad_language = 'es_MX';

UPDATE ad_window_trl
   SET NAME = 'Algoritmo Conciliaci�n'
 WHERE ad_window_id = 302 AND ad_language = 'es_MX';

UPDATE ad_element_trl
   SET NAME = 'Contacto del men�',
       printname = 'Contacto del men�'
 WHERE ad_element_id = 2743 AND ad_language = 'es_MX';

UPDATE ad_field_trl
   SET NAME = 'Contacto del men�'
 WHERE ad_field_id = 11559 AND ad_language = 'es_MX';

UPDATE ad_menu_trl
   SET NAME = 'Pa�s, Regi�n y Ciudad',
       description = 'Mantener pa�ses, regiones y ciudades'
 WHERE ad_menu_id = 109 AND ad_language = 'es_MX';

UPDATE ad_window_trl
   SET NAME = 'Pa�s, Regi�n y Ciudad',
       description = 'Mantener pa�ses, regiones y ciudades'
 WHERE ad_window_id = 122 AND ad_language = 'es_MX';

COMMIT ;
