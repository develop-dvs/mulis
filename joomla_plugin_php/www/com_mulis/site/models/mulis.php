<?php
// Запрет прямого доступа.
defined('_JEXEC') or die;

// Подключаем библиотеку modelitem Joomla.
jimport('joomla.application.component.modelitem');

/**
 * Модель сообщения компонента HelloWorld.
 */
class MulisModelMulis extends JModelItem
{
	/**
	 * Сообщение.
	 *
	 * @var string 
	 */
	protected $msg;

	/**
	 * Получаем сообщение.
	 * 
	 * @return string Сообщение, которое отображается пользователю.
	 */
	public function getMsg() 
	{
		if (!isset($this->msg)) 
		{
			// Получаем объект Запроса.
			$input = JFactory::getApplication()->input;

			// Получаем Id сообщения из Запроса.
			$this->msg = $input->getInt('tb');
		}

		return $this->msg;
	}
}