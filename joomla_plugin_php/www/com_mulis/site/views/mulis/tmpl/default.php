<?php
// Запрет прямого доступа.
defined('_JEXEC') or die;
?>
<?php if ($this->params->get('show_page_heading')) : ?>
<h1>
	<?php echo $this->escape($this->params->get('page_heading')); ?>
</h1>
<?php endif; ?>