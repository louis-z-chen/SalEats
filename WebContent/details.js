

$(document).ready(function() {
	$("#toggle-button").button().click(function(){
		$("#toggle-section").slideToggle(500, function() {
		});
    });  
});