package com.myshopkirana.utils;

import android.app.Activity;

public class AnalyticDataClass {
    public static AnalyticDataClass AnalyticDataClass;
    private static Activity mActivity;

    public static AnalyticDataClass getInstance(Activity activity) {
        if (AnalyticDataClass == null) {
            AnalyticDataClass = new AnalyticDataClass();
        }
        mActivity = activity;
        return AnalyticDataClass;

    }

//    public void updateAnalyticView(MixpanelAPI mMixpanel) {
//
//        final long nowInHours = Utility.hoursSinceEpoch();
//        final int hourOfTheDay = Utility.hourOfTheDay();
//
//        try {
//            final JSONObject properties = new JSONObject();
//            properties.put("first viewed on", nowInHours);
//            properties.put("user domain", "(unknown)"); // default value
//            mMixpanel.registerSuperPropertiesOnce(properties);
//        } catch (final JSONException e) {
//            throw new RuntimeException("Could not encode hour first viewed as JSON");
//        }
//        try {
//            final JSONObject properties = new JSONObject();
//            properties.put("hour of the day", hourOfTheDay);
//            mMixpanel.track("App Resumed", properties);
//        } catch (final JSONException e) {
//            throw new RuntimeException("Could not encode hour of the day in JSON");
//        }
//
//        mMixpanel.getPeople().showNotificationIfAvailable(mActivity);
//    }
//
//    public void sendToMixpanel(MixpanelAPI mMixpanel, String firstName, String lastName, String email, String Tittle) {
//
//        final MixpanelAPI.People people = mMixpanel.getPeople();
//        people.set("$first_name", firstName);
//        people.set("$last_name", lastName);
//        people.set("$email", email);
//
//        people.increment("Update Count", 1L);
//        try {
//            final JSONObject domainProperty = new JSONObject();
//            domainProperty.put("user domain", Utility.domainFromEmailAddress(email));
//            mMixpanel.registerSuperProperties(domainProperty);
//        } catch (final JSONException e) {
//            throw new RuntimeException("Cannot write user email address domain as a super property");
//        }
//
//        mMixpanel.track(Tittle, null);
//    }
}