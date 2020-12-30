package com.lionscribe.open.libphonenumber;

import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberToTimeZonesMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;

public class PhoneLookup
{

    public static String getPhoneInfo(int countryCode, String phonenumber, Locale locale)
    {
        // We do not have to pass Context as we already call PhoneNumberUtil.init(context) in the Application class
        // Alternately, you can use PhoneNumberUtil.getInstance(context) and PhoneNumberOfflineGeocoder.getInstance(context)
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
        PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

        String text;
        try
        {
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phonenumber, phoneNumberUtil.getRegionCodeForCountryCode(countryCode));

            text = "Number: " + phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL) +
                    "\nCarrier: " + carrierMapper.getNameForNumber(parsedNumber, Locale.ENGLISH) +
                    "\nLocation: " + geocoder.getDescriptionForNumber(parsedNumber, locale) +
                    "\nTimeZone1: " + PhoneNumberToTimeZonesMapper.getInstance().getTimeZonesForGeographicalNumber(parsedNumber).get(0); // There can be multiple TimeZones. We use the first.

        } catch (Exception e) {
            text = "Error: " + e;
        }
        return text;
    }

}
