<?php

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('admin/conf.php');
require_once('admin/pdo2.php');



if ((isset($_POST['helloworld'])) && (!empty($_POST['helloworld'])))
{
	$pdo =null;
	try {
		$pdo = PDO2::getInstance();
		$select = $pdo->prepare(";");
		$select->bindParam(':userPassword',$pwd ,PDO::PARAM_STR);
		$select->execute();

		
			$data=$select->fetch(PDO::FETCH_ASSOC);
			die(json_encode($data));
		

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