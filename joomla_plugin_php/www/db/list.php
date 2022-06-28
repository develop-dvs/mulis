<?php
include __DIR__ .'/init.php';

$db_name = MulisWeb::isAllowedDb(MulisWeb::requestDbName($table));
$tbalias=MulisWeb::getDbSHAlias($table);
$seo['title'] = MulisWeb::getDbAlias($table," в Пензе");

$where = MulisWeb::buildWhereFilter($where_in,$request);
$orderby="date_mod";
$order = MulisWeb::buildOrder($orderby);
$limit = MulisWeb::buildLimit($from,$cnt);

//$request[]="Itemid=$itemid";
$request[]="cnt=$cnt";

$cid = md5($where . $order . $limit);
$tmpl = "list.tpl";
if (!$smarty->isCached($tmpl,$cid)) {
    $mulis = new MulisWeb();
    
    $sql_count="SELECT COUNT(1) FROM $db_name WHERE state=1 $where";
    $dbg[] = $sql_count;
    $all = $mulis->querySingle($db_name, $sql_count);
    
    $sql_where="SELECT * FROM $db_name WHERE state=1 $where $order $limit";
    $dbg[] = $sql_where;
    $db = array($mulis->prepare($db_name, $sql_where));
    
    $smarty->assign('items', $db[0]);
    $smarty->assign('agents', $mulis->agents);
    $smarty->assign('cnt', $cnt);
    $smarty->assign('from', $from);
    $smarty->assign('all', $all);
    $smarty->assign('tb', $db_name);
    $smarty->assign('tbalias', $tbalias);
    $smarty->assign('tbid', $table); //
    $smarty->assign('request', implode("&", $request));
    $smarty->assign('itemid', $itemid);
    $smarty->assign('pagination', $pagination);
}
$smarty->display($tmpl, $cid);
?>