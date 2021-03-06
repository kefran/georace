<?php

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('admin/conf.php');
require_once('admin/pdo2.php');



if ((isset($_POST['userLogin'])) && (!empty($_POST['userLogin'])) && (isset($_POST['userPassword'])) && (!empty($_POST['userPassword'])))
{
	$pdo =null;
	try {
		$pdo = PDO2::getInstance();
		$selectUser = $pdo->prepare("SELECT u.id
											,u.login
											,u.firstname
											,u.lastname
											,u.email
											,u.latitude
											,u.longitude
									 FROM user u
									 WHERE login = :userLogin
									 AND password = :userPassword;");
		$pwd = sha1($_POST['userPassword']);
		$selectUser->bindParam(':userLogin',$_POST['userLogin'] ,PDO::PARAM_STR);
		$selectUser->bindParam(':userPassword',$pwd ,PDO::PARAM_STR);
		$selectUser->execute();

		if ($selectUser->rowCount()!=1){
			die(json_encode(Array("Status"=>"unauthorized")));
		}
		else{
			$user=$selectUser->fetch(PDO::FETCH_ASSOC);
			die(json_encode($user));
		}

	}
	catch (Exception $e) {
		echo('HTTP/1.0 456 Unrecoverable Error');
		header('HTTP/1.0 456 Unrecoverable Error');

		exit();
	}
}else{
	die(json_encode(Array("Status"=>"unauthorized")));
}
?>