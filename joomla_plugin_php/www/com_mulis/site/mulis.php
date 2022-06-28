<?php
// Запрет прямого доступа.
defined('_JEXEC') or die;

// Подключаем логирование.
//JLog::addLogger(array('text_file' => 'com_helloworld.php'), JLog::ALL, array('com_helloworld'));

// Подключаем библиотеку контроллера Joomla.
jimport('joomla.application.component.controller');

// Получаем экземпляр контроллера с префиксом HelloWorld.
$controller = JControllerLegacy::getInstance('Mulis');

// Исполняем задачу task из Запроса.
$input = JFactory::getApplication()->input;
$controller->execute($input->getCmd('task'));
 
// Перенаправляем, если перенаправление установлено в контроллере.
$controller->redirect();
$itemid=JRequest::getInt("Itemid");
//$orderby=JRequest::getString("order_by");
$pagination=1;

include ___DIR__.'../../db/index.php';
$mydoc =& JFactory::getDocument();
$mydoc->setTitle($seo['title']);
//$mydoc->setMetaData('keywords', $seo['keywords']);
//$mydoc->setMetaData( 'description', $seo['description']);