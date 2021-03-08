<?php

$pdo = new PDO("mysql:dbname=PictureParadise;host=localhost;port=3306", "web", "spartanswhatisyourprofession?motdepasse!!!moo***ootdepasse!aouhaouh!!");

$friends = array();

$stm = $pdo->prepare("SELECT Users.Login AS FriendLogin FROM Friends INNER JOIN Users ON Users.UserId = Friends.User1Id WHERE User2Id = :user_id");
$stm->bindParam(":user_id", $_SESSION['user_id'], PDO::PARAM_INT);
$stm->execute();
$rows = $stm->fetchAll(PDO::FETCH_ASSOC);
foreach($rows as $row)
{
	array_push($friends, ucfirst($row['FriendLogin']));
}

$stm = $pdo->prepare("SELECT Users.Login AS FriendLogin FROM Friends INNER JOIN Users ON Users.UserId = Friends.User2Id WHERE User1Id = :user_id");
$stm->bindParam(":user_id", $_SESSION['user_id'], PDO::PARAM_INT);
$stm->execute();
$rows = $stm->fetchAll(PDO::FETCH_ASSOC);
foreach($rows as $row)
{
	array_push($friends, ucfirst($row['FriendLogin']));
}


$picture_dir_path = "/var/www/localhost/htdocs/uploads/" . $_SESSION['user_id'] . "_";

$stm = $pdo->prepare("SELECT FileName, `Key`, `DateTime` FROM Pictures WHERE UserId = :user_id ORDER BY `DateTime` DESC LIMIT 10");
$stm->bindParam(":user_id", $_SESSION['user_id'], PDO::PARAM_INT);
$stm->execute();

$no_image = $stm->rowCount() == 0;

if(!$no_image)
{
	$row = $stm->fetch(PDO::FETCH_ASSOC);

	$picture_file_name = $row['FileName'];
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
}

?>

<br />
<div class="row justify-content-center">
	<div class="col-10">
		<h3 class="font-weight-bold"> Home</h3>
		<br />
		<div class="card-deck">
			<div class="card">
				<div class="card-body">
					<h5 class="card-title">Profile</h5>
					<p class="card-text"><?= $_SESSION['user_profile']; ?></p>
				</div>
			</div>

			<?php

			if(!$no_image)
			{

			?>

			<div class="card">
				<img class="card-img-top" src="<?= $picture_base64; ?>">
				<div class="card-body">
					<h5 class="card-title">Last uploaded picture</h5>
					<p class="card-text"><i><?= $picture_upload_date; ?></i></p>
				</div>
			</div>

			<?php

			}

			?>

			<div class="card">
				<div class="card-body">
					<h5 class="card-title">Friends</h5>
					<p class="card-text"><?= implode(", ", $friends); ?></p>
				</div>
			</div>
		</div>
	</div>
</div>

