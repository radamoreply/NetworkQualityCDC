package com.example.android.networkqualitycdc.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.android.networkqualitycdc.myapplication.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CardElement extends FrameLayout {

    private TextView accountHolder;
    private TextView accountNumber;
    private TextView cardBalance;
    private TextView cardIban;


    public CardElement(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CardElement(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_element_layout,this);
        accountHolder = view.findViewById(R.id.card_holder);
        accountNumber = view.findViewById(R.id.card_account_number);
        cardBalance = view.findViewById(R.id.card_balance);
        cardIban = view.findViewById(R.id.card_iban);
    }

    public CardElement(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }



    public void setCard(Card card){
        if(card != null){
            accountNumber.setText(card.getAccountNumber());
            accountHolder.setText(card.getAccountHolder());
            cardBalance.setText(card.getCardBalance());
            cardIban.setText(card.getCardIban());
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
