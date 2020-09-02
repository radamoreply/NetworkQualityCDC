package com.example.android.networkqualitycdc.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class CardsAdapter extends PagerAdapter {

    private List<Card> cardsList;
    private Context context;


    public CardsAdapter(List<Card> cardsList, Context context) {
        this.cardsList = cardsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cardsList.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((CardElement) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        CardElement element = new CardElement(context);
        element.setCard(cardsList.get(position));
        container.addView(element);
        return element;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
