package com.lionscribe.open.libphonenumber;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    TextView _tv;
    Spinner _spinner;
    EditText _editText;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _tv = findViewById(R.id.results);

        ArrayList<String> regions = new ArrayList<>();
        // Really no need to pass context to getInstance(), as we already call PhoneNumberUtil.init(context) in the Application class
        ArrayList<Integer> codes = new ArrayList<>(PhoneNumberUtil.getInstance(this).getSupportedCallingCodes());
        Collections.sort(codes);
        for (Integer i : codes)
            regions.add(Integer.toString(i));

        _spinner = findViewById(R.id.country_code_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.country_code_spinner_item, android.R.id.text1, regions);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.country_code_spinner_item);
        _spinner.setAdapter(spinnerArrayAdapter);

        _editText = findViewById(R.id.number_edit);
        _editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        ||  (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
                    doUpdate();
                    return true;
                }
                return false;
            }
        });

        ImageButton button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doUpdate();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void doUpdate()
    {
        try
        {
            final int parsedCountryCode = Integer.parseInt(_spinner.getSelectedItem().toString());
            final Locale locale = getResources().getConfiguration().locale;
            final String phonenumber = _editText.getText().toString();
            _tv.setText(PhoneLookup.getPhoneInfo(parsedCountryCode, phonenumber, locale));

        } catch (Exception e) {
            _tv.setText("Error: " + e);
        }
    }
}