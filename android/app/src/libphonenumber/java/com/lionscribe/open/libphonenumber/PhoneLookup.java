package com.lionscribe.open.libphonenumber;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

public class PhoneLookup
{

    public static String getPhoneInfo(int countryCode, String phonenumber, Locale locale)
    {
        // We do not have to pass Context as we already call PhoneNumberUtil.init(context) in the Application class
        // Alternately, you can use PhoneNumberUtil.getInstance(context)
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String text;
        try
        {
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phonenumber, phoneNumberUtil.getRegionCodeForCountryCode(countryCode));

            text = "Number: " + phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL) +
					"\nCarrier: " + "GeoCoder not included!" +
                    "\nLocation: " + "GeoCoder not included!" +
                    "\nTimeZone1: " + "GeoCoder not included!";

        } catch (Exception e) {
            text = "Error: " + e;
        }
        return text;
    }

}
