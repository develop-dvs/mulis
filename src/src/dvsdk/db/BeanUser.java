package dvsdk.db;

import dvsdk.util.UtilSec;

public class BeanUser {

    private String name="";
    // TODO: Баланс пользователей
    private int balance=0;
    private String idr="";
    private String pwdMD5="";
    private String contact="";
    private boolean uploader=true;

    public BeanUser() {
    }

    public BeanUser(String name, int balance, String idr) {
        this.name = name;
        this.balance = balance;
        this.idr = idr;
    }

    public BeanUser(String name, String idr, String contact, String pwdMD5) {
        this.name = name;
        //this.balance = balance;
        this.idr = idr;
        this.pwdMD5 = pwdMD5;
        this.contact = contact;
        //this.pwdMD5 = Util.getMD5(pwdMD5);
    }
    
    public BeanUser(String name, String idr, String contact, String pwdMD5, String uploader) {
        this(name, idr, contact, pwdMD5);
        this.uploader= Boolean.parseBoolean(uploader);
    }
    
    public BeanUser(String name, String idr, String contact) {
        this.name = name;
        //this.balance = balance;
        this.idr = idr;
        //this.uploader = uploader;
        this.contact = contact;
        //this.pwdMD5 = Util.getMD5(pwdMD5);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getIdr() {
        return idr;
    }

    public void setIdr(String idr) {
        this.idr = idr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwdMD5() {
        return pwdMD5;
    }

    public void setPwdMD5(String pwdMD5) {
        this.pwdMD5 = pwdMD5;
    }

    public void setPWD(String str) {
        this.pwdMD5 = UtilSec.getMD5(str);
    }

    public String getContact() {
        return contact;
    }
    
    public String getInfo() {
        return name+" "+contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean checkPWD(String pwd) {
        return (UtilSec.getMD5(pwd).equals(pwdMD5)) ? true : false;
    }

    public boolean isUploader() {
        return uploader;
    }

    public void setUploader(boolean uploader) {
        this.uploader = uploader;
    }

    public String setStdParam(String name, String value, boolean set) {
        if (value.isEmpty() || name == null) {
            return "";
        }

        if (name.equals("name")) {
            if (set) {
                return getName();
            } else {
                setName(value);
            }
        } else if (name.equals("idr")) {
            if (set) {
                return getIdr();
            } else {
                setIdr(value);
            }
        } else if (name.equals("pwd")) {
            if (set) {
                return getPwdMD5();
            } else {
                setPWD(value);
            }
        } else if (name.equals("contact")) {
            if (set) {
                return getContact();
            } else {
                setContact(value);
            }
        }
        return "";
    }
}