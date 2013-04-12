<%@ page contentType="text/css;charset=UTF-8" %>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

html,body {
	margin: 0;
	padding: 0;
	height: 100%;
	width: 100%;
	background-color: #D4E3F4;
	color: #333;
	font-family: Helvetica,Arial,sans-serif;
	overflow: hidden;
}

<%-- login --%>
.login-window {
	background-color: #E5E5E5;
}

.login-box-body {
	width: 660px;
	background-image: url(../images/login-box-bg.png);
	background-repeat: repeat-y;
	background-color: transparent;
	z-index: 1;
	padding: 0;
	margin: 0;
	text-align: center;
	padding-bottom: 100px;
}

.login-box-header {
	background-image: url(../images/login-box-header.png);
	background-color: transparent;
	z-index: 2;
	height: 54px;
	width: 660px;
}

.login-box-header-txt {
	color: white !important;
	font-weight: bold;
	position: relative;
	top: 30px;
}

.login-box-header-logo {
	padding-top: 20px;
	padding-bottom: 25px;
}

.login-box-footer {
	background-image: url(../images/login-box-footer.png);
	background-position: top right;
	background-attachment: scroll;
	background-repeat: repeat-y;
	z-index: 2;
	height: 110px;
	width: 660px;
}

.login-box-footer .confirm-panel {
	width: 600px !important;
}

.login-box-footer-pnl {
	width: 604px;
	margin-left: 10px;
	margin-right: 10px;
	padding-top: 40px !important;
}

.login-label {
	color: black;
	text-align: right;
	width: 40%;
}

.login-field {
	text-align: left;
	width: 55%;
}

.login-btn {
	padding: 4px 20px !important;
}

.login-east-panel, .login-west-panel {
	width: 350px;
	background-color: #E0EAF7;
	position: relative;
}

<%-- header --%>
.desktop-header-left {
	margin: 0;
	margin-left: 5px;
	margin-top: 3px;
	background-color: transparent !important; 
	border: none !important;
}

.desktop-header-right {
	margin: 0;
	margin-top: 3px;
	padding-right: 5px;
	background-color: transparent !important; 
	border: none !important;
}

.desktop-header {
	background-color: #F4F4F4;
	width: 100%;
	height: 35px;
	border-bottom: 1px solid #C5C5C5;
}

.desktop-header-font {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
}

.menu-href {
	font-size: 11px;
	font-weight: normal;
	color: #333 !important;
	text-decoration: none !important;
	padding-right: 4px;
}

.menu-href:hover, .menu-href:active {
	text-decoration: underline !important;
	color: blue !important;
}

.menu-href img {
	padding: 2px;
	padding-right: 4px;
}

.fav-new-btn {
	margin-top: 4px;
	margin-left: 2px;
}

.disableFilter img {
	opacity: 0.2;
	filter: progid : DXImageTransform . Microsoft . Alpha(opacity = 20);
	-moz-opacity: 0.2;
}

.toolbar {
	padding: 0px;
}

.z-toolbarbutton-cnt {
	padding: 0px;
}

.toolbar-button {
	background-color: transparent; 
	display:inline-block; 
	margin-left: 1px; 
	margin-right: 1px; 
	width: 26px; 
	height: 24px;
}

.breadcrumb-toolbar-button {
	background-color: transparent; 
	display:inline-block; 
	width: 22px; 
	height: 22px;
}

.breadcrumb-record-info {
	font-size: 12px;
}

.toolbar-button .z-toolbarbutton-body .z-toolbarbutton-cnt img {
	width: 22px;
	height: 22px;
	padding: 0px 1px 0px 1px;
	border-style: solid;
	border-width: 1px;
	border-color: transparent;
}

.depressed img {
	border-width: 1px;
	border-color: #9CBDFF;
	background-color: #C4DCFB;
	padding: 0px 1px 0px 1px;
}

<%-- button --%>
.z-button .z-button-tl,
.z-button .z-button-tr,
.z-button .z-button-bl,
.z-button .z-button-br
{
	display: none;
	background: none !important;
}

.z-button .z-button-tm,
.z-button .z-button-bm
{
	display: none;
	background: none !important;
}

.z-button .z-button-cl,
.z-button .z-button-cr
{
	display: none;
	background: none !important;
}

.z-button .z-button-cm
{
	background: none !important;
	border: none !important;
	margin: 0 !important;
	padding: 0 !important;
}

