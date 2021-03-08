<?php

error_reporting(E_ALL);
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

if(isset($_POST['login']) && isset($_POST['password']))
{
	$pdo = new PDO("mysql:dbname=PictureParadise;host=localhost;port=3306", "web", "spartanswhatisyourprofession?motdepasse!!!moo***ootdepasse!aouhaouh!!");
	$stm = $pdo->query("SELECT UserId, Description FROM Users WHERE Login = '" . $_POST['login'] . "' AND Password = '" . $_POST['password'] . "'");

	if($stm->rowCount() != 0)
	{
		$row = $stm->fetch(PDO::FETCH_ASSOC);

		session_start();

		$_SESSION['signed_in'] = true;
		$_SESSION['user_id'] = $row['UserId'];
		$_SESSION['user_login'] = $_POST['login'];
		$_SESSION['user_profile'] = $row['Description'];

		header('Location: index.php?page=home.php');
		exit();
	}
	else
	{
		$error = 'Access denied'; // wrong login or password
	}
}

?>

<html>

<head>
	<meta charset="utf-8">
	<title>PictureParadise - Sign in</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
	<link rel="icon" href="logo.svg" />
</head>

<body>
	<div class="row justify-content-center align-items-center" style="height: 100%">
		<div class="col-4">
			<form action="" method="POST">
				<div class="form-group">
					<img src="logo.svg" style="width: 300px; margin-left: auto; margin-right: auto; display: block" />
				</div>
				<br />
				<br />
				<div class="form-group">
					<h2 style="text-align: center">Please, sign in</h2>
				</div>
				<br />
				<div class="form-group">
					<label for="sign-in-login" style="font-size: 20px; margin-bottom: 5px">Login</label>
					<input id="sign-in-login" type="text" placeholder="Type your login" name="login" class="form-control" style="height: 50px" required>
				</div>
				<br />
				<div class="form-group">
					<label for="sign-in-password" style="font-size: 20px; margin-bottom: 5px">Password</label>
					<input id="sign-in-password" type="password" placeholder="Type your password" name="password" class="form-control" style="height: 50px" required>
				</div>
				<div class="form-row justify-content-center">
					<p><?php if(isset($error)) echo $error; ?></p>
				</div>
				<br />
				<div class="form-group">
					<button type="submit" class="btn btn-success" style="width: 100%; height: 50px">Sign in</button>
				</div>
			</form>
		</div>
	</div>
</body>

</html>

