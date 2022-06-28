<?php
//$_POST['dvsauth']="test";
if (!isset($_POST['dvsauth']) || $_POST['dvsauth'] != "test") {
    header('HTTP/1.0 403 Forbidden');
    exit();
}

if (count($_FILES) > 0) {
    
    define("ZIP_FILE", __DIR__ . "/arch.zip");
    move_uploaded_file($_FILES["filex"]["tmp_name"],ZIP_FILE);
    
    $zip = new ZipArchive();
    $res=$zip->open(ZIP_FILE);
    if ($res===true) {
        $zip->extractTo(__DIR__);
        $zip->close();
		@unlink(ZIP_FILE);
        require_once './postinit.php';
    }
    /*
    require_once './pclzip.lib.php';
    $zip = new PclZip(ZIP_FILE);
    if ($zip->extract(PCLZIP_OPT_PATH,__DIR__)) {
        require_once './postinit.php';
        //@unlink(ZIP_FILE);
    }*/
}

if (isset($_POST['dt'])) {
    define("IMG_DIR", str_replace("\\", "/", __DIR__) . "/db_img");
    include './DirectoryTreeIterator.php';

    // Список существующих файлов
    $it = new DirectoryTreeIterator(IMG_DIR);
    $imgFiles = array();
    foreach ($it as $path) {
        if (!is_dir($path)) {
            $std = new stdClass();
            $std->k = str_replace("\\", "/", $path);
            $std->v = md5_file($std->k);
            $imgFiles[] = $std;
        }
    }
    //print_r($imgFiles);

    // Список
    $json = json_decode(trim($_POST['dt']));
    if (count($json->images) > 0) {
        foreach ($json->images as $key => $value) {
            foreach ($imgFiles as $keyIn => $valueIn) {
                if ((endsWith($valueIn->k, $value->k)) && ($value->v == $valueIn->v)) {
                    unset($imgFiles[$keyIn]);
                    unset($json->images[$key]);
                    continue;
                }
            }
        }

        // Удаляем лишнее
        foreach ($imgFiles as $value) {
            @unlink($value->k);
        }
    }

    // Переиндексируем массив
    $json->images = array_values($json->images);
    //print_r($imgFiles);
    echo json_encode($json);
}

function startsWith($haystack, $needle) {
    return strpos($haystack, $needle) === 0;
}

function endsWith($haystack, $needle) {
    return substr($haystack, -strlen($needle)) == $needle;
}

function deleteDir($path) {
    return is_file($path) ?
            @unlink($path) :
            array_map(__FUNCTION__, glob($path . '/*')) == @rmdir($path);
}

?>