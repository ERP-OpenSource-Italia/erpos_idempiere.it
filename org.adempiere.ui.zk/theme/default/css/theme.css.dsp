<%@ page contentType="text/css;charset=UTF-8" %>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

html,body {
	margin: 0;
	padding: 0;
	height: 100%;
	width: 100%;
	background-color: #D4E3F4;
	color: #333;
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
	padding-top: 40px;
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
	height: 36px;
	width: 72px;
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
	background-image: url(../images/header-bg.png);
	background-repeat: repeat-x;
	background-position: bottom left;
	background-color: white;
	width: 100%;
	height: 38px;
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
}

.menu-href:hover, .menu-href:active {
	text-decoration: underline !important;
}

.menu-href img {
	padding: 2px;
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

.toolbar-button .z-toolbarbutton-body .z-toolbarbutton-cnt img {
	width: 22px;
	height: 22px;
	padding: 0px 1px 0px 1px;
	border-style: solid;
	border-width: 1px;
	border-color: transparent;
}

.embedded-toolbar-button .z-toolbarbutton-body .z-toolbarbutton-cnt img {
	width: 16px;
	height: 16px;
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
.action-button {
	height: 30px;
	width: 48px;
}

.action-text-button {
	height: 30px;
	width: 80px;
}

.action-image-text-button {
	height: 30px;
	width: 80px;
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
}

.desktop-tabbox {
	padding-top: 0px; 
	background-color: #D1E7F6;
}

.desktop-north, .desktop-center {
	border: none;
}

.desktop-tabpanel {
	margin: 0;
	padding: 0;
	border: 0;
	position: absolute !important;
	background-color: #FFFFFF
}

.menu-search-panel .z-comboitem-img { 
	vertical-align:top; 
	padding-bottom:4px; 
}

.menu-panel .z-toolbar-panel {
	padding-right: 0;
}

.desktop-left-column {
	width: 310px;
	border: none;
	background-color: #D2E0EB;
}

.desktop-left-column + .z-west-splt {
	border-top: 1px solid #c5c5c5; 
	border-right: 1px solid #c5c5c5;
}

.desktop-left-column + .z-west-splt .z-west-splt-btn {
	display: none;
}

.desktop-left-column .z-west-body {
	border-right: 1px solid #c5c5c5;
}

.desktop-left-column .z-west-header {
	border-top: 1px solid #c5c5c5;
}

.desktop-hometab {
	margin-left: 2px !important;
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
	width: 99%;
	height: 99%;
	position: absolute;
}

.dashboard-widget {
	margin-top: 4px; 
	margin-left: 8px; 
	position: relative;
}

.dashboard-report-iframe {
	min-height:300px; 
	border: 1px solid lightgray; 
	margin:auto;
	width: 99%;
	height: 90%;
}

<%-- adwindow and form --%>
.adform-content-none {
	overflow: auto;
	position: absolute;
	width: 100%;
	margin: 3px;
}

.adwindow-layout {
	position:absolute; 
	border: none;
	width: 100%;
	height: 100%;
}

.adwindow-layout .z-center {
	border: none;
}

.adwindow-north {
	border: none;
}

.adwindow-south {
	border-left: none;
	border-right: none;
}

.adwindow-layout .z-south {
	border-top: 1px solid #C5C5C5 !important;
}

.adwindow-status {
	background-color: #D2E0EB;
	height: 20px;
}

.adwindow-nav {
}

.adwindow-left-nav {
	border-right: 1px solid #7EAAC6;
	border-left: none;
	border-top: none;
	border-bottom: none;
}

.adwindow-right-nav {
	border-left: 1px solid #7EAAC6;
	border-right: none;
	border-top: none;
	border-bottom: none;
}

.adwindow-nav-content {
	background-color: #D2E0EB;
	height: 100%;
	padding-top: 2px;
}

.adwindow-toolbar {
	border: 0px;
}

.adwindow-navbtn-first {
	margin-top: 8px !important;
}

.adwindow-navbtn-dis, .adwindow-navbtn-sel, .adwindow-navbtn-uns {
	border: 0px;
	margin-top: 3px;
	padding-top: 2px;
	padding-bottom: 2px;
}

.adwindow-navbtn-sel {
	background-color: #9CBDFF;
	font-weight: bold;
	color: #274D5F;
	cursor: pointer;
	border-top: 2px solid #7EAAC6;
	border-bottom: 2px solid #7EAAC6;
}

.adwindow-left-navbtn-sel {
	border-left: 2px solid #7EAAC6;
	border-right: none;
	text-align: right;
	-moz-border-radius-topleft: 5px;
	-moz-border-radius-bottomleft: 5px;
	border-top-left-radius: 5px;
	border-bottom-left-radius: 5px;
	-webkit-border-top-left-radius: 5px;
	-webkit-border-bottom-left-radius: 5px;
	background-color: #D2E0EB !important;
	background-image: url(../images/adtab-left-bg.png);
	background-repeat: repeat-y;
	background-position: top right;
}

.adwindow-right-navbtn-sel {
	border-right: 2px solid #7EAAC6;
	border-left: none;
	text-align: left;
	-moz-border-radius-topright: 5px;
	-moz-border-radius-bottomright: 5px;
	border-top-right-radius: 5px;
	border-bottom-right-radius: 5px;
	-webkit-border-top-right-radius: 5px;
	-webkit-border-bottom-right-radius: 5px;
	background-color: #D2E0EB !important;
	background-image: url(../images/adtab-right-bg.png);
	background-repeat: repeat-y;
	background-position: top left;
}

.adwindow-navbtn-uns {
	background-color: #C4DCFB;
	font-weight: normal;
	color: #274D5F;
	cursor: pointer;
}

.adwindow-navbtn-dis {
	background-color: #C4DCFB;
}

.adwindow-navbtn-uns, .adwindow-navbtn-dis {
	border-top: 1px solid #CCCCCC;
	border-bottom: 1px solid #CCCCCC;
}

.adwindow-left-navbtn-uns, .adwindow-left-navbtn-dis {
	border-left: 1px solid #CCCCCC;
	border-right: none;
	text-align: right;
	-moz-border-radius-topleft: 5px;
	-moz-border-radius-bottomleft: 5px;
	border-top-left-radius: 5px;
	border-bottom-left-radius: 5px;
	-webkit-border-top-left-radius: 5px;
	-webkit-border-bottom-left-radius: 5px;
}

.adwindow-right-navbtn-uns, .adwindow-right-navbtn-dis {
	border-right: 1px solid #CCCCCC;
	border-left: none;
	text-align: left;
	-moz-border-radius-topright: 5px;
	-moz-border-radius-bottomright: 5px;
	border-top-right-radius: 5px;
	border-bottom-right-radius: 5px;
	-webkit-border-top-right-radius: 5px;
	-webkit-border-bottom-right-radius: 5px;
}

<%-- ad tab --%>
.adtab-body {
	position: absolute;
	margin: 0;
	padding: 0;
	width: 100%;
	height: 100%;
	border: none;
}

.adtab-content {
	margin: 0;
	padding: 0;
	border: none;
	overflow: auto;
	width: 100%;
	height: 100%;
	position: absolute;
}

.adtab-form {
	width:100%;
	height:100%;
	border:none !important;
	margin:0;
	padding:0;
}

.adtab-form .z-grid {
	border:none !important;
}

.adtab-grid-panel {
	position: absolute;
	overflow: hidden;
	width: 100%;
	height: 100%;
	border: none;
}

.adtab-grid-panel .z-grid {
	border: none;
}

.adtab-grid-panel .z-center {
	border: none;
	
}

.adtab-grid {
	width: 100%;
	position: absolute;
}

.adtab-grid-south {
	border: none;
}

.adtab-grid-south .z-paging {
	border: none;
}

.adtab-tabpanels {
	width: 80%;
	border-top: 1px solid #9CBDFF;
	border-bottom: 1px solid #9CBDFF;
	border-left: 2px solid #9CBDFF;
	border-right: 2px solid #9CBDFF;
}

.adtab-tree-layout {
	width: 100%; 
	height: 100%; 
	position: absolute;
}

.current-row-indicator {
	background-color: #FA962F !important;
	background-image: none !important; 
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

span.z-tree-tee, span.z-tree-last {
	width:0px;
} 

.z-west-colpsd {
	cursor: pointer;
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

.editor-box .editor-button {
	width: 26px;
	padding: 0px;
}

.editor-box .editor-button img {
	vertical-align: middle;
	text-align: center;
}

.editor-box .editor-button-column {
	width: 28px !important;
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
	background-image: url(../images/group_bg.gif);
}

tr.z-group td.z-row-inner {
	overflow: hidden;
	border-top: 2px solid #81BAF5;
	border-bottom: 1px solid #BCD2EF;
	color: #2C559C;
	font-weight: bold;
}

div.z-group-cnt {
	display:table-cell;
	vertical-align:middle;
}

div.z-group-cnt span.z-label {
	display:inline-block; 
	vertical-align:middle;
	color: #2C559C;
	padding: 5px;
	width: auto;
	height: 100%;
	font-weight: bold;
}

img.z-group-img-open, img.z-group-img-close {
	width: 18px;
	min-height: 18px;
	height: 100%;
	vertical-align: top;
	cursor: pointer;
	border: 0;
}

img.z-group-img-open {
	background-image: url(../images/group-open.png);
	background-position: center center;
	background-color: transparent;
	background-repeat: no-repeat;
}

img.z-group-img-close {
	background-image: url(../images/group-close.png);
	background-position: center center;
	background-color: transparent;
	background-repeat: no-repeat;
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
}

.z-textbox-readonly, .z-intbox-readonly, .z-longbox-readonly, .z-doublebox-readonly, .z-decimalbox-readonly {
	background-color: #F0F0F0;
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

<%-- dashlet --%>
.z-panel-hl .z-panel-header {
	padding: 0 0 2px 0;
}

div.simileAjax-bubble-container {
	z-index: 2800 !important;
}

.info-panel .z-grid {
	border: none !important;
}

.info-panel .z-center {
	text-align: -webkit-center;
	text-align: -moz-center;
}

.info-panel .z-listbox {
	width: 99% !important;
}

.info-panel .z-south {
	margin-top: 2px;
	border-top: 1px solid #C5C5C5;
	background-color: #D2E0EB;	
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

.z-window-highlighted {
	z-index: 1900 !important;
}

.z-window-popup {
	z-index: 1900 !important;
} 

.grid-layout {
	border: none !important; 
	margin: 0 !important; 
	padding: 0 !important;
	background-color: transparent !important;
}

.grid-layout .z-row-inner {
	border: none !important;
	background-color: transparent !important;
}

.confirm-panel {
	width: 100%;
	height: 36px;
}

.confirm-panel .z-hbox {
	margin: 2px;
}

.confirm-panel-right .z-button-os {
	margin-left: 3px;
}

.confirm-panel-left .z-button-os {
	margin-right: 3px;
}

.info-product-tabbedpane {
	background-color: #FFFFFF;
}

.field-label {
	position: relative; 
	float: right;
}
