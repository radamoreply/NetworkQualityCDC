package services

import android.content.Context
import android.content.res.AssetManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.android.networkqualitycdc.utils.Card
import com.example.android.networkqualitycdc.utils.ServiceUtil
import com.google.gson.Gson

class BankRepositoryDefault(context:Context):BankRepository{



    private var gson: Gson ?= null
    private var assets: AssetManager ?= null

    init {
        gson = Gson()
        assets = context.assets
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getCardInformation(id: Int): Card {

        val filePath = when(id){
            1 -> "mockFiles/get-card-1.json"
            2 -> "mockFiles/get-card-2.json"
            3 -> "mockFiles/get-card-3.json"
            else -> "mockFiles/get-card-4.json"
        }

        return ServiceUtil.loadJson(filePath,Card::class.java,assets,gson)
    }

    override fun makeBonifico(iban:String,intestatario:String,importo:String) {
        if(iban.isEmpty() || intestatario.isEmpty() || importo.isEmpty()){
            throw Exception("Campi vuoti")
        }
    }

    override fun makeLogin(userName:String,password:String):Boolean {
        if(userName.isEmpty() || password.isEmpty()){
            throw Exception("Campi vuoti")
        }

        if(userName != "DemoUser" || password != "pwd"){
            throw Exception("Credenziali non corrette")
        }

        return true
    }

}