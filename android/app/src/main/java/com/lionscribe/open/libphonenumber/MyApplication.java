package com.lionscribe.open.libphonenumber;

import android.app.Application;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        PhoneNumberUtil.init(this);
    }
}
