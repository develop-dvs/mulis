<?php
    $gen_start = microtime(true);
    
    require_once './MulisWeb.php';
    $mulis = new MulisWeb();
    //$mulis->prepare("s_kvart","SELECT * FROM s_kvart");
    $mulis->prepareBd();
    
    print "Page generated in ".round((microtime(true) - $gen_start), 4)." seconds";

?>
