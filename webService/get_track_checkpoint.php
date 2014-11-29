<?php

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('admin/conf.php');
require_once('admin/pdo2.php');

$_POST['track_checkpoint']=1;

if ((isset($_POST['track_checkpoint'])) && (!empty($_POST['track_checkpoint'])))
{
	$pdo =null;
	try {
		$pdo = PDO2::getInstance();
		$select = $pdo->prepare("
			SELECT 
				t.track
				,t.checkpoint
			FROM 
				TRACK_CHECKPOINT  t;");

		$select->execute();

		if ($select->rowCount()<=0){
			die(json_encode(Array("Status"=>"unauthorized")));
		}
		else{
			$data=$select->fetch(PDO::FETCH_ASSOC);
			die(json_encode($data));
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