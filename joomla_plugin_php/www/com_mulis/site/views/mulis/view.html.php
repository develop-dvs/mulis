<?php
// Запрет прямого доступа.
defined('_JEXEC') or die;

// Подключаем библиотеку представления Joomla.
jimport('joomla.application.component.view');

/**
 * HTML представление сообщения компонента Mulis.
 */
class MulisViewMulis extends JViewLegacy
{
	/**
	 * Сообщение.
	 *
	 * @var string 
	 */
	protected $msg;

	/**
	 * Параметры.
	 *
	 * @var object
	 */
	protected $params;

	/**
	 * Переопределяем метод display класса JViewLegacy.
	 * 
	 * @return void
	 */
	public function display($tpl = null) 
	{
		// Получаем сообщение.
		$this->msg = $this->get('Msg');

		// Есть ли ошибки.
		if (count($errors = $this->get('Errors')))
		{
			foreach ($errors as $error)
			{
				JLog::add($error, JLog::ERROR, 'com_mulis');
			}
		}

		// Получаем параметры приложения.
		$app = JFactory::getApplication();
		$this->params = $app->getParams();

		// Подготавливаем документ.
		$this->_prepareDocument();

		// Отображаем представление.
		parent::display($tpl);
	}

	/**
	 * Подготавливает документ.
	 *
	 * @return void
	 */
	protected function _prepareDocument()
	{
		$app = JFactory::getApplication();
		$menus = $app->getMenu();
		$title = null;

		// Так как приложение устанавливает заголовок страницы по умолчанию, 
		// мы получаем его из пункта меню.
		$menu = $menus->getActive();

		if ($menu)
		{
			$this->params->def('page_heading', $this->params->get('page_title', $menu->title));
		}
		else 
		{
			$this->params->def('page_heading', JText::_('COM_HELLOWORLD_DEFAULT_PAGE_TITLE'));
		}

		// Получаем заголовок страницы в браузере из параметров.
		$title = $this->params->get('page_title', '');

		if (empty($title)) 
		{
			$title = $app->getCfg('sitename');
		}
		elseif ($app->getCfg('sitename_pagetitles', 0) == 1) 
		{
			$title = JText::sprintf('JPAGETITLE', $app->getCfg('sitename'), $title);
		}
		elseif ($app->getCfg('sitename_pagetitles', 0) == 2) 
		{
			$title = JText::sprintf('JPAGETITLE', $title, $app->getCfg('sitename'));
		}
		else 
		{
			$title = $this->msg;
		}

		// Устанавливаем заголовок страницы в браузере.
		//$this->document->setTitle($title);
	}
}