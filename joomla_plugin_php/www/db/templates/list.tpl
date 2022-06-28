<script type="text/javascript" src="/db/fav.js" ></script>
<div class="dvs-advert-note">
	<span>Добавлено в <a href="index.php?option=com_formcalc&view=formcalc&formid=2&Itemid=157">список заявок</a>: </span><span id="num-advert">0</span>
</div>
<h2 class="title">{$tbalias}</h2>
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
        <td><input type="checkbox" value="{$tbid}_{$item.id}"></td>
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

{if $pagination=="1"}
<div class="dvs-pagination">
	<ul class="rt-floatleft">
        {assign var=page_delta value=($all/$cnt)|floor}
            <!--<li class="disabled">
                    <a href="#">«</a>
            </li>-->
            {for $page=0 to $page_delta}
		<li {if ($from>(($page-1)*$cnt) && $from<(($page+1)*$cnt))}class="active"{/if}>
			<a href="index.php?option=com_mulis&from={$page*$cnt}&tb={$tbid}&{$request}&Itemid={$itemid}">{$page+1}</a>
		</li>
             {/for}
            <!--<li>
                    <a href="#">»</a>
            </li>-->
	</ul>
        <div class="rt-floatright">
            <span>Показать на странице</span>
                <ul class="rt-floatright">
                        <li{if $cnt==20} class="active"{/if}><a href="index.php?option=com_mulis&from=0&cnt=20&tb={$tbid}&{$request}&Itemid={$itemid}">20</a></li>
                        <li{if $cnt==40} class="active"{/if}><a href="index.php?option=com_mulis&from=0&tb={$tbid}&{$request}&cnt=40&Itemid={$itemid}">40</a></li>
                        <li{if $cnt==60} class="active"{/if}><a href="index.php?option=com_mulis&from=0&tb={$tbid}&{$request}&cnt=60&Itemid={$itemid}">60</a></li>
                        <li{if $cnt==1000} class="active"{/if}><a href="index.php?option=com_mulis&from=0&tb={$tbid}&{$request}&cnt=1000&Itemid={$itemid}">Все</a></li>
                </ul>
	</div>
	<div class="clr"></div>
</div>
{/if}