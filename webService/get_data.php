<?php 

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('conf.php');
require_once('pdo2.php');



$_POST['user'] = "list";
//$_POST['userPassword'] = "luke";

if (isset($_POST['user']))
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
									 FROM user u");
		
		$selectUser->execute();

		if ($selectUser->rowCount()==0){
			die(json_encode(Array("Status"=>"PROBLEM in pdo")));
		}
		else{
			$user=$selectUser->fetchAll(PDO::FETCH_ASSOC);
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