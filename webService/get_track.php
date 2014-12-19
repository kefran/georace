<?php

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('admin/conf.php');
require_once('admin/pdo2.php');

$_POST['track']=1;

if ((isset($_POST['track'])) && (!empty($_POST['track'])))
{
	$pdo =null;
	try {
		$pdo = PDO2::getInstance();
		$select = $pdo->prepare("
			SELECT 
				t.id
				,t.name
			FROM 
				track t
				;");
		$select->execute();

			$data=$select->fetchAll(PDO::FETCH_ASSOC);
			die(json_encode($data));
	
	}
	catch (Exception $e) {
		echo $e;
		exit();
	}
}else{
	die(json_encode(Array("Status"=>"unauthorized")));
}
?>