.z-button-os, .z-button {
  display: inline-block;
  margin: 0px;
  padding: 4px 20px;
  font-size: 12px;
  line-height: 20px;
  text-align: center;
  vertical-align: middle;
  cursor: pointer;
  background-color: #f5f5f5;
  background-image: -moz-linear-gradient(top, #ffffff, #e6e6e6);
  background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#ffffff), to(#e6e6e6));
  background-image: -webkit-linear-gradient(top, #ffffff, #e6e6e6);
  background-image: -o-linear-gradient(top, #ffffff, #e6e6e6);
  background-image: linear-gradient(to bottom, #ffffff, #e6e6e6);
  background-repeat: repeat-x;
  border: 1px solid #cccccc;
  border-color: #e6e6e6 #e6e6e6 #bfbfbf;
  border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
  border-bottom-color: #b3b3b3;
  -webkit-border-radius: 4px;
     -moz-border-radius: 4px;
          border-radius: 4px;
  filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffffffff', endColorstr='#ffe6e6e6', GradientType=0);
  filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
  zoom: 1;
  -webkit-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05);
     -moz-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05);
          box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05);
}

.z-button {
	margin: 0px !important;
}

.z-button-os:hover, .z-button-over,
.z-button-os:focus, .z-button-focus,
.z-button-os:active, .z-button-clk,
.z-button-os.active, 
.z-button-os.disabled, .z-button-disd,
.z-button-os[disabled] {
  color: #333333;
  background-color: #e6e6e6;
}

.z-button-os:active, .z-button-clk,
.z-button-os.active {
  background-color: #cccccc \9;
}

.z-button-os:hover, .z-button-over,
.z-button-os:focus, .z-button-focus {
  color: #333333;
  text-decoration: none;
  background-position: 0 -15px;
  -webkit-transition: background-position 0.1s linear;
     -moz-transition: background-position 0.1s linear;
       -o-transition: background-position 0.1s linear;
          transition: background-position 0.1s linear;
}

.z-button-os:focus, .z-button-focus {
  outline: 5px auto -webkit-focus-ring-color;
}

.z-button-os.active, .z-button-clk,
.z-button-os:active {
  background-image: none;
  outline: 0;
  -webkit-box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15), 0 1px 2px rgba(0, 0, 0, 0.05);
     -moz-box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15), 0 1px 2px rgba(0, 0, 0, 0.05);
          box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15), 0 1px 2px rgba(0, 0, 0, 0.05);
}

.z-button-os.disabled, .z-button-disd,
.z-button-os[disabled] {
  cursor: default;
  background-image: none;
  opacity: 0.65;
  filter: alpha(opacity=65);
  -webkit-box-shadow: none;
     -moz-box-shadow: none;
          box-shadow: none;
}

.img-btn img {
	height: 22px;
	width: 22px;
	background-color: transparent;
}

.txt-btn img {
	height: 16px;
	width: 16px;
	background-color: transparent;
	vertical-align: middle;
	display: inline-block;
}

.z-button-os.btn-small {
	padding: 1px 5px;
}

<%-- desktop --%>
div.wc-modal, div.wc-modal-none, div.wc-highlighted, div.wc-highlighted-none {
	background-color: white;
}

.desktop-user-panel {
	text-align: right;
}

.desktop-layout {
	position: absolute; 
	border: none;
	background-color: #E4E4E4;
}

.desktop-tabbox {
	padding-top: 0px; 
	background-color: #E4E4E4;
}

.desktop-tabbox .z-tab {
	margin-right: 2px;
}

.desktop-tabbox .z-tab .z-tab-hl,  .desktop-tabbox .z-tab .z-tab-hr, 
.desktop-tabbox .z-tab .z-tab-hm {
	height: 22px;
}

.desktop-tabbox .z-tab-seld .z-tab-hl,  .desktop-tabbox .z-tab-seld .z-tab-hr, 
.desktop-tabbox .z-tab-seld .z-tab-hm {
	height: 21px;
}

.desktop-tabbox .z-tab-seld {
	border-top: 2px solid #666;
	border-top-left-radius: 5px 5px;
	border-top-right-radius: 5px 5px;
}

.desktop-north, .desktop-center {
	border: none;
}

.desktop-center {
	padding-top: 4px;
	background-color: #E4E4E4;
}

.desktop-tabpanel {
	margin: 0;
	padding: 0;
	border: 0;
	position: absolute !important;
	background-color: #FFFFFF
}

.menu-search-panel .z-comboitem-img { 
	padding-bottom:4px; 
}

.z-comboitem-img { 
	vertical-align:top; 
}

.z-combobox input {
	vertical-align:top;
}

