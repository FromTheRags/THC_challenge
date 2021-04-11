<?php
/*
error_reporting(E_ALL);
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
*/
session_start();

if(isset($_GET['sign_out']))
{
	$_SESSION = array();
}

if(!isset($_SESSION['signed_in']) || !$_SESSION['signed_in'])
{
	header("Location: login.php");
	exit();
}

?>

<html>

<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<title>PictureParadise</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<link rel="icon" href="logo.svg" />
</head>

<body>
	<header>
	<nav class="navbar navbar-expand navbar-light bg-light">
		<a class="navbar-brand" href="?page=home.php">PictureParadise</a>
		<ul class="navbar-nav mr-auto">
			<li class="nav-item"><a class="nav-link<?php echo (isset($_GET['page']) && $_GET['page'] == "home.php") ? " active" : ""; ?>" href="?page=home.php">Home</a></li>
			<li class="nav-item"><a class="nav-link<?php echo (isset($_GET['page']) && $_GET['page'] == "show.php") ? " active" : ""; ?>" href="?page=show.php">Show photos</a></li>
			<li class="nav-item"><a class="nav-link<?php echo (isset($_GET['page']) && $_GET['page'] == "upload.php") ? " active" : ""; ?>" href="?page=upload.php">Upload new photo</a></li>
		</ul>
		<span class="navbar-text" style="margin-right: 10px">
			<?php echo ucfirst($_SESSION['user_login']); ?>
		</span>
		<a role="button" class="btn btn-danger" style="border-radius: 20px" href="?sign_out=true">Sign out</a>
	</nav>
	</header>

	<section>
	<?php

	if(!isset($_GET['page']) || str_contains($_GET['page'], "..") || $_GET['page'][0] == "/" || !file_exists("/var/www/localhost/htdocs/" . $_GET['page']))
	{
		echo "<p>Error, unknown page</p>";
	}
	else
	{
		require_once($_GET['page']);
	}

	?>
	</section>
</body>

</html>
