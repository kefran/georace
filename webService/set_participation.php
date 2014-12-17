<?php

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('admin/conf.php');
require_once('admin/pdo2.php');


if ((isset($_POST['participation'])) && (!empty($_POST['participation'])))
{
	$pdo =null;
	try {
		$pdo = PDO2::getInstance();
		$select = $pdo->prepare("INSERT INTO `georace`.`participation` 
				(`user`, `race`, `start`, `end`, `finished`) 
				VALUES ( :user, :race, :start, :end, :finished ););
				;");
		
		$select->bindParam(':user',$_POST['user']);
		$select->bindParam(':race',$_POST['race']);
		$select->bindParam(':start',$_POST['start']);
		$select->bindParam(':end',$_POST['end']);
		$select->bindParam(':finished',$_POST['finished']);
		
		$ret = $select->execute();

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