.menu-panel .z-toolbar-panel {
	padding-right: 0;
}

.desktop-left-column {
	width: 200px;
	border: none;
	border-right: 1px solid #C5C5C5;
	background-color: #E4E4E4;
	padding-top: 4px; 
}

.desktop-right-column {
	width: 200px;
	border: none;
	border-left: 1px solid #C5C5C5;
	background-color: #E4E4E4;
	padding-top: 4px; 
}

.desktop-left-column + .z-west-splt {
	border-top: none; 
	border-right: 1px solid #c5c5c5;
}

.desktop-right-column + .z-east-splt {
	border-top: none; 
	border-left: 1px solid #c5c5c5;
}

.z-south-splt,  .z-west-splt, .z-east-splt, .z-north-splt {
	background: none !important;
}

.desktop-left-column .z-west-body {
	border-right: none;
}

.desktop-right-column .z-east-body {
	border-left: none;
}

.desktop-left-column .z-west-header, .desktop-right-column .z-east-header {
	border-top: 1px solid #c5c5c5;
}

.desktop-left-column .z-anchorlayout-body, .desktop-right-column .z-anchorlayout-body {
	overflow-x: hidden;
}

.z-anchorlayout-body { overflow:auto }
 
.z-anchorchildren { overflow:visible }

.slimScroll .z-anchorlayout-body { overflow:hidden }

.desktop-hometab {
	margin-left: 4px !important;
}

.desktop-menu-popup {
	z-index: 9999;
	background-color: #fff;
}

.desktop-menu-toolbar {
	background-color: #ffffff; 
	verticle-align: middle; 
	padding: 2px;
	border-top: 1px solid #c5c5c5;
}

.desktop-home-tabpanel {
	background-color: #FFFFFF;
	width: 99% !important;
}

.link {
	cursor:pointer;
}

.link.z-toolbarbutton-over {
	border: none;
	padding: 1px 0;
} 

.link.z-toolbarbutton-over .z-toolbarbutton-body {
	border: none;
	padding: 0 1px;
}

.link.z-toolbarbutton-over .z-toolbarbutton-cnt {
	background-image: none;
	color: blue;
	text-decoration: underline;
}

<%-- dashlet --%>
.z-panel-tl, .z-panel-tr, 
.z-panel-hr, .z-panel-hl, 
.z-panel-hm {
	background-image: none; background-color: #FFFFFF;
}

.z-panel {
	border: 1px solid #c5c5c5;
}

.z-panel-noborder {
	border: none !important;
}

.z-panelchildren {
	border: none;
}

.z-panel-hl {
	padding-bottom: 1px;
	border-bottom: 1px solid #008BB6;
}

.z-panel-hl .z-panel-header {
	padding: 0 0 2px 0;
	color: #333; font-weight: bold;
}	

.desktop-home-tabpanel .z-panel-tl, .desktop-home-tabpanel .z-panel-tr, 
.desktop-home-tabpanel .z-panel-hr, .desktop-home-tabpanel .z-panel-hl, 
.desktop-home-tabpanel .z-panel-hm {
	background-color: #FFFFFF;
}

.menu-panel {
	width: 100% !important;
	height: 100% !important;
	position: relative !important;
}

.menu-panel .z-tree {
	border: none !important;
	width: 100%;
}

.dashboard-layout {
	width: 100%;
	height: 100%;
	position: absolute;
}

.dashboard-widget {
	margin-top: 4px; 
	margin-left: auto; 
	margin-right: auto;
	position: relative;
	width: 92%;
}

.dashboard-report-iframe {
	min-height:300px; 
	border: 1px solid lightgray; 
	margin:auto;
	width: 99%;
	height: 90%;
}

.favourites-box {
	width: 90%;
	margin: auto;
	padding: 5px 0px 5px 0px;
}

.favourites-box .z-vbox {
	width: 100%;
}

.favourites-box .z-hbox {
	padding: 2px 0px;
	width: 100%;
}

.recentitems-box {
	width: 90%;
	margin: auto;
	padding: 5px 0px 5px 0px;
}

.recentitems-box A {
	display: block;
	width: 100%;
	padding: 2px 0px;
}
	
.views-box {
	width: 90%;
	margin: auto;
	padding: 5px 0px 5px 0px;
}

.views-box .z-vbox {
	width: 100%;
}

.views-box .z-toolbarbutton {
	width: 100%;
	padding: 2px 0px;
}

.activities-box {
	width: 90%;
	margin: auto;
	padding: 5px 0px 5px 0px;
}

