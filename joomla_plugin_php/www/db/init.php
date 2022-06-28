<?php
defined('_JEXEC') or die;
require_once __DIR__ .'/smarty/Smarty.class.php';
require_once __DIR__ .'/base/MulisWeb.php';
$smarty = new Smarty();
$smarty->caching = false;
$smarty->debugging = false;
$smarty->compile_dir = __DIR__ .'/templates_c';
$smarty->template_dir = __DIR__ .'/templates';
$smarty->cache_dir = __DIR__ .'/cache';
?>
