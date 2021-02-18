<?php

$db_access = parse_ini_file("../db/db_access.config");
$pdo = new PDO("mysql:host=" . $db_access['host'] . ";dbname=" . $db_access['name'], $db_access['user'], $db_access['password']);

?>
