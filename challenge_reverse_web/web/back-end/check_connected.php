<?php

if(!isset($_SESSION[$session_version]['signed_in']) || !$_SESSION[$session_version]['signed_in'])
{
	echo 'Access denied';
	exit();
}

?>