.activities-box .z-vbox {
	width: 100%;
}

.activities-box .z-button-os {
	text-align: left;
}

.recentitems-box .z-toolbar, .favourites-box .z-toolbar {
	margin-top: 5px;
	margin-bottom: 5px;
}

<%-- adwindow and form --%>
.adwindow-form > .z-grid-body {
	background-color: #F9F9F9;
}

.adwindow-layout {
	position:absolute; 
	border: none;
	width: 100%;
	height: 100%;
	background-color: #F9F9F9;
}

.adwindow-layout .z-center {
	border: none;
}

.adwindow-north {
	min-height: 56px;
	width: 100%;
	padding: 0px;
	margin: 0px;
	border: none;
}

.adwindow-south {
	height: 20px;
	width: 100%;
	padding: 0px;
	margin: 0px;
	border-top: 1px solid #C5C5C5 !important;
}

.adwindow-status {
	background-color: #F4F4F4;
	height: 20px;
}

.adwindow-toolbar {
	border: 0px;
	height: 26px;
	background-image: none;
	background-color: #fff;
}

.adwindow-breadcrumb {
	height: 30px;
	background-color: #FFF;
	padding: 0px;
	padding-left: 5px;
	border-bottom: 1px solid #C5C5C5 !important;
	clear: both;
}

.adwindow-detailpane {
	width: 100%; 
	overflow-y: visible;
}

.adwindow-detailpane-tabbox .z-tab-seld span.z-tab-text {
	cursor: pointer;
}

.adwindow-detailpane-tabbox .z-tab-seld span.z-tab-text:hover {
	text-decoration: underline;
}

.adwindow-detailpane-tabpanel {
	background-color: #fff
}

.adwindow-detailpane-toolbar {
	border: none;
}

.adwindow-detailpane-toolbar .z-toolbar-body {
	width: 100%;
}

.adwindow-detailpane-toolbar .z-toolbarbutton {
	float: left;
	display: inline-block;
}

.adwindow-detailpane-message {
	float: right;
}

.adwindow-detailpane-tabbox {
	width: 99%; 
	margin: auto;
	background-color: #D3D3D3;
}

.adwindow-gridview-detail {
	height: 200px;
}

.adwindow-gridview-detail + .z-south-splt {
	border-top: 1px solid #C5C5C5;
	border-bottom: 1px solid #C5C5C5;
}

.adwindow-gridview-detail .z-south-body {
	padding-top: 1px;
	background-color: #D3D3D3;
}

.adwindow-gridview-borderlayout {
	position: absolute; height: 100%; width: 100%;
}

<%-- ad tab --%>
.adtab-content {
	margin: 0;
	padding: 0;
	border: none;
	position: relative;
}

.adtab-form {
	border:none !important;
	margin:0;
	padding:0;
}

.adtab-grid {
	margin-top: -1px;
	border: none !important;
}

.adtab-grid-south {
	border: none;
	height: 30px;
}

.adtab-grid-south .z-paging {
	border: none;
	padding: 0;
}

.adtab-form-borderlayout {
	width: 100%; 
	height: 100%; 
	position: absolute;
}

.adtab-form-borderlayout .z-center {
	background-color: #F9F9F9;
}

.z-grid tbody tr.highlight td.z-cell { 
	background-color: #FFFFCC !important;
	background-image: none !important;
}

.z-grid tbody tr.highlight td.row-indicator-seld {
	background-color: transparent !important;
	background-image: url(${c:encodeURL('/theme/default/images/EditRecord16.png')}) !important;
	background-position: center;
	background-repeat: no-repeat;
	background-size: 16px 16px;  
	cursor: pointer;
}

.z-grid tbody tr.highlight td.row-indicator {
	background-color: transparent !important;
	background-image: none !important; 
}

.form-label 
{
	text-align: right;
}

.form-label-heading
{
	text-align: center;
}

<%-- status bar --%>
.status {
	width: 100%;
	height: 20px;
}

.status-db {
	padding-top: 0;
	pdding-bottom: 0;
	padding-left: 5px;
	padding-right: 5px;
	cursor: pointer;
	width: 100%;
	height: 100%;
	margin: 0;
	border-left: solid 1px #9CBDFF;
}

.status-info {
	padding-right: 10px;
	border-left: solid 1px #9CBDFF;
}

.status-border {
	border: solid 1px #9CBDFF;
}

.form-button {
	width: 99%;
}

