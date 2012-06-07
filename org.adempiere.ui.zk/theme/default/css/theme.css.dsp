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
	color: white;
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
}

.desktop-header-right {
	margin: 0;
	margin-top: 3px;
	padding-right: 5px;
}

.desktop-header {
	background-image: url(../images/header-bg.png);
	background-repeat: repeat-x;
	background-position: bottom left;
	background-color: white;
	width: 100%;
	height: 35px;
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
	height: 32px;
	width: 48px;
}

.action-text-button {
	height: 32px;
	width: 80px;
}

.action-image-text-button {
	height: 32px;
	width: 80px;
}

.editor-button {
	width: 26px;
	padding: 0px;
}

.editor-button img {
	vertical-align: middle;
	text-align: center;
}

<%-- desktop --%>
div.wc-modal, div.wc-modal-none, div.wc-highlighted, div.wc-highlighted-none {
	background-color: white;
}

.desktop-tabpanel {
	margin: 0;
	padding: 0;
	border: 0;
	position: absolute;
}

.menu-search {
	background-color: #E0EAF7;
}

.desktop-left-column {
	width: 310px;
}

<%-- adwindow and form --%>
.adform-content-none {
	overflow: auto;
	position: absolute;
	width: 100%;
	margin: 3px;
}

.adwindow-status {
	background-color: #E0EAF7;
	height: 20px;
}

.adwindow-nav {
}

.adwindow-left-nav {
	border-right: 1px solid #7EAAC6;
	border-left: none;
}

.adwindow-right-nav {
	border-left: 1px solid #7EAAC6;
	border-right: none;
}

.adwindow-nav-content {
	background-color: #E0EAF7;
	height: 100%;
}

.adwindow-toolbar {
	border: 0px;
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
	-webkit-border-top-left-radius: 5px;
	-webkit-border-bottom-left-radius: 5px;
	background-color: #d1e7f6 !important;
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
	-webkit-border-top-right-radius: 5px;
	-webkit-border-bottom-right-radius: 5px;
	background-color: #d1e7f6 !important;
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
	-webkit-border-top-left-radius: 5px;
	-webkit-border-bottom-left-radius: 5px;
}

.adwindow-right-navbtn-uns, .adwindow-right-navbtn-dis {
	border-right: 1px solid #CCCCCC;
	border-left: none;
	text-align: left;
	-moz-border-radius-topright: 5px;
	-moz-border-radius-bottomright: 5px;
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

.adtab-grid-panel {
	position: absolute;
	overflow: hidden;
	width: 100%;
	height: 100%;
}

.adtab-grid {
	width: 100%;
	position: absolute;
}

.adtab-tabpanels {
	width: 80%;
	border-top: 1px solid #9CBDFF;
	border-bottom: 1px solid #9CBDFF;
	border-left: 2px solid #9CBDFF;
	border-right: 2px solid #9CBDFF;
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
	border: none; padding: 0px; margin: 0px; background-color: transparent;
}

.number-box {
	display: inline-block; white-space:nowrap;
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
