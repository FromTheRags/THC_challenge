<?php

if(isset($_FILES['picture']) && isset($_POST['title']))
{
        $uploaded_name = basename($_FILES['picture']['name']);

	$target_path = "uploads/" . $_SESSION['user_id'] . "_" . $uploaded_name;

        $uploaded_ext = substr($uploaded_name, strrpos($uploaded_name, '.') + 1);
        $uploaded_size = $_FILES['picture']['size'];
	$uploaded_tmp = $_FILES['picture']['tmp_name'];

	if((strtolower($uploaded_ext) == "jpg" || strtolower($uploaded_ext) == "jpeg" || strtolower($uploaded_ext) == "png") && ($uploaded_size < 2000000) && getimagesize($uploaded_tmp))
	{
		$pdo = new PDO("mysql:dbname=PictureParadise;host=localhost;port=3306", "web", "spartanswhatisyourprofession?motdepasse!!!moo***ootdepasse!aouhaouh!!");

		$stm = $pdo->prepare("SELECT count(1) FROM Pictures WHERE UserId = :user_id AND FileName = :file_name");
		$stm->bindParam(":user_id", $_SESSION['user_id'], PDO::PARAM_INT);
		$stm->bindParam(":file_name", $_SESSION['file_name'], PDO::PARAM_STR);
		$cnt = $stm->fetchColumn();
		$duplicate = $cnt > 0;

		if(!$duplicate)
		{
			if(!isset($_POST['public']) || !$_POST['public'])
			{	
				$stm = $pdo->prepare("INSERT INTO Pictures (FileName, Title, Description, UserId, `Key`) VALUES (:file_name, :title, :description, :user_id, :key)");
	
				$key = openssl_random_pseudo_bytes(256/8);
				$stm->bindParam(":key", $key, PDO::PARAM_STR);

				$iv = openssl_random_pseudo_bytes(openssl_cipher_iv_length('aes-256-cbc'));
				$ciphertext = openssl_encrypt(file_get_contents($uploaded_tmp), 'aes-256-cbc', $key, 0, $iv);
				file_put_contents($uploaded_tmp, base64_encode($ciphertext . '::' . $iv));
				$target_path .= ".enc";
			}
			else
			{
				$stm = $pdo->prepare("INSERT INTO Pictures (FileName, Title, Description, UserId) VALUES (:file_name, :title, :description, :user_id)");
			}

			$title = $_POST['title'];
			$description = isset($_POST['description']) ? $_POST['description'] : "";
			$user_id = $_SESSION['user_id'];

			$stm->bindParam(":file_name", $uploaded_name, PDO::PARAM_STR);
			$stm->bindParam(":title", $title, PDO::PARAM_STR);
			$stm->bindParam(":description", $description, PDO::PARAM_STR);
			$stm->bindParam(":user_id", $user_id);
			$stm->execute();

			if(move_uploaded_file($uploaded_tmp, $target_path))
			{
				$error = $uploaded_name . " was succesfully uploaded";
				chmod($target_path, 0755);
	                }
			else
			{
	                        $error = "The picture could not be uploaded";
			}
		}
		else
		{
			$error = "The picture could not be uploaded. A picture having the same name already exists";
		}
        }
	else
	{
                $error = "The picture could not be uploaded, only JPEG or PNG formats are accepted and size must be inferior to 2MB";
        }
}

?>

<br />
<div class="row justify-content-center">
	<div class="col-10">
		<h3 class="font-weight-bold">Upload image</h3>
		<br />
		<form enctype="multipart/form-data" action="" method="POST">
			<div class="form-group">
				<label for="title">Pick a title:</label>
				<input id="title" name="title" type="text" class="form-control" required />
			</div>
			<div class="form-group">
				<label for="description">Describe the picture in a few words:</label>
				<textarea id="description" name="description" class="form-control" rows=4></textarea>
			</div>
			<div class="form-group">
				<input type="hidden" name="MAX_FILE_SIZE" value="2000000" />
				<label for="picture">Choose a picture to upload:</label>
				<input id="picture" name="picture" type="file" class="form-control" required />
			</div>
			<div class="form-check">
				<input id="public" name="public" type="checkbox" class="form-check-input" />
				<label for="public" class="form-check-label">make the picture visible to my friends</label>
			</div>
			<br />
			<button type="submit" class="btn btn-success">Upload</button>
			<pre><?php if(isset($error)) echo $error; ?></pre>
		</form>
	</div>
</div>

