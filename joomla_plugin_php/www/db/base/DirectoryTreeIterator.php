<?php
class DirectoryTreeIterator extends RecursiveIteratorIterator
{
    function __construct($path)
    {
        parent::__construct(
            new RecursiveCachingIterator(
                new RecursiveDirectoryIterator($path, RecursiveDirectoryIterator::KEY_AS_FILENAME
                ), 
                CachingIterator::CALL_TOSTRING|CachingIterator::CATCH_GET_CHILD
            ), 
            parent::SELF_FIRST
        );
    }

    function current()
    {
        $tree = '';
        for ($l=0; $l < $this->getDepth(); $l++) {
            $tree .= $this->getSubIterator($l)->hasNext() ? '' : '';
        }
        return $tree . ($this->getSubIterator($l)->hasNext() ? '' : '') 
               . $this->getSubIterator($l)->__toString();
    }

    function __call($func, $params)
    {
        return call_user_func_array(array($this->getSubIterator(), $func), $params);
    }
}
?>