<%-- Combobox --%>
.z-combobox-disd {
	color: black !important; cursor: default !important; opacity: 1; -moz-opacity: 1; -khtml-opacity: 1; filter: alpha(opacity=100);
}

.z-combobox-disd * {
	color: black !important; cursor: default !important;
}

.z-combobox-text-disd {
	background-color: #ECEAE4 !important;
}

<%-- Button --%>
.z-button-disd {
	color: black; cursor: default; opacity: .6; -moz-opacity: .6; -khtml-opacity: .6; filter: alpha(opacity=60);
}

<%-- highlight focus form element --%>
input:focus, textarea:focus, .z-combobox-inp:focus, z-datebox-inp:focus {
	border: 1px solid #0000ff;
}

.mandatory-decorator-text {
	text-decoration: none; font-size: xx-small; vertical-align: top; color:red;
}
<%-- menu tree cell --%>
.menu-treecell-cnt div {
	border: 0; margin: 0; padding: 0;
	font-family: ${fontFamilyC};
	font-size: ${fontSizeM}; 
	font-weight: normal;
    overflow-x: hidden;
    white-space: nowrap;
    text-overflow: ellipsis !important;
    cursor: pointer;
}

span.z-tree-tee, span.z-tree-last, span.z-tree-firstspacer {
	width:0px;
} 

.z-west-colpsd {
	cursor: pointer;
	border-top: none;
}

.z-east-colpsd {
	cursor: pointer;
	border-top: none;
}

<%-- FOCUSED FIELD in different color --%>

.z-textbox-focus, .z-textbox-focus input,
.z-decimalbox-focus, .z-decimalbox-focus input,
.z-intbox-focus, .z-intbox-focus input,
.z-longbox-focus, .z-longbox-focus input,
.z-doublebox-focus, .z-doublebox-focus input,
.z-combobox-focus .z-combobox-inp,
.z-checkbox-focus .z-checkbox-inp,
.z-datebox-focus .z-datebox-inp,
.z-timebox-focus .z-timebox-inp {
	background: #FFFFCC;
}

<%-- Editor --%>
.editor-box {
	display: inline-block;
	border: none; 
	padding: 0px; 
	margin: 0px; 
	background-color: transparent;
	width: 100%;
}

.editor-box table {
	border: none; 
	padding: 0px; 
	margin: 0px;
	width: 100%;
	border: none;
}

.editor-box tr {
	width: 100%; 
	border: none; 
	padding: 0px; 
	margin: 0px; 
	white-space:nowrap; 
}

.editor-box td {
	border: none; 
	padding: 0px; 
	margin: 0px;
}

.editor-box .z-textbox {
	display: inline; 
	width: 99%;
}

.editor-button {
	padding: 3px 8px;
	margin: 0px;
}

.editor-button img {
	vertical-align: middle;
	text-align: center;
	width: 10px;
	height: 10px;
}

.editor-button-column {
}

.number-box {
	display: inline-block; white-space:nowrap;
}

.datetime-box {
	display: inline-block;
	white-space:nowrap;
}

.datetime-box .z-datebox {
	display: inline;
}

.datetime-box .z-timebox {
	display: inline;
}

<%-- Group --%>
tr.z-group {
	background: #E9F2FB repeat-x 0 0;
	background-image: url(../images/group_bg.gif) !important;
}

.z-group-header .z-label {
	font-family: Helvetica,Arial,sans-serif;
	color: #333;
	width: auto;
	font-weight: bold;
	font-size: 11px;
}

.z-group td.z-group-inner {
	overflow: hidden !important;
	border-bottom: 1px solid #CFCFCF !important;
	border-top: 1px solid #CFCFCF !important;
}

<%-- Tablet --%>
.tablet-scrolling {
	-webkit-overflow-scrolling: touch;
}

<%-- Tab --%>
.z-tab-close {
	top:4px; 
	margin-right:2px;
}

.z-tab-hm-close {
	padding-right:18px;
}

<%-- set color for text and label --%>
.z-tab-seld .z-tab-text, .z-tab .z-tab-text, div.z-treefooter-cnt, div.z-treecell-cnt, div.z-treecol-cnt, 
.z-label, .z-toolbarbutton-cnt, tr.z-treerow, tr.z-treerow a, tr.z-treerow a:visited {
	color: #333;
	font-family: Helvetica,Arial,sans-serif;
}

.z-textbox-readonly, .z-intbox-readonly, .z-longbox-readonly, .z-doublebox-readonly, .z-decimalbox-readonly {
	background-color: #F0F0F0;
}

span.z-tab-text {
	height: 13px; 
}

