<?php

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('admin/conf.php');
require_once('admin/pdo2.php');


if ((isset($_POST['user_login'])) && (!empty($_POST['user_login'])) && 
	(isset($_POST['user_password'])) && (!empty($_POST['user_password'])) &&
	(isset($_POST['user_firstname'])) && (!empty($_POST['user_firstname'])) &&
	(isset($_POST['user_lastname'])) && (!empty($_POST['user_lastname'])) &&
	(isset($_POST['user_email'])) && (!empty($_POST['user_email'])) &&
	(isset($_POST['user_latitude'])) && (!empty($_POST['user_latitude'])) &&
	(isset($_POST['user_longitude'])) && (!empty($_POST['user_longitude'])))
{
	$pdo =null;
	try {
		$pdo = PDO2::getInstance();
		$insert = $pdo->prepare("INSERT INTO user(login,
			password,
			firstname,
			lastname,
			email,
			latitude,
			longitude)
			VALUES(:user_login,
				:user_password,
				:user_firstname,
				:user_lastname,
				:user_email,
				:user_latitude,
				:user_longitude);
				");
			$password = sha1($_POST['user_password']);
			$insert->bindParam(':user_login',$_POST['user_login']);
			$insert->bindParam(':user_password',$password;
			$insert->bindParam(':user_firstname',$_POST['user_firstname']);
			$insert->bindParam(':user_lastname',$_POST['user_lastname']);
			$insert->bindParam(':user_email',$_POST['user_email']);
			$insert->bindParam(':user_latitude',$_POST['user_latitude']);
			$insert->bindParam(':user_longitude',$_POST['user_longitude']);
		$ret = $insert->execute();

		if ($ret){
			die(json_encode(Array("Status"=>"Ok")));
		}
		else{
			die(json_encode(Array("Status"=>"NOk")));
		}

	}
	catch (Exception $e) {
		echo $e;


		exit();
	}
}else{
	die(json_encode(Array("Status"=>"unauthorized")));
}
?>