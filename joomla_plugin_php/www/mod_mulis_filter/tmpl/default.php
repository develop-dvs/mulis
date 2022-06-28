<?php
defined('_JEXEC') or die;
?>

<div class="dvs-filter<?php echo $moduleclass_sfx ?>">
    <form method="GET" action="index.php">
        <input type="hidden" name="option" value="com_mulis" />
        <input type="hidden" name="tb" value="<?=$table?>" />
        <input type="hidden" name="Itemid" value="<?=($itemid==101)?141:$itemid?>" />
        <table>
            <tbody><tr>
                    <td>
                        <label for="inputAddress">Адрес</label>
                    </td>
                    <td colspan="3">
                        <input id="inputAddress" name="loc" placeholder="Адрес" class="input-xxlarge" type="text" value="<?=$address?>" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="inputPrice">Цена</label>
                    </td>
                    <td>
                        <input id="inputPrice1" name="price_min" placeholder="от" class="input-small" type="text" value="<?=$price_from?>" />
                        —
                        <input id="inputPrice2" name="price_max" placeholder="до" class="input-small" type="text" value="<?=$price_to?>" />
                        <span class="help-inline">руб.</span>
                    </td>
                    <td>
                        <label for="inputSquare">Площадь</label>
                    </td>
                    <td>
                        <input id="inputSquare1" name="s_all_min" placeholder="от" class="input-small" type="text" value="<?=$s_from?>" />
                        —
                        <input id="inputSquare2" name="s_all_max" placeholder="до" class="input-small" type="text" value="<?=$s_to?>" />
                        <span class="help-inline">м. кв.</span>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <button type="submit" class="btn btn-inverse rt-floatright">Поиск</button>
                    </td>
                </tr>
            </tbody></table>
    </form>
</div>