<?php

session_start();
if(!isset($_SESSION['signed_in']) || !$_SESSION['signed_in'])
{
    echo 'Access denied';
    exit();
}

$db_access = parse_ini_file("../../db/db_access.config");

$pdo = new PDO("mysql:host=" . $db_access['host'] . ";dbname=" . $db_access['name'], $db_access['user'], $db_access['password']);

$stm = $pdo->query("SELECT ProductId, Name, Description, Price, QuantityAvailable FROM Products");

/*
if($stm->rowCount() == 0)
{
    echo 'No products';
    exit();
}
*/
$rows = $stm->fetchAll(PDO::FETCH_ASSOC);

echo json_encode($rows);

?>