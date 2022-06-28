<?php
class XmlBean {
    //var $name = "";
    var $alias = "";
    var $settings = "";
    var $type = "";
    var $nodes = "";
    
    function __construct($alias, $settings, $type, $nodes) {
        $this->alias=$alias;
        $this->settings=$settings;
        $this->type=$type;
        $this->nodes=$nodes;
    }
    
    function getNodeById($id) {
        return isset($this->nodes[$id])?$this->nodes[$id]:"";
    }

}

?>
