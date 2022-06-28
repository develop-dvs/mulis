<?php
include __DIR__ .'/init.php';

$db_name =  MulisWeb::isAllowedDb(MulisWeb::requestDbName($table));

$where = MulisWeb::buildWhereFilter($where_in,$request);
$order = MulisWeb::buildOrder($orderby);
$limit = MulisWeb::buildLimit($from,$cnt);

$cid = md5($where . $order . $limit);
$tmpl = "list_short.tpl";
if (!$smarty->isCached($tmpl,$cid)) {
    $mulis = new MulisWeb();
    
    $sql_count="SELECT COUNT(1) FROM $db_name WHERE state=1 $where";
    $dbg[] = $sql_count;
    $all = $mulis->querySingle($db_name, $sql_count);
    
    $sql_where="SELECT * FROM $db_name WHERE state=1 $where $order $limit";
    $dbg[] = $sql_where;
    $db = array($mulis->prepare($db_name, $sql_where));
    
    $smarty->assign('items', $db[0]);
    $smarty->assign('cnt', $cnt);
    $smarty->assign('all', $all);
    $smarty->assign('tb', $db_name);
    $smarty->assign('tbid', $table);
    $smarty->assign('pagination', $pagination);
}
$smarty->display($tmpl, $cid);
?>