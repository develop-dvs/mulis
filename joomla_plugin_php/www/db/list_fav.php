<?php
include __DIR__ .'/init.php';

function add_dbid(&$item,$key,$dbid) {
    $item["dbid"]=$dbid;
}

if (isset($_COOKIE["dvs-atlas"])) {
    $obj = explode(",", $_COOKIE["dvs-atlas"]);
    $db_use=array();
    foreach ($obj as $val) {
        $inObj=explode("_", $val);
        if (count($inObj)==2) {
            $db_use[(int)$inObj[0]][]=(int)$inObj[1];;
        }
    }
    $mulis = new MulisWeb();
    $results = array();
    foreach ($db_use as $dbid => $items) {
        $db_name = MulisWeb::isAllowedDb(MulisWeb::requestDbName($dbid));
        
        $sql_where="SELECT * FROM $db_name WHERE state=1 AND id IN(".implode(",", $items).")";
        $dbg[] = $sql_where;
        $result_one=$mulis->prepare($db_name, $sql_where);
        array_walk($result_one, 'add_dbid', $dbid);
        $results=array_merge($results,$result_one);
    }
    
    if ($hide_body==false) {
        $results=array($results);
        $tmpl = "list_fav.tpl";
        $smarty->assign('items', $results[0]);
        $smarty->assign('agents', $mulis->agents);
        $smarty->display($tmpl); 
    }
}
?>