<?php

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('admin/conf.php');
require_once('admin/pdo2.php');

$_POST['raceID'] = 1;

if ((isset($_POST['raceID'])) && (!empty($_POST['raceID'])))
{
	$pdo =null;
	try {
		$pdo = PDO2::getInstance();
		$select = $pdo->prepare("
			SELECT 
				R.id,
				R.date_start,
				R.date_end,
				R.track,
				R.organizer
			FROM 
				race R
			WHERE
				R.id = :raceID ;");
		$select->bindParam(':raceID',$_POST['raceID'] ,PDO::PARAM_INT);
		$select->execute();

		if ($select->rowCount()!=1){
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