<%-- menu tree cell --%>
div.z-tree-body td.menu-tree-cell {
	cursor: pointer;
	padding: 0 2px;
   	font-size: ${fontSizeM};
   	font-weight: normal;
   	overflow: visible;
}

div.menu-tree-cell-cnt {
	border: 0; margin: 0; padding: 0;
	font-family: ${fontFamilyC};
	font-size: ${fontSizeM}; font-weight: normal;
    white-space:nowrap
}

td.menu-tree-cell-disd * {
	color: #C5CACB !important; cursor: default!important;
}

td.menu-tree-cell-disd a:visited, td.menu-tree-cell-disd a:hover {
	text-decoration: none !important;
	cursor: default !important;;
	border-color: #D0DEF0 !important;
}

div.z-dottree-body td.menu-tree-cell {
	cursor: pointer; padding: 0 2px;
	font-size: ${fontSizeM}; font-weight: normal; overflow: visible;
}

div.z-filetree-body td.menu-tree-cell {
	cursor: pointer; padding: 0 2px;
	font-size: ${fontSizeM}; font-weight: normal; overflow: visible;
}

div.z-vfiletree-body td.menu-tree-cell {
	cursor: pointer; padding: 0 2px;
	font-size: ${fontSizeM}; font-weight: normal; overflow: visible;
}

div.simileAjax-bubble-container {
	z-index: 2800 !important;
}

.info-panel .z-window-overlapped-bl, .info-panel .z-window-highlighted-bl {
	background-color: #F4F4F4 !important;
}

.info-panel .z-grid {
	border: none !important;
}

.info-panel .z-center {
}

.info-panel .z-listbox {
	width: 99% !important;
	position: relative;
	margin: auto;
}

.info-panel .z-south {
	margin-top: 2px;
	border-top: 1px solid #C5C5C5;
	background-color: #F4F4F4; 
}

.info-panel .z-window-highlighted-cnt {
	padding: 0px;
}

.z-borderlayout, .z-north, .z-center, .z-south {
	border: none;
}

.z-window-embedded-cnt, .z-window-embedded-cm {
	border: none;
}

.z-window-embedded-tl, .z-window-embedded-tr, .z-window-embedded-hl, .z-window-embedded-hr, 
.z-window-embedded-hm, .z-window-embedded-cl, .z-window-embedded-cr, 
.z-window-embedded-bl, .z-window-embedded-br {
	background-image: none;
}

.z-modal-mask {
	z-index: 1800 !important;
}

.grid-layout {
	border: none !important; 
	margin: 0 !important; 
	padding: 0 !important;
	background-color: transparent !important;
}

.grid-layout .z-row-inner, .grid-layout .z-cell {
	border: none !important;
	background-color: transparent !important;
}

.grid-layout tr.z-row-over>td.z-row-inner, .grid-layout tr.z-row-over>.z-cell {
	border: none !important;
}

.grid-layout tr.z-row-over>td.z-row-inner, .grid-layout tr.z-row-over>.z-cell {
	background-image: none !important;
}

.confirm-panel {
	width: auto;
	height: auto;
	position: relative;
	padding-left: 2px;
	padding-right: 2px;
}

.confirm-panel-right {
	float: right;
}

.confirm-panel-left {
	float: left;
}

.info-product-tabbedpane {
	background-color: #FFFFFF;
}

.field-label {
	position: relative; 
	float: right;
}

tbody.z-grid-empty-body td {
	text-align: left;
}

<%-- notification message --%>
.z-notification .z-notification-cl, 
.z-notification .z-notification-cnt {
    width: 400px;
}

.z-notification {
	padding: 3px !important;
}

<%-- toolbar popup dialog --%>	
.toolbar-popup-window .z-window-popup-cnt {
	margin-top: 6px;
}

.toolbar-popup-window.process-buttons-popup .z-button-os {
	white-space: nowrap;
	width: 95%;
	text-align: left;
	margin: auto;
}

.toolbar-popup-window-cnt {
}

.toolbar-popup-window::before {
	content: '';
	position: absolute;
	width: 0px;
	height: 0px;
	top: -7px;
	left: 6px;
	border-top: 7px dashed transparent;
	border-left: 7px dashed transparent;
	border-right: 7px dashed transparent;
	border-bottom: 7px solid #ACACAC;
}

.toolbar-popup-window-cnt::before {
	content: '';
	position: absolute;
	width: 0px;
	height: 0px;
	top: -5px;
	left: 7px;
	border-top: 6px dashed transparent;
	border-left: 6px dashed transparent;
	border-right: 6px dashed transparent;
	border-bottom: 6px solid #FFF;
}

