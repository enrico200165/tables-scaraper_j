function ajax_wishlist(page, user, command, type)
{
	var xmlhttp;
	if (window.XMLHttpRequest)
	{
		// most browsers
		xmlhttp = new XMLHttpRequest();
	}
	else
	{
		// IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
		{
			var field = document.getElementById(type + '_field');
			var response = xmlhttp.responseText;
			if (response == 'login')
				window.location = 'http://www.japan-guide.com/m/login.html?aACTION=wish&aOBJECT=' + page + '&aVALUE=' + command;
			else
				field.innerHTML = response;
		}
	}
	xmlhttp.open('POST', 'http://www.japan-guide.com/m/' + type + '_ajax.html?user=' + user + '&page=' + page + '&command=' + command, true);
	xmlhttp.send();
}

function display_online_reservation_form(type, val1, val2, val3)
{
	var xmlhttp;
	if (window.XMLHttpRequest)
	{
		// most browsers
		xmlhttp = new XMLHttpRequest();
	}
	else
	{
		// IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
		{
			var field = document.getElementById('online_reservation_form');
			field.innerHTML = xmlhttp.responseText;
		}
	}
	xmlhttp.open('POST', 'http://www.japan-guide.com/scripts/online_reservation_form_' + type + '.html?' + val1 + '+' + val2 + '+' + val3, true);
	xmlhttp.send();
}

function display_been_there_popup(page, command)
{
	var popupwindow = document.getElementById('been_there_popup');
	var xmlhttp;
	if (window.XMLHttpRequest)
	{
		// most browsers
		xmlhttp = new XMLHttpRequest();
	}
	else
	{
		// IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
		{
			if (xmlhttp.responseText == "login")
			{
				popupwindow.innerHTML = "<table width=100% height=100%><tr><td height=100% valign=middle><center><font size=5 face=arial>just a second...</font></center></td></tr></table>";
				window.location = 'http://www.japan-guide.com/m/login.html?aACTION=url&aURL=' + page;
			}
			else
			{
				popupwindow.innerHTML = xmlhttp.responseText;
			}
		}
	}
	xmlhttp.open('POST', 'http://www.japan-guide.com/scripts/been_there_popup.html?' + page + '+' + command, true);
	xmlhttp.send();
}


function toggle(div_id)
{
	var el = document.getElementById(div_id);
	if (el.style.display == 'none')
	{
		el.style.display = 'block';
	}
	else
	{
		el.style.display = 'none';
	}
}

function blanket_size(blanketname, viewportwidth, viewportheight)
{
	var documentheight = 0;

	if (typeof(document.body.parentNode.scrollHeight) != 'number')
	{
		documentheight = document.body.scrollHeight;
	}
	else if (typeof(document.body.scrollHeight) != 'number')
	{
		documentheight = document.body.parentNode.scrollHeight;
	}
	else
	{
		documentheight = Math.max(document.body.scrollHeight, document.body.parentNode.scrollHeight);
	}	

	if (viewportheight > documentheight)
	{
		blanket_height = viewportheight;
	}
	else
	{
		blanket_height = documentheight;
	}

	var blanket = document.getElementById(blanketname);
	blanket.style.height = blanket_height + 'px';
}

function window_pos_been_there(popUpDivVar, viewportwidth, viewportheight, popup_width, popup_height)
{
	if (typeof(window.pageYOffset) != 'undefined')
	{
		scrollheight = window.pageYOffset;
	}
	else
	{
		scrollheight = document.body.scrollTop;
	}

	var popUpDiv = document.getElementById(popUpDivVar);
	var popup_width = Math.max(20, viewportwidth / 2 - (popup_width / 2));
	popUpDiv.style.left = popup_width + 'px';
	var popup_height = Math.max(20, viewportheight / 2 - (popup_height / 2) + scrollheight);
	popUpDiv.style.top = popup_height + 'px';
}

function popup_been_there_select(page, value)
{
	var xmlhttp;
	if (window.XMLHttpRequest)
	{
		// most browsers
		xmlhttp = new XMLHttpRequest();
	}
	else
	{
		// IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
		{
			if (xmlhttp.responseText == "login")
			{
				window.location = 'http://www.japan-guide.com/m/login.html?aACTION=url&aURL=' + page;
			}
			else if (xmlhttp.responseText != "")
			{
				var rating_field_div = document.getElementById('rating_field');
				rating_field_div.innerHTML = xmlhttp.responseText;
			}
		}
	}
	xmlhttp.open('POST', 'http://www.japan-guide.com/scripts/been_there_modify_db.html?' + page + '+' + value, true);
	xmlhttp.send();

	popup_been_there(page);
}

function popup_been_there(page)
{
	if (typeof(window.innerWidth) == 'number')
	{
		//Non-IE
		viewportwidth = window.innerWidth;
		viewportheight = window.innerHeight;
	}
	else if (document.documentElement && (document.documentElement.clientWidth || document.documentElement.clientHeight))
	{
		//IE6+
		viewportwidth = document.documentElement.clientWidth;
		viewportheight = document.documentElement.clientHeight;
	}
	else
	{
		//IE5-
		viewportwidth = document.body.clientWidth;
		viewportheight = document.body.clientHeight;
	}

	windowname = 'been_there_popup';
	blanketname = 'blanket';

	var popupwindow = document.getElementById(windowname);
	if (popupwindow.style.display == 'none')
		display_been_there_popup(page, '');
	else
		display_been_there_popup(page, 'clear');

	blanket_size(blanketname, viewportwidth, viewportheight);
	window_pos_been_there(windowname, viewportwidth, viewportheight, 460, 450);
	toggle(blanketname);
	toggle(windowname);		
}
