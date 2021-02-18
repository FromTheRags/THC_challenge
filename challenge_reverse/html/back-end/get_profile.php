<?php

session_start();
if(!isset($_SESSION['signed_in']) || !$_SESSION['signed_in'])
{
    echo 'Access denied';
    exit();
}

$db_access = parse_ini_file("../../db/db_access.config");

$pdo = new PDO("mysql:host=" . $db_access['host'] . ";dbname=" . $db_access['name'], $db_access['user'], $db_access['password']);

$stm = $pdo->prepare("SELECT Description FROM Users WHERE UserId = :id");
$stm->bindParam(":id", $_SESSION['user_id'], PDO::PARAM_INT);
$stm->execute();

$row = $stm->fetch(PDO::FETCH_ASSOC);
$row['Description'];
$profile = array("login" => $_SESSION['user_login'], "description" => $row['Description']);

echo json_encode($profile);

?>