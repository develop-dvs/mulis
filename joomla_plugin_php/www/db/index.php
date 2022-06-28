<?php
    //$gen_start = microtime(true);
    
    $pages = array(0=>"list",1=>"object");
    $inc = isset($_REQUEST['mid'])?1:0;
    include $pages[$inc].".php";
    
    //print "<br/>Page generated in ".round((microtime(true) - $gen_start), 4)." seconds";
?>