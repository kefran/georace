<?php

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('admin/conf.php');
require_once('admin/pdo2.php');



if ((isset($_POST['checkpoint'])) && (!empty($_POST['checkpoint'])))
{
	$pdo =null;
	try {
		$pdo = PDO2::getInstance();
		$select = $pdo->prepare("INSERT INTO `georace`.`checkpoint`
				 (`name`, `latitude`, `longitude`, `photo`, `creator`)
				 VALUES ( :name, :latitude, :longitude, :photo, :creator);
				;");
		
		$select->bindParam(':name',$_POST['name']);
		$select->bindParam(':latitude',$_POST['latitude']);
		$select->bindParam(':longitude',$_POST['longitude']);
		$select->bindParam(':photo',$_POST['photo']);
		$select->bindParam(':creator',$_POST['creator']);
		
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