<div class="row justify-content-center">
	<div class="col-11">

<?php

$pdo = new PDO("mysql:dbname=PictureParadise;host=localhost;port=3306", "web", "spartanswhatisyourprofession?motdepasse!!!moo***ootdepasse!aouhaouh!!");

$friends = array(array('FriendId' => $_SESSION['user_id'], 'FriendLogin' => $_SESSION['user_login']));

$stm = $pdo->prepare("SELECT Users.UserId AS FriendId, Users.Login AS FriendLogin FROM Friends INNER JOIN Users ON Users.UserId = Friends.User1Id WHERE User2Id = :user_id");
$stm->bindParam(":user_id", $_SESSION['user_id'], PDO::PARAM_INT);
$stm->execute();
$rows = $stm->fetchAll(PDO::FETCH_ASSOC);
foreach($rows as $row)
{
	array_push($friends, $row);
}

$stm = $pdo->prepare("SELECT Users.UserId AS FriendId, Users.Login AS FriendLogin FROM Friends INNER JOIN Users ON Users.UserId = Friends.User2Id WHERE User1Id = :user_id");
$stm->bindParam(":user_id", $_SESSION['user_id'], PDO::PARAM_INT);
$stm->execute();
$rows = $stm->fetchAll(PDO::FETCH_ASSOC);
foreach($rows as $row)
{
	array_push($friends, $row);
}

$once = false;
foreach($friends as $friend)
{
	echo "<br />";
	if($friend['FriendId'] == $_SESSION['user_id'])
	{
		echo "<h3 class=\"font-weight-bold\"> My pictures</h3>";
	}
	else
	{
		if(!$once)
		{
			echo "<h3 class=\"font-weight-bold\"> My friends' pictures</h3>";
			echo "<br />";
			$once = true;
		}
		echo "<h4 class=\"font-weight-normal\"> " . ucfirst($friend['FriendLogin']) . "'s pictures</h4>";
	}
	echo "<br />";
	echo "<div class=\"card-columns\">";

	$picture_dir_path = "/var/www/localhost/htdocs/uploads/" . $friend['FriendId'] . "_";

	$stm = $pdo->prepare("SELECT FileName, Title, Description, `Key`, `DateTime` FROM Pictures WHERE " . (($friend['FriendId'] != $_SESSION['user_id']) ? "`Key` IS NULL AND " : "") . "UserId = :user_id ORDER BY `DateTime` DESC");
	$stm->bindParam(":user_id", $friend['FriendId'], PDO::PARAM_INT);
	$stm->execute();
	$rows = $stm->fetchAll(PDO::FETCH_ASSOC);

	foreach ($rows as $row)
	{
		$picture_file_name = $row['FileName'];
		$picture_title = $row['Title'];
		$picture_description = $row['Description'];
		$picture_upload_date = $row['DateTime'];
		$key = $row['Key'];

		$picture_file_path = $picture_dir_path . $picture_file_name . (!is_null($key) ? ".enc" : "");
		$picture_type = pathinfo($picture_file_path, PATHINFO_EXTENSION);
		$picture_data = file_get_contents($picture_file_path);

		if (!is_null($key))
		{
			list($ciphertext, $iv) = explode('::', base64_decode($picture_data), 2);
			$picture_data = openssl_decrypt($ciphertext, 'aes-256-cbc', $key, 0, $iv);
		}

		$picture_base64 = "data:image/" . $picture_type . ";base64," . base64_encode($picture_data);

		?>

		<div class="card">
			<img class="card-img-top" src="<?= $picture_base64; ?>">
			<div class="card-body"<?= !is_null($key) ? " style=\"background-color: ffe8e8\"" : ""; ?>>
				<h5 class="card-title"><span title="This picture is <?= !is_null($key) ? "private" : "public"; ?>"><?= !is_null($key)  ? "🔒" : "🔓"; ?></span> <?= $picture_title; ?></h5>
				<?php if($picture_description != "") { ?><p class="card-text"><?= $picture_description; ?></p><?php } ?>
				<p><i><?= $picture_upload_date; ?></i></p>
			</div>
		</div>

		<?php

	}

	if(count($rows) == 0)
	{
		echo "<p class=\"card font-weight-normal\" style=\"border: none\">No pictures</p>";
	}

	echo "</div>";
}

?>

	</div>
</div>

