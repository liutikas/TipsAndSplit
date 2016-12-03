package net.liutikas.tipandsplit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import net.liutikas.widget.StepperWidget;

public class MainActivity extends AppCompatActivity {
    private static final String PERCENT_SELECTED = "percent_selected";
    private static final int PERCENT_DEFAULT = 10;

    private RadioGroup mPercentGroup;
    private EditText mBillEditText;
    private TextView mTipTextView;
    private TextView mTotalTextView;
    private TextView mPerPerson;
    private EditText mCustomTip;
    private StepperWidget mStepperWidget;

    private boolean mCustomTipSelected;
    private int mPercent = PERCENT_DEFAULT;
    private int mSplit = 1;
    private float mBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPercentGroup = (RadioGroup) findViewById(R.id.perc_radio);
        mPercentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mCustomTipSelected = false;
                switch (checkedId) {
                    case R.id.perc10:
                        mPercent = 10;
                        break;
                    case R.id.perc15:
                        mPercent = 15;
                        break;
                    default:
                        mCustomTipSelected = true;
                        updateCustomTip();
                        break;
                }
                updatePayInfo();
            }
        });
        mBillEditText = (EditText) findViewById(R.id.bill);
        mTipTextView = (TextView) findViewById(R.id.tip);
        mTotalTextView = (TextView) findViewById(R.id.total);
        mPerPerson = (TextView) findViewById(R.id.per_person);
        mCustomTip = (EditText) findViewById(R.id.custom_tip);
        mStepperWidget = (StepperWidget) findViewById(R.id.stepper);
        mStepperWidget.setMin(1);
        mStepperWidget.setMax(10);
        mStepperWidget.addOnUpdateListener(new StepperWidget.OnUpdateListener() {
            @Override
            public void onUpdate(int oldValue, int newValue) {
                mSplit = newValue;
                updatePayInfo();
            }
        });

        mBillEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updatePayInfo();
            }
        });

        mCustomTip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (mCustomTipSelected) {
                    updateCustomTip();
                    updatePayInfo();
                }
            }
        });

        mBillEditText.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        int percent = sharedPreferences.getInt(PERCENT_SELECTED, PERCENT_DEFAULT);
        switch (percent) {
            case 10:
                mPercentGroup.check(R.id.perc10);
                break;
            case 15:
                mPercentGroup.check(R.id.perc15);
                break;
            default:
                mPercentGroup.check(R.id.perc_custom);
                mCustomTip.setText(Integer.toString(percent));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PERCENT_SELECTED, mPercent);
        editor.commit();
    }

    private void updateCustomTip() {
        final String customTip = mCustomTip.getText().toString();
        if (TextUtils.isEmpty(customTip)) {
            mPercent = 20;
        } else {
            mPercent = Integer.parseInt(customTip);
        }
    }

    private void updatePayInfo() {
        final String bill = mBillEditText.getText().toString();
        if (TextUtils.isEmpty(bill)) {
            mBill = 0;
        } else {
            mBill = Float.parseFloat(bill);
        }
        final float tip = mBill / 100f * mPercent;
        mTipTextView.setText(String.format("Tip: %.2f", tip));
        mTotalTextView.setText(String.format("Total: %.2f", (mBill + tip)));
        if (mSplit > 1) {
            mPerPerson.setVisibility(View.VISIBLE);
            mPerPerson.setText(String.format("Per person: %.2f", (mBill + tip) / (mSplit * 1f)));
        } else {
            mPerPerson.setVisibility(View.INVISIBLE);
        }
    }
}