.adwindow-detailpane-sub-tab::before {
	content: '';
	position: absolute;
	width: 0px;
	height: 0px;
	top: 6px;
	left: 4px;
	border-top: 5px dashed transparent;
	border-left: 5px dashed #696969;
	border-right: 5px dashed transparent;
	border-bottom: 5px solid transparent;
}

i.grid-combobox-editor {
	width: 96% !important;
}

.grid-combobox-editor input {
	width: 85% !important;
}

.embedded-dialog {
	position: absolute;
}

.embedded-dialog .z-window-embedded-header {
	color: #fff;
	font-weight: bold;
}

.z-window-embedded-cnt {
	border: none;
}

.z-window-overlapped-cm,  .z-window-modal-cm, .z-window-highlighted-cm, .z-window-embedded-cm {
	border: none;
}

.z-window-overlapped-tl, .z-window-popup-tl, .z-window-modal-tl, .z-window-highlighted-tl, .embedded-dialog .z-window-embedded-tl
{
	display: none;
}

.z-window-overlapped-tr, .z-window-popup-tr, .z-window-modal-tr, .z-window-highlighted-tr, .embedded-dialog .z-window-embedded-tr
{
	display: none;
}

.z-window-overlapped-hl, .z-window-popup-hl, .z-window-modal-hl, .z-window-highlighted-hl, .embedded-dialog .z-window-embedded-hl {
	padding-top: 3px;
}

.z-window-overlapped-tl, .z-window-popup-tl, .z-window-modal-tl, .z-window-highlighted-tl, .embedded-dialog .z-window-embedded-tl,
.z-window-overlapped-tr, .z-window-popup-tr, .z-window-modal-tr, .z-window-highlighted-tr, .embedded-dialog .z-window-embedded-tr,
.z-window-overlapped-hm, .z-window-popup-hm, .z-window-modal-hm, .z-window-highlighted-hm, .embedded-dialog .z-window-embedded-hm,
.z-window-overlapped-hl, .z-window-popup-hl, .z-window-modal-hl, .z-window-highlighted-hl, .embedded-dialog .z-window-embedded-hl,
.z-window-overlapped-hr, .z-window-popup-hr, .z-window-modal-hr, .z-window-highlighted-hr, .embedded-dialog .z-window-embedded-hr
{
	background-color: #484848;
}

.z-window-overlapped-cl, .z-window-popup-cl, .z-window-modal-cl, .z-window-highlighted-cl, .embedded-dialog .z-window-embedded-cl
{
	padding-left: 1px;
	border-left: 1px solid #c5c5c5;
}

.z-window-overlapped-cr, .z-window-popup-cr, .z-window-modal-cr, .z-window-highlighted-cr, .embedded-dialog .z-window-embedded-cr
{
	padding-right: 1px;
	border-right: 1px solid #c5c5c5;
}

.z-window-overlapped-bl, .z-window-popup-bl, .z-window-modal-bl, .z-window-highlighted-bl, .embedded-dialog .z-window-embedded-bl
{
	border-left: 1px solid #c5c5c5;
	border-right: 1px solid #c5c5c5;
	background-color: #fff;
	margin: 0px;
	padding-bottom: 3px;
}

.z-window-overlapped-br, .z-window-popup-br, .z-window-modal-br, .z-window-highlighted-br, .embedded-dialog .z-window-embedded-br
{
	display: none;
}

.z-window-overlapped .z-window-overlapped-header,
.z-window-popup .z-window-popup-header,
.z-window-modal .z-window-modal-header,
.z-window-highlighted .z-window-highlighted-header
{
	color: #fff;
	font-weight: bold;
}

.z-window-overlapped, .z-window-modal, .z-window-highlighted
{
	border-top: 1px solid #c5c5c5 !important;
	border-bottom: 1px solid #c5c5c5 !important;
}

