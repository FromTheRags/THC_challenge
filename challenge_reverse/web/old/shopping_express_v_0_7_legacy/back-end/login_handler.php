<?php

if(!isset($_POST['login']) || !isset($_POST['password']) || !isset($_POST['id']))
{
    echo 'Bad request';
    exit();
}

$token = "A1548S968D2563";
$user_agent = $_SERVER['HTTP_USER_AGENT'];
$user_agent_ok = strpos($user_agent, "? id:BuyExpress/" . $app_version . "/" . $token) != false;
$user_agent_ok = true; // REMOVE !

if(!$user_agent_ok)
{
    echo 'Access denied'; // wrong user agent
    exit();
}

$login = $_POST['login'];
$password = $_POST['password'];
$token_hash = $_POST['id'];
$token_hash = hash("sha256", $token); // REMOVE !

if($token_hash != hash("sha256", $token))
{
    echo 'Access denied'; // wrong token hash
    exit();
}

if(floatval($app_version) < 1.0)
{
    $stm = $pdo->query("SELECT UserId FROM Users WHERE Login = '" . $login . "' AND Password = '" . $password . "'");

    if($stm->rowCount() == 0)
    {
        echo 'Access denied'; // wrong login or password
        exit();
    }

    $row = $stm->fetch(PDO::FETCH_ASSOC);
}
else
{
    $stm = $pdo->prepare("SELECT UserId, PasswordHash, Salt FROM Users WHERE Login = :login");
    $stm->bindParam(":login", $login, PDO::PARAM_STR);
    $stm->execute();

    if ($stm->rowCount() == 0)
    {
        echo 'Access denied'; // wrong login
        exit();
    }

    $row = $stm->fetch(PDO::FETCH_ASSOC);

    $hash = hash("sha256", $password . $row['Salt']);
    if ($hash != $row['PasswordHash'])
    {
        echo 'Access denied'; // wrong password
        exit();
    }
}
$_SESSION['signed_in'] = true;
$_SESSION['user_id'] = $row['UserId'];
$_SESSION['user_login'] = $login;

echo 'Success';

?>