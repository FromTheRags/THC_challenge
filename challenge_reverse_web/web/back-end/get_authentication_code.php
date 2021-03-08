<?php

$auth_code = rand(100, 999);

$_SESSION[$session_version]['authentication_code'] = $auth_code;
$_SESSION[$session_version]['auth_code_retrieved'] = true;

header("HTTP/1.1 " . strval($auth_code) . " OK");
echo "Ok";

?>
