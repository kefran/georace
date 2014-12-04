<?php
header ( "Cache-Control: no-cache, must-revalidate" );
header ( "Expires: Mon, 26 Jul 1997 05:00:00 GMT" );
header ( 'content-type: application/json; charset=utf-8' );

require_once ('admin/conf.php');
require_once ('admin/pdo2.php');

//$_POST ['checkpoint'] = 2;

if ((isset ( $_POST ['checkpoint'] )) && (! empty ( $_POST ['checkpoint'] ))) {
	$pdo = null;
	try {
		$pdo = PDO2::getInstance ();
		$select = $pdo->prepare ( "
			SELECT 
	c.id
	,c.name
	,c.latitude
	,c.longitude
	,c.photo
	,c.creator
FROM checkpoint c
INNER JOIN track_checkpoint tc 
on tc.checkpoint=c.id
WHERE tc.track=:trackid;" );
		$trackid =$_POST ['checkpoint'] ;
		$select->bindParam ( "trackid", $trackid );
		$select->execute ();
		
		if ($select->rowCount () <= 0) {
			die ( json_encode ( Array (
					"Status" => "unauthorized" 
			) ) );
		} else {
			$data = $select->fetchAll ( PDO::FETCH_ASSOC );
			die ( json_encode ( $data ) );
		}
	} catch ( Exception $e ) {
		echo ('HTTP/1.0 456 Unrecoverable Error');
		header ( 'HTTP/1.0 456 Unrecoverable Error' );
		
		exit ();
	}
} else {
	die ( json_encode ( Array (
			"Status" => "unauthorized" 
	) ) );
}
?>