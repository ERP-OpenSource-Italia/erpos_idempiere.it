function zoom(cmpid, column, value){
	var widget = zk.Widget.$(cmpid);
	var event = new zk.Event({target: widget, name: 'onZoom', data: [column, value], opts: {ctl: true}});
	zAu.send(event);
}

function drillAcross(cmpid, column, value){
	var widget = zk.Widget.$(cmpid);
	var event = new zk.Event({target: widget, name: 'onDrillAcross', data: [column, value], opts: {ctl: true}});
	zAu.send(event);
}

function drillDown(cmpid, column, value){
	var widget = zk.Widget.$(cmpid);
	var event = new zk.Event({target: widget, name: 'onDrillDown', data: [column, value], opts: {ctl: true}});
	zAu.send(event);
}

function showColumnMenu(e, columnName, row) {
	var d = document.getElementById(columnName + "_" + row + "_d");
	
	var posx = 0;
	var posy = 0;
	if (!e) var e = window.event;
	if (e.pageX || e.pageY) 	{
		posx = e.pageX;
		posy = e.pageY;
	}
	else if (e.clientX || e.clientY) 	{
		posx = e.clientX + document.body.scrollLeft
			+ document.documentElement.scrollLeft;
		posy = e.clientY + document.body.scrollTop
			+ document.documentElement.scrollTop;
	}
	
	d.style.top = posy;	
	d.style.left = posx;
	d.style.display = "block";
	
	setTimeout("document.getElementById('" + columnName + "_" + row + "_d" + "').style.display='none'", 3000);
}
