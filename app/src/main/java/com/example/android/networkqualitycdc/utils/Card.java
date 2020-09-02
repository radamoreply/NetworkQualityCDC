package com.example.android.networkqualitycdc.utils;

public class Card {

    private String accountNumber;
    private String accountHolder;
    private String cardBalance;
    private String cardIban;

    public Card(String accountNumber, String accountHolder, String cardBalance, String cardIban) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.cardBalance = cardBalance;
        this.cardIban = cardIban;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(String cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String getCardIban() {
        return cardIban;
    }

    public void setCardIban(String cardIban) {
        this.cardIban = cardIban;
    }
}
