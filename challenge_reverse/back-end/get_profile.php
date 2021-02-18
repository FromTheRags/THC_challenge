<?php

$stm = $pdo->prepare("SELECT Description FROM Users WHERE UserId = :id");
$stm->bindParam(":id", $_SESSION['user_id'], PDO::PARAM_INT);
$stm->execute();

$row = $stm->fetch(PDO::FETCH_ASSOC);
$row['Description'];
$profile = array("login" => $_SESSION['user_login'], "description" => $row['Description']);

echo json_encode($profile);

?>