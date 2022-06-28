<script type="text/javascript" src="/db/fav.js" ></script>
<p>Заполнив контактную информацию в приведенной ниже форме, вы можете отправить нам заявку на выбранные объекты недвижимости.</p>
<div class="rt-floatright">
    <span>Добавлено в список заявок: </span><span id="num-advert">0</span>
    <a id="dvs-alink" class="dvs-alink">Очистить</a>
</div>
<div class="clr"></div>
<table class="dvs-table">
    <tr>
        <th class="favorite"><span class="icon-star"></span></th>
        <th>№</th>
        <th>Фотография</th>
        <th>Адрес</th>
        <th>Этаж</th>
        <th>Площадь, м<sup>2</sup><span>(общ / жил / кух)</span></th>
        <th>Цена, руб.</th>
    	<th>Контакты</th>
    </tr>
        {foreach item=item from=$items name=nm key=key}
            <!--<a href="?mid={$item.id}">{$item.id}</a> | {$item.city} | {$item.street|capitalize} | {$item.loc|capitalize} | {$item.s_all}/{$item.s_live}/{$item.s_co} | {$item.price} | {$item.date_mod} <br/>-->
    <tr>
        <td><input type="checkbox" value="{$item.dbid}_{$item.id}"></td>
        <td><a href="index.php?option=com_mulis&mid={$item.id}&tb={$tbid}&Itemid=131">{$item.id}</a></td>
        <td>{if $item.photos!=0}<a data-rokbox href="/db/base/db_img/m/t{$tb}/{$item.id}/0.jpg"><img class="dvs-advert-item-img" src="/db/base/db_img/m/t{$tb}/{$item.id}/0.jpg" /></a>{else}<img class="dvs-advert-item-img" src="/images/nophoto.png" />{/if}</td>
        <td onclick="window.location.href='index.php?option=com_mulis&mid={$item.id}&tb={$tbid}&Itemid=131'; return false;">{$item.street|capitalize}, {$item.nhome} ({$item.loc|capitalize})</td>
        <td onclick="window.location.href='index.php?option=com_mulis&mid={$item.id}&tb={$tbid}&Itemid=131'; return false;">{$item.floor}/{$item.afloor}</td>
        <td onclick="window.location.href='index.php?option=com_mulis&mid={$item.id}&tb={$tbid}&Itemid=131'; return false;">{$item.s_all}/{$item.s_live}/{$item.s_co}</td>
        <td onclick="window.location.href='index.php?option=com_mulis&mid={$item.id}&tb={$tbid}&Itemid=131'; return false;">{$item.price}</td>
        <td>{$agents[$item.idr].contact}</td>
    </tr>
        {foreachelse}
    <tr>
        <td colspan="8">Нет объектов</td>
    </tr>
        {/foreach}
</table>