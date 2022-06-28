<div class="dvs-advert-list">
    {foreach item=item from=$items name=nm key=key}
    <div class="dvs-advert-item{if ($smarty.foreach.nm.index+1) % 4 == 0} dvs-advert-last{/if}">
        <h3>{$item.street|capitalize}, {$item.nhome}</h3>
        <div class="dvs-advert-item-img">
        <a href="index.php?option=com_mulis&mid={$item.id}&tb={$tbid}">{if $item.photos!=0}<img class="dvs-advert-item-img" src="/db/base/db_img/m/t{$tb}/{$item.id}/0.jpg" />{else}<img class="dvs-advert-item-img" src="/images/nophoto.png" />{/if}</a>
        </div>
        <p>{$item.loc|capitalize}<br>Этаж: {$item.floor}; Площадь: {$item.s_all}; Цена: {$item.price} руб.</p>
            <a class="readon" href="index.php?option=com_mulis&mid={$item.id}&tb={$tbid}">Подробнее</a>
    </div>
    {if ($smarty.foreach.nm.index+1) % 4 == 0}<div class="clr"></div>{/if}
    {foreachelse}
    {/foreach}
</div>