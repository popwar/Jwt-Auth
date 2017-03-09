<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
</head>
<body>
Date: ${time?date}
<form role="form" action="/auth/login" method="post">
<input id="username" name="username" placeholder="Username" />
<input type="hidden" id="forwardURL" name="forwardURL" value="${forwardURL}" />
<input type="button" id="login" value="Sign In">
</form>

</body>
<script src="https://code.jquery.com/jquery-1.12.4.min.js"
  integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
  crossorigin="anonymous"></script>
<script src="a.js"> </script>
</html>
