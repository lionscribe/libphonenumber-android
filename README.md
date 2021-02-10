
<p align="right">
<img src="https://travis-ci.org/google/libphonenumber.svg?branch=master">
</p>

# What is it?

An Android port of Google's phonelibnumber library for parsing, formatting, and
validating international phone numbers. It also can include offline geocode lookup.
The Android version is optimized by using Assets, which are much quicker than ResourceStreams.
It also includes a demo app.

# Highlights of functionality

*   Parsing, formatting, and validating phone numbers for all countries/regions
    of the world.
*   `getNumberType` - gets the type of the number based on the number itself;
    able to distinguish Fixed-line, Mobile, Toll-free, Premium Rate, Shared
    Cost, VoIP, Personal Numbers, UAN, Pager, and Voicemail (whenever feasible).
*   `isNumberMatch` - gets a confidence level on whether two numbers could be
    the same.
*   `getExampleNumber` and `getExampleNumberForType` - provide valid example
    numbers for all countries/regions, with the option of specifying which type
    of example phone number is needed.
*   `isPossibleNumber` - quickly guesses whether a number is a possible
    phone number by using only the length information, much faster than a full
    validation.
*   `isValidNumber` - full validation of a phone number for a region using
    length and prefix information.
*   `AsYouTypeFormatter` - formats phone numbers on-the-fly when users enter
    each digit.
*   `findNumbers` - finds numbers in text.
*   `PhoneNumberOfflineGeocoder` - provides geographical information related to
    a phone number.
*   `PhoneNumberToCarrierMapper` - provides carrier information related to a
    phone number.
*   `PhoneNumberToTimeZonesMapper` - provides timezone information related to a
    phone number.


# Before you start

 1.  Add the following to the dependencies section of your module's build.gradle file;
     - If you only need phonelibnumber add;
     `implementation 'com.lionscribe.open.libphonenumber:libphonenumber:8.12.18.1'`
     - If you need phonelibnumber and offline-geocoder add; 
     `implementation 'com.lionscribe.open.libphonenumber:geocoder:8.12.18.1'`
     - Do NOT add both!

 2.  In your code, you should call `PhoneNumberUtil.init(context)`, before any call to `getInstance()`, preferably in the Application class. Alternately, you can use the `getInstance(context)` method.


# Quick Examples

Let's say you have a string representing a phone number from Switzerland. This
is how you parse/normalize it into a `PhoneNumber` object:

```java
String swissNumberStr = "044 668 18 00";
PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
try {
  PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, "CH");
} catch (NumberParseException e) {
  System.err.println("NumberParseException was thrown: " + e.toString());
}
```

At this point, `swissNumberProto` contains:

```json
{
  "country_code": 41,
  "national_number": 446681800
}
```

`PhoneNumber` is a class that was originally auto-generated from
`phonenumber.proto` with necessary modifications for efficiency. For details on
the meaning of each field, refer to `resources/phonenumber.proto`.

Now let us validate whether the number is valid:

```java
boolean isValid = phoneUtil.isValidNumber(swissNumberProto); // returns true
```

There are a few formats supported by the formatting method, as illustrated
below:

```java
// Produces "+41 44 668 18 00"
System.out.println(phoneUtil.format(swissNumberProto, PhoneNumberFormat.INTERNATIONAL));
// Produces "044 668 18 00"
System.out.println(phoneUtil.format(swissNumberProto, PhoneNumberFormat.NATIONAL));
// Produces "+41446681800"
System.out.println(phoneUtil.format(swissNumberProto, PhoneNumberFormat.E164));
```

You could also choose to format the number in the way it is dialed from another
country:

```java
// Produces "011 41 44 668 1800", the number when it is dialed in the United States.
System.out.println(phoneUtil.formatOutOfCountryCallingNumber(swissNumberProto, "US"));
```

## Formatting Phone Numbers 'as you type'

```java
PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
AsYouTypeFormatter formatter = phoneUtil.getAsYouTypeFormatter("US");
System.out.println(formatter.inputDigit('6'));  // Outputs "6"
...  // Input more digits
System.out.println(formatter.inputDigit('3'));  // Now outputs "650 253"
```

## Geocoding Phone Numbers

```java
PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
// Outputs "Zurich"
System.out.println(geocoder.getDescriptionForNumber(swissNumberProto, Locale.ENGLISH));
// Outputs "Zürich"
System.out.println(geocoder.getDescriptionForNumber(swissNumberProto, Locale.GERMAN));
// Outputs "Zurigo"
System.out.println(geocoder.getDescriptionForNumber(swissNumberProto, Locale.ITALIAN));
```

## Mapping Phone Numbers to original carriers

Caveat: We do not provide data about the current carrier of a phone number, only
the original carrier who is assigned the corresponding range. Read about [number
portability](FAQ.md#what-is-mobile-number-portability).

```java
PhoneNumber swissMobileNumber =
    new PhoneNumber().setCountryCode(41).setNationalNumber(798765432L);
PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();
// Outputs "Swisscom"
System.out.println(carrierMapper.getNameForNumber(swissMobileNumber, Locale.ENGLISH));
```

More examples on how to use the library can be found in the [unit
tests](https://github.com/google/libphonenumber/tree/master/java/libphonenumber/test/com/google/i18n/phonenumbers).

# Third-party Ports

    We know that there is already an Android port at
    https://github.com/MichaelRocks/libphonenumber-android, which does repackage
    the metadata and use `AssetManager#open()`, but there are deficiencies in 
	that port. First, it does not include an option for the GeoCoder and
	is not compatible with the Google GeoCoder. Additionally, the developer chose
	not to institute the Singleton instance method, and relies on the user to institute
	it, which can lead to complications. We chose to include the Singleton instance, 
	and we rely on developer to init it with the context.

