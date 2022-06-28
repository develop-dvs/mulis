jQuery(document).ready(function(){
var loadedSettings = readCookie("dvs-atlas");
var loadedSettingsArray = [];
if (loadedSettings!==null) {
    var loadedSettingsArray = loadedSettings.split(',');
    //var loadedSettingsArray = loadedSettings.split(',');
    jQuery("input[type='checkbox']").each(function () {
        if (inArray(this.value,loadedSettingsArray)) {
            this.setAttribute("checked","checked");
        }
    });
    updateCount(loadedSettingsArray);
} else {
    loadedSettings = "";
}

jQuery("input[type='checkbox']").click( function() {
    if (jQuery(this).prop("checked")) {
        if (!inArray(this.value,loadedSettingsArray)) {
            loadedSettingsArray.push(this.value); 
        }
    } else {
        delFromArray(this.value,loadedSettingsArray);
    }
    
    updateCount(loadedSettingsArray);
    createCookie("dvs-atlas",loadedSettingsArray);
});

jQuery("#dvs-alink").click( function() {
    loadedSettingsArray=[""];
    jQuery("input[type='checkbox']").each(function () {
        jQuery(this).attr('checked', false);
    });
    updateCount(loadedSettingsArray);
    createCookie("dvs-atlas",loadedSettingsArray);
});

function updateCount(arr) {
    var len = (arr.length-1<0)?0:arr.length-1;
    jQuery("#num-advert").html(len);
}

function inArray(val,arr) {
    for (var i=0; i<arr.length; i++) {
        if (arr[i]===val) return true;
    } return false;
}

function delFromArray(val,arr) {
    for (var i=0; i<arr.length; i++) {
        if (arr[i]===val) {
            arr.splice(i,1);
            return arr;
        }
    }
}

//function getCheckboxSettings() {
//    return jQuery(':checkbox:checked').map(function () {
//        return this.value;
//    }).get().join();
//}

function createCookie(name, value, days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
    } else var expires = "";
    document.cookie = escape(name) + "=" + escape(value) + expires + "; path=/";
}

function readCookie(name) {
    var nameEQ = escape(name) + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return unescape(c.substring(nameEQ.length, c.length));
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name, "", -1);
}

});