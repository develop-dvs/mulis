<script type="text/javascript" src="/db/fav.js" ></script>
<div class="advert">
    <h2 class="title dvs-margin">{$item.city}, {$item.street}, {$item.nhome}</h2>
    <div class="rt-grid-3">
    	<div class="block-padding">
		{if $item.photos != 0}<a data-rokbox data-rokbox-album="Atlas" href="/db/base/db_img/m/t{$tb}/{$item.id}/0.jpg"><img class="advert-image-main" src="/db/base/db_img/m/t{$tb}/{$item.id}/0.jpg" /></a>{else}<img src="/images/nophoto.png" />{/if}
        	{if $item.photos > 1}<div class="advert-image-list">
                    {for $photo=1 to $item.photos-1}
                        <a data-rokbox data-rokbox-album="Atlas" href="/db/base/db_img/m/t{$tb}/{$item.id}/{$photo}.jpg"><img src="/db/base/db_img/m/t{$tb}/{$item.id}/{$photo}.jpg" /></a>
                    {/for}
	        </div>{/if}
		</div>
	</div>
	<div class="rt-grid-6">
		<div class="block-padding">
		<table class="table table-striped">
			<tr>
				<td>Число комнат:</td>
				<td>{$item.room}</td>
			</tr>

			<tr>
				<td>Этаж / Этажность:</td>
				<td>{$item.floor} / {$item.afloor}</td>
			</tr>
			<tr>
				<td>Площадь (общ / жил / кух):</td>
				<td>{$item.s_all} / {$item.s_live} / {$item.s_co}</td>
			</tr>
		</table>
<script src="//api-maps.yandex.ru/2.0/?load=package.standard&lang=ru-RU&onload=init" type="text/javascript"></script>
{literal}<script type="text/javascript">
    var myMap; var point;
    function init() {
        
        myMap = new ymaps.Map("YMapsID", {
            center: [53.200726, 45.025952],
            zoom: 16
        });
        myMap.controls.add('zoomControl', { left: 5, top: 5 }).add('typeSelector').add('mapTools', { left: 35, top: 5 });//.add(new ymaps.control.MiniMap({ type: 'yandex#publicMap'}));

        point = ymaps.geocode("{/literal}{$item.city}, {$item.street}, {$item.nhome}{literal}");
        point.then(
            function (res) {
                myPlacemark1 = new ymaps.Placemark(res.geoObjects.get(0).geometry.getCoordinates(), {
                    balloonContent: '{/literal}{$item.city}, {$item.street}, {$item.nhome}{literal}'
                }, {
                    preset: 'twirl#violetIcon'
                });
                myMap.geoObjects.add(myPlacemark1);
                myMap.setCenter(res.geoObjects.get(0).geometry.getCoordinates());
                //alert('Координаты объекта :' + res.geoObjects.get(0).geometry.getCoordinates());
            },
            function (err) {
                alert('Ошибка');
            }
        );
    }
</script>{/literal} 
<div id="YMapsID" class="advert-map"></div>
		</div>
	</div>
	<div class="rt-grid-3">
		<div class="block-padding">
			<div class="advert-price">
				<span>{$item.price} руб.</span>
			</div>
			<div class="advert-contact">
				<h4>Контактные данные</h4>
				<p class="advert-contact-item">{$agents[$item.idr].name}</p>
				<p class="advert-contact-item">{$agents[$item.idr].contact}</p>
			</div>
			<div class="advert-note-add">
				<input value="{$tbid}_{$item.id}" type="checkbox"> <span class="help-inline">Добавить в <a href="index.php?option=com_formcalc&view=formcalc&formid=2&Itemid=157">список заявок</a>:</span>
				<span id="num-advert">0</span>
			</div>
		</div>
	</div>
	<div class="clr"></div>{if $item.comment!="" && $item.comment!="0"}
	<div class="advert-description">
		<h4>Описание:</h4>
                <p>{$item.comment}</p>
	</div>{/if}
	<div class="advert-additional">
		<h4>Дополнительная информация</h4>
		<table class="table table-striped">
                        {foreach item=it from=$item name=nt key=kt}
                            {if $it!="?" && $it!="" && $alias.$kt!="" && $it!="Нет" && !in_array($kt,array("idr","loc","area","city","iswap","floor","afloor","isale","street","nhome","photos","price","auction","room","prepaid","price_a","comment","s_all","s_live","s_co","room_new","comment_private"))}<tr><td>{$alias.$kt}</td><td>{$it}</td></tr>{/if}
                        {/foreach}
		</table>
	</div>
</div>