<?php
include __DIR__ .'/init.php';

$id = isset($_REQUEST['mid'])?(int)$_REQUEST['mid']:1;
$db_name =  MulisWeb::isAllowedDb(MulisWeb::requestDbName($table));
$seo['title'] = MulisWeb::getDbAlias($table," в Пензе");

$cid = md5($id. $db_name);
$tmpl = "object.tpl";
if (!$smarty->isCached($tmpl,$cid)) {
    $mulis = new MulisWeb();
    $db = array($mulis->prepare($db_name, "SELECT * FROM $db_name WHERE id=$id"));
    
    $smarty->assign('item', $db[0][0]);
    $smarty->assign('agents', $mulis->agents);
    $smarty->assign('alias', $mulis->alias);
    $smarty->assign('tb', $db_name);
    $smarty->assign('tbid', $table);
    
    $seo['title']=$seo['title']." | ".$db[0][0]['city'].", ".$db[0][0]['street'].$db[0][0]['nhome'];
}
$smarty->display($tmpl, $cid);
?>