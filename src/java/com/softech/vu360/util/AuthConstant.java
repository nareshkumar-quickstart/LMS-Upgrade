package com.softech.vu360.util;

public interface AuthConstant {
    int USER_CONTROL_NORMAL_USER = 512;
    int UF_ACCOUNTDISABLE = 0x0002;
    int UF_PASSWD_NOTREQD = 0x0020;
    int UF_PASSWD_CANT_CHANGE = 0x0040;
    int UF_NORMAL_ACCOUNT = 0x0200;
    int UF_DONT_EXPIRE_PASSWD = 0x10000;
    int UF_PASSWORD_EXPIRED = 0x800000;
    
    String AD_FIELD_CN = "cn";
    String AD_FIELD_PASSWORD = "unicodePwd";
    String AD_FIELD_USERACCOUNTCONTROL = "userAccountControl";
    String AD_FIELD_SAMUSERACCOUNTNAME = "samAccountName";
    String AD_FIELD_FIRSTNAME = "givenName";
    String AD_FIELD_LASTNAME = "sn";
    String AD_FIELD_DISPLAYNAME = "displayName";
    String AD_FIELD_USERPRINCIPALNAME = "userPrincipalName";    
    String AD_FIELD_OBJECTCLASS = "objectClass";
    String AD_FIELD_EMAIL = "mail";
}