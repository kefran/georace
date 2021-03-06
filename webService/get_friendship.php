<?php

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('admin/conf.php');
require_once('admin/pdo2.php');


if ((isset($_POST['friendship'])))
{
	$pdo =null;
	try {
		$pdo = PDO2::getInstance();
		$select = $pdo->prepare("
			SELECT 
				f.user
				,f.friend
				,f.date
			FROM 
				friendship f
			WHERE 
				f.user = ?
				;");
		$select->bindParam(1,$_POST['friendship']);
		$select->execute();

	
			$data=$select->fetchAll(PDO::FETCH_ASSOC);
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