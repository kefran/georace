<?php

header("Cache-Control: no-cache, must-revalidate");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header('content-type: application/json; charset=utf-8');

require_once('admin/conf.php');
require_once('admin/pdo2.php');


if ((isset ( $_POST['user'] ) && isset($_POST['race'])) ) {
	$pdo = null;
	try {
		$pdo = PDO2::getInstance ();
		$select = $pdo->prepare ( "SELECT user,race,checkpoint,date_check FROM georace.`check` 
				WHERE user= :userid AND race= :raceid ;" );

		$select->bindParam ( ":userid", $_POST['user'] );
		$select->bindParam ( ":raceid", $_POST['race'] );

		$select->execute ();
			
		$data = $select->fetchAll ( PDO::FETCH_ASSOC );
		die ( json_encode ( $data ) );
		
	} catch ( Exception $e ) {
		$message =$e->getMessage();
		die ( json_encode ( Array("Status"=>$message)) );
		exit ();
	}
} else {
	die ( json_encode ( Array (
			"Status" => "unauthorized" 
	) ) );
}

?>