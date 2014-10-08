<?php

class PDO2 extends PDO {
	private static $instance;
	public function __construct(){
	}
	public static function getInstance(){
		if(!isset(self::$instance)){
			try{
				self::$instance = new PDO(
					SQL_DSN,
					SQL_USERNAME,
					SQL_PASSWORD,
					array( PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
			} catch (PDOExcepion $e) {
				echo $e;
			}
		}
		return self::$instance;
	}
}
?>