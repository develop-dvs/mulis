<?php

require_once __DIR__ . '/XmlBean.php';

class MulisWeb {

    var $agents = array();
    var $info = array();
    var $alias = array();
    var $db = array();
    var $path_usr = "/db/users.xml";
    var $path_xml = "/db_xml/";
    var $path_db = "/db/";
    var $col_prepare = array("street", "loc");

    function __construct() {
        $this->path_usr = __DIR__ . $this->path_usr;
        $this->path_xml = __DIR__ . $this->path_xml;
        $this->path_db = __DIR__ . $this->path_db;
    }

    public function prepareBd() {
        if (count($this->info) == 0) {
            $this->parse();
        }
        foreach (MulisWeb::getDbNames() as $db) {
            foreach ($this->col_prepare as $col) {
                foreach ($this->info[$col]->nodes as $key => $value) {
                    $value = mb_strtolower($value, "UTF-8");
                    //echo "UPDATE $db SET $col='$value' where $col='$key' <br/>";
                    $this->query($db, "UPDATE $db SET $col='$value' where $col='$key' ");
                }
            }
        }
    }

    public static function getDbNames($index = 0) {
        $arr = array(11 => "s_kvart", 12 => "s_komtat", 13 => "s_kommers", 14 => "s_garazg", 15 => "s_dom", 21 => "r_kvart", 22 => "r_komnat", 23 => "r_kommers", 24 => "r_garazg", 25 => "r_dom");
        if ($index == 0) {
            return $arr;
        }
        else
            return $arr[$index];
    }
    public static function getDbAlias($index = 0,$postfix="") {
        $arr = array(11 => "Купить квартиру", 12 => "Купить комнату", 13 => "Купить коммерческую недвижимость", 14 => "Купить гараж", 15 => "Купить дом", 21 => "Снять квартиру", 22 => "Снять комнату", 23 => "Снять коммерческую недвижимость", 24 => "Снять гараж", 25 => "Снять дом");
        if ($index == 0) {
            return $arr;
        }
        else
            return $arr[$index].$postfix;
    }
    public static function getDbSHAlias($index = 0,$postfix="") {
        $arr = array(11 => "Квартиры", 12 => "Комнаты", 13 => "Купить коммерческую недвижимость", 14 => "Купить гараж", 15 => "Купить дом", 21 => "Снять квартиру", 22 => "Снять комнату", 23 => "Снять коммерческую недвижимость", 24 => "Снять гараж", 25 => "Снять дом");
        if ($index == 0) {
            return $arr;
        }
        else
            return $arr[$index].$postfix;
    }
    public static function isAllowedDb($name, $default = 11) {
        if (array_key_exists($name, MulisWeb::getDbNames()))
            return MulisWeb::getDbNames($name);
        else
            return MulisWeb::getDbNames($default);
    }

    public static function requestDbName(&$name) {
        if (!$name)
            $name = isset($_REQUEST['tb']) ? (int) $_REQUEST['tb'] : 11;
        return $name;
    }

    public static function buildWhereFilter($where = "BLANK",&$request) {
        if ($where == "BLANK")
            return "";
        $where_arr = array();
        $s_all_max = isset($_REQUEST['s_all_max']) ? (int) $_REQUEST['s_all_max'] : "";
        if ($s_all_max != "") {
            $where_arr[] = "s_all <= $s_all_max";
            $request[]="s_all_max=$s_all_max";
        }

        $s_all_min = isset($_REQUEST['s_all_min']) ? (int) $_REQUEST['s_all_min'] : "";
        if ($s_all_min != "") {
            $where_arr[] = "s_all >= $s_all_min";
            $request[]="s_all_min=$s_all_min";
        }

        $price_max = isset($_REQUEST['price_max']) ? (int) $_REQUEST['price_max'] : "";
        if ($price_max != "") {
            $where_arr[] = "price <= $price_max";
            $request[]="price_max=$price_max";
        }

        $price_min = isset($_REQUEST['price_min']) ? (int) $_REQUEST['price_min'] : "";
        if ($price_min != "") {
            $where_arr[] = "price >= $price_min";
            $request[]="price_min=$price_min";
        }

        $loc_in = array();
        if (isset($_REQUEST['loc'])) {
            $_REQUEST['loc'] = mb_strtolower($_REQUEST['loc']);
            if (preg_match('/^[а-яё -]+$/u', $_REQUEST['loc'], $loc_arr)) {
                if (count($loc_arr) != 0) {
                    $request[]="loc=".trim($loc_arr[0]);
                    $loc_in = explode(" ", trim($loc_arr[0]));
                    $loc_sql = array();
                    foreach ($loc_in as $loc) {
                        if (trim($loc) == "")
                            continue;
                        $loc_sql[] = "loc LIKE '%" . $loc . "%'";
                        $loc_sql[] = "street LIKE '%" . $loc . "%'";
                    }
                    if (count($loc_sql) > 0)
                        $where_arr[] = " ( ".implode(" OR ", $loc_sql)." ) ";
                }
            }
        }
        return (count($where_arr) ? " AND " : "") . implode(" AND ", $where_arr);
    }

    public static function buildLimit(&$from, &$cnt) {
        if (!$cnt)
            $cnt = isset($_REQUEST['cnt']) ? (int) $_REQUEST['cnt'] : 20;
        if (!$from && $from !== 0)
            $from = isset($_REQUEST['from']) ? (int) $_REQUEST['from'] : 0;

        return " LIMIT $from , $cnt ";
    }

    public static function buildOrder(&$name = "", &$how = "DESC") {
        return ($name == "") ? "" : " ORDER BY $name $how ";
    }

    /**
     * Загрузить БД
     * @param type $db
     */
    public function load($db) {
        if (!$this->db[$db]) {
            $this->db[$db] = new SQLite3($this->path_db . "$db.db");
        }
    }

    /**
     * Запрос данных
     * @param type $db
     * @param type $sql
     * @return type
     */
    public function query($db, $sql) {
        $this->load($db);

        if ($this->db[$db]) {
            return $this->db[$db]->query($sql);
        }
    }

    /**
     * Запрос данных
     * @param type $db
     * @param type $sql
     * @return type
     */
    public function querySingle($db, $sql) {
        $this->load($db);

        if ($this->db[$db]) {
            return $this->db[$db]->querySingle($sql);
        }
    }

    /**
     * Запросить данные и заполнить их
     * @param type $db
     * @param type $sql
     */
    public function prepare($db, $sql) {
        if (count($this->info) == 0) {
            $this->parse();
        }
        $mas = array();
        $result = $this->query($db, $sql);
        while ($row = $result->fetchArray()) {
            $ret = array();
            foreach ($row as $key => $value) {
                if (is_integer($key)) {
                    continue;
                }
                $ret[$key] = $this->fill($key, $value);
            }
            $mas[] = $ret;
        }
        return $mas;
    }

    /**
     * Test
     * @param type $db
     */
    public function test($db) {
        $sql = "SELECT * FROM $db";
        $result = $this->db[$db]->query($sql);
        while ($row = $result->fetchArray()) {
            print_r($row);
        }
    }

    /**
     * Прочитать все информационные XML
     */
    public function parse() {
        $this->info = array();
        $this->alias = array();
        
        $list_files = glob($this->path_xml . "*.xml");
        foreach ($list_files as $file) {
            $xml = simplexml_load_file($file);
            $xml_nm = $xml->getName();

            $xml_nodes = $xml->xpath("node");
            $nodes = array();

            foreach ($xml_nodes as $node) {
                $nodes[(string) $node['id']] = (string) $node[0];
            }
            $this->alias[$xml_nm]=(string) $xml->attributes()->alias;
            $this->info[$xml_nm] = new XmlBean((string) $xml->attributes()->alias, (string) $xml->attributes()->settings, (string) $xml->attributes()->type, $nodes); //"SimpleXMLElement",0,"",true
        }
        
        // Пользователи
        $this->agents = array();
        $xml = simplexml_load_file($this->path_usr);
        $xml_nm = $xml->getName();

        $xml_nodes = $xml->xpath("node");
        $nodes = array();

        foreach ($xml_nodes as $node) {
            $this->agents[(string) $node['idr']] = array("name"=>(string) $node[0], "contact"=>(string) $node['contact']);
        }
    }

    /**
     * Получить информационную XML
     * @param type $key
     * @return type
     */
    public function getInfo($key) {
        return $this->info[$key];
    }

    /**
     * Заполнить значение из информационной XML
     * @param type $key
     * @param type $value
     * @return type
     */
    public function fill($key, $value) {
        if (!$this->getInfo($key))
            return $value;

        if (in_array($key, $this->col_prepare))
            return $value;
        if ($this->getInfo($key)->type==1) {
            return ($value==0)?"Есть":"Нет";
        }
        $val = $this->getInfo($key)->getNodeById($value);
        return ($val == "") ? $value : $val;
    }

}

?>
