<?php
// no direct access
defined('_JEXEC') or die;

//$moduleclass_sfx = htmlspecialchars($params->get('moduleclass_sfx'));
$pagination=$params->get('show_pagination');
$cnt=$params->get('count_item');
$orderby=$params->get('order_by');
$where_in="BLANK";
$from=0;
$table=$params->get('table');
include ___DIR__.'../../db/list_short.php';
//require JModuleHelper::getLayoutPath('mod_mulis', $params->get('layout', 'default'));
