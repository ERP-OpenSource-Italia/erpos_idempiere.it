<%@ page import="org.bmlaurus.home.Prop" %>
<!--
Theme Name: iDempiere Theme (ErpOS Edition)
Version: 2.0
Description: iDempiere Theme (ErpOS Edition)
Author: s.trinchero
Author URI: http://www.erp-opensource.it
-->
<html>
<% Prop.load(); %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="<%=Prop.loadCss()%>">
	<link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300" rel="stylesheet"> 
	<link rel="stylesheet" href="resources/bootstrap.min.css">	
	<script type="text/javascript" src="resources/jquery/jquery.js"></script>
	<title><%=Prop.getProperty(Prop.TITLE) %></title>
</head>


  <body>

    <!-- Begin page content -->
    <div class="container-fluid">
    
      <div class="row erpos-header">
      	<div class="idempiere-server">
      		<h1><%=Prop.getProperty(Prop.CLIENT_NAME)%></h1>
      		<span><%=Prop.getProperty("ClientVersion")%></span>
      	</div>
      </div>
      
      <div class="row erpos-main-content">
      	<div class="col-md-6">
      	</div>
      	<div class="col-md-5">      	
      		<div class="row">
      			<div class="col-md-12">
      				<a href="<%=Prop.getProperty(Prop.WEBUI_LINK)%>" target="_self">
      					<img name="img_idempiere" style="float:right;" src="<%=Prop.getImage("logo_idempiere.png")%>" border="0" />
      				</a>
      			</div>
      		</div>
      		<div class="row">
						<div class="col-md-12 claim-text">
							<h1>Semplicemente libero</h1>
							<h2>L'erp senza obblighi ma solo vantaggi</h2>
      			</div>      		
      		</div>      		
      	</div>      	
      </div>
      
    </div>

    <footer class="footer">
      <div class="container-fluid">
      	<div class="row">
      		<div class="col-md-12 powered-by">
							<a href="https://www.erp-opensource.it" target="_blank" style="float:right;">
      					<img name="img_locsupport" src="<%=Prop.getImage("powered-by.png")%>" border="0" />
      				</a>
      		</div>
      	</div>
      	<div class="row footer-button-row">
      		<div class="col-md-2 plugin-console">
      			<a id="felixConsole" href="<%=Prop.getProperty(Prop.FELIX_LINK)%>">
      				<img name="img_felix" src="<%=Prop.getImage("img_felix.png")%>" border="0" title="<%=Prop.getProperty(Prop.FELIX_TEXT)%>"/>
      				<span><%=Prop.getProperty(Prop.FELIX_TEXT)%></span>
      			</a>
      		</div>
      		<div class="col-md-2 idempiere-monitor">
      			<a href="<%=Prop.getProperty(Prop.MONITOR_LINK)%>"  target="_blank">
      				<img name="img_idempiereMonitor" src="<%=Prop.getImage("img_idempiereMonitor.png")%>" border="0" title="<%=Prop.getProperty(Prop.MONITOR_TEXT)%>"/>
      				<span><%=Prop.getProperty(Prop.MONITOR_TEXT)%></span>
      			</a>
      		</div>
      		<div class="col-md-2 supporto">
      			<a href="<%=Prop.getProperty(Prop.SUPPORT_LINK)%>" target="_blank">
      				<img name="img_locsupport" src="<%=Prop.getImage("img_locsupport.png")%>" border="0" title="<%=Prop.getProperty(Prop.LOCAL_TEXT)%>"/>
      				<span><%=Prop.getProperty(Prop.LOCAL_TEXT)%></span>
      			</a>
					</div>
      		<div class="col-md-2 go-to-idempiere">
					<a href="<%=Prop.getProperty(Prop.WEBUI_LINK)%>" target="_self">
						<img name="img_webui" src="<%=Prop.getImage("img_webui.png")%>" border="0" />
						<span><%=Prop.getProperty("WebuiText")%></span>
					</a>      			
      		</div>      		
      	</div>        
      </div>
    </footer>
	</body>

</html>