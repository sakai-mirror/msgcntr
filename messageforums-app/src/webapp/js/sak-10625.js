//fix for double click stack traces in IE - SAK-10625
sak10625_disabler = function(){
   $("a, input[@type=button], input[@type=submit]").attr("onclick", "");
}
$(document).ready(function(){
	$("a").filter(function(){regexp=/submit\(\)/; 
							 var obj = regexp.test($(this).attr("onclick"));
							 if (obj) obj.bind("click", sak10625_disabler);})
   $("input[@type=button], input[@type=submit]").bind("click", sak10625_disabler);
});
