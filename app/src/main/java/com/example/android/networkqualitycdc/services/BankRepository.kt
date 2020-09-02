package services

import com.example.android.networkqualitycdc.utils.Card


interface BankRepository{
    fun getCardInformation(id:Int) : Card
    fun makeBonifico(iban:String,intestatario:String,importo:String)
    fun makeLogin(userName:String,password:String):Boolean
}