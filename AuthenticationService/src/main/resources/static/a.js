$("#login").click(function() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8088/auth/login",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({ "username": $("#username").val(), "url" : $("#forwardURL").val() }),
		success : function(data) {
			console.log(data.token);
			window.location.replace($("#forwardURL").val() + "?thing=" + data.token);
//			$.ajax({
//				type : "GET",
//				headers : {
//					"auth-token" : data.token
//				},
//				url : $("#forwardURL").val(),
//				success : function(data) {
//					console.log(data);
//				}
//			});
		}
	});
});
