<?php
  class PDO2 extends PDO
  {
      private static $instance;
      public function __construct()
      {
      	
      }
      public static function getInstance()
      {
          try
          {
              if (!isset(self::$instance))
              {
                  self::$instance = new PDO(
                          SQL_DSN, SQL_USERNAME, SQL_PASSWORD, array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8", PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
              }
              return self::$instance;
          } catch (Exception $e)
          {
              $fp = fopen('pdoError.txt', 'a');
              $errorString = "[" . date("Y-m-d H:i:s") . "] [" . $_SERVER['REMOTE_ADDR'] . "] [" . gethostbyaddr($_SERVER['REMOTE_ADDR']) . "] [" . $_SERVER['PHP_SELF'] . " ] " . $e->getMessage() . "\n";
              fwrite($fp, $errorString);
              fclose($fp);
              die('MySQL Error, please contact us');
          }
      }
  }
?>