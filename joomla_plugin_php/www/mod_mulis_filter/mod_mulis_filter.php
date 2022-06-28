<?php
// no direct access
defined('_JEXEC') or die;

$moduleclass_sfx = htmlspecialchars($params->get('moduleclass_sfx'));
$address = JRequest::getString("loc");
$price_from = JRequest::getString("price_min");
$price_to = JRequest::getString("price_max");
$s_from = JRequest::getString("s_all_min");
$s_to = JRequest::getString("s_all_max");
$itemid = JRequest::getInt("Itemid");
$table = JRequest::getInt("tb");
require JModuleHelper::getLayoutPath('mod_mulis_filter', $params->get('layout', 'default'));
