<?php

session_start();

if(!isset($_SESSION['signed_in']) || !$_SESSION['signed_in'])
{
	echo 'Access denied';
	exit();
}

?>