.z-window-overlapped-hl, .z-window-popup-hl, .z-window-modal-hl, .z-window-highlighted-hl, .z-window-embedded-hl,
.z-window-overlapped-hr, .z-window-popup-hr, .z-window-modal-hr, .z-window-highlighted-hr, .z-window-embedded-hr,
.z-window-overlapped-hm, .z-window-popup-hm, .z-window-modal-hm, .z-window-highlighted-hm, .z-window-embedded-hm,
.z-window-overlapped-cl, .z-window-popup-cl, .z-window-modal-cl, .z-window-highlighted-cl, .z-window-embedded-cl, 
.z-window-overlapped-cr, .z-window-popup-cr, .z-window-modal-cr, .z-window-highlighted-cr, .z-window-embedded-cr,
.z-window-overlapped-tl, .z-window-popup-tl, .z-window-modal-tl, .z-window-highlighted-tl, .z-window-embedded-tl, 
.z-window-overlapped-tr, .z-window-popup-tr, .z-window-modal-tr, .z-window-highlighted-tr, .z-window-embedded-tr,
.z-window-overlapped-bl, .z-window-popup-bl, .z-window-modal-bl, .z-window-highlighted-bl, .z-window-embedded-bl, 
.z-window-overlapped-br, .z-window-popup-br, .z-window-modal-br, .z-window-highlighted-br, .z-window-embedded-br
{
	background-image: none !important;
}

.z-window-modal-shadow, .z-window-overlapped-shadow, .z-window-popup-shadow, .z-window-embedded-shadow, .z-window-highlighted-shadow
{
	border-radius: 0px !important;
}

<%-- Splitter button --%>
.z-east-splt-btn,
.z-west-splt-btn,
.z-north-splt-btn,
.z-south-splt-btn {
	filter: alpha(opacity=100);  <%-- IE --%>
	opacity: 1.0;  <%-- Moz + FF --%>
}

.z-east-splt-btn-over,
.z-west-splt-btn-over,
.z-north-splt-btn-over,
.z-south-splt-btn-over {
	-webkit-filter: brightness(-30%);
	filter: brightness(-30%);
}

.help-content
{
	padding: 5px;
	font-size: 11px;
	font-weight: normal;
}

<%-- Tree nodes configurable on Setup Wizard --%>
.tree-wsetupwizard-finished {
	background-color: #90EE90;
	margin-left:20px
}

.tree-wsetupwizard-skipped {
	background-color: #00FF00;
	margin-left:20px
}

.tree-wsetupwizard-delayed {
	background-color: #0080FF;
	margin-left:20px;
}

.tree-wsetupwizard-in-progress {
	background-color: #FFFF33;
	margin-left:20px;
}

.tree-wsetupwizard-pending {
	background-color: #FFFF00;
	margin-left:20px;
}

.tree-setupwizard-nostatus{
	margin-left:20px;
}

.tree-wsetupwizard-finished-all {
	background-color: #90EE90;
}

.progressmeter-setupwizard {
background: #FFFF00 repeat-x 0 0 ;
background-image: none;
border: 1px solid #CFCFCF;
text-align: left;
height: 20px;
overflow: hidden;
}
.progressmeter-setupwizard-img {
display: inline-block;
background: #90EE90;
background-image: none;
height: 20px;
line-height: 0;
font-size: 0;
}

.menu-search-toggle-box {
	display: inline-block;
	border: 1px solid #ababab;			
}

.menu-search-toggle-box .z-toolbarbutton-over {
	border: none;
}

.menu-search-toggle-off {
	margin:0px; 
	padding: 1px 4px 1px 4px;
}

.menu-search-toggle-on {
	background: #999999;
	-webkit-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3) inset;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3) inset;
	margin:0px;
	padding: 1px 4px 1px 4px;
}

<%-- workaround for http://jira.idempiere.com/browse/IDEMPIERE-692 --%>
.z-combobox-pp {
	max-height: 200px;
}

<%-- dialog --%>
.popup-dialog .z-window-overlapped-cnt, .popup-dialog .z-window-highlighted-cnt {
	padding: 0px;
}

.popup-dialog .z-window-overlapped-bl, .popup-dialog .z-window-highlighted-bl {
	background-color: #f5f5f5;
}

.popup-dialog .dialog-content {
	padding: 8px !important;
	margin-bottom: 20px !important;
}

.popup-dialog.z-window-overlapped .dialog-footer {
	padding: 12px 15px 8px 15px !important;
}

.popup-dialog.z-window-highlighted .dialog-footer {
	padding: 12px 15px 5px 15px !important;
}

.dialog-footer {
	margin-bottom: 0;
	background-color: #f5f5f5;
	border-top: 1px solid #ddd;
	-webkit-box-shadow: inset 0 1px 0 #ffffff;
	-moz-box-shadow: inset 0 1px 0 #ffffff;
	box-shadow: inset 0 1px 0 #ffffff;
}

.btn-ok {
	font-weight: bold;
}

<%-- vbox fix for firefox and ie --%>
table.z-vbox > tbody > tr > td > table {
	width: 100%;	
}
