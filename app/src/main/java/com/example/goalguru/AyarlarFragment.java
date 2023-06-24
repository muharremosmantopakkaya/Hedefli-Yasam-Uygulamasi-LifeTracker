package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class AyarlarFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        Preference geriBildirimPreference = findPreference("feedback_signature");
        geriBildirimPreference.setOnPreferenceClickListener(this);

        Preference odemePlanıPreference = findPreference("payment_plan");
        odemePlanıPreference.setOnPreferenceClickListener(this);

        Preference SSSPreference = findPreference("feedback_signatures");
        SSSPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals("feedback_signature")) {
            openFeedbackActivity();
            return true;
        } else if (key.equals("payment_plan")) {
            openExpandableListActivity();
            return true;
        }
        else if(key.equals("feedback_signatures")) {
            openFaq();
            return true;

        }
        return false;
    }

    private void openFaq() {
        Intent intent = new Intent(requireContext(), Faq.class);
        startActivity(intent);
    }

    private void openFeedbackActivity() {
        Intent intent = new Intent(requireContext(), FeedbackActivity.class);
        startActivity(intent);
    }

    private void openExpandableListActivity() {
        Intent intent = new Intent(requireContext(), ExpandableList.class);
        startActivity(intent);
    }
}
