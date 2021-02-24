<?php

$db_access = parse_ini_file($dir_path . "db/db_access.config");
$pdo = new PDO("mysql:dbname=" . $db_access['name'] . ";unix_socket=" . $db_access['unix_socket'], $db_access['user'], $db_access['password']);

?>
