package com.example.android.networkqualitycdc.myapplication

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.android.networkqualitycdc.myapplication.BonificoActivity
import com.example.android.networkqualitycdc.myapplication.LoginActivity.KEY_CONNECTION_SPEED
import com.example.android.networkqualitycdc.myapplication.R
import com.example.android.networkqualitycdc.utils.Card
import com.example.android.networkqualitycdc.utils.CardsAdapter
import services.BankRepositoryDefault



class MainActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun createOpenIntent(
            context: Context,
            connectionSpeed: ChooseConnectivityCheckActivity.SPEED
        ): Intent {
            val i = Intent(context, MainActivity::class.java)
            i.putExtra(KEY_CONNECTION_SPEED, connectionSpeed)
            return i
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bankRepository = BankRepositoryDefault(this)
        val speed : ChooseConnectivityCheckActivity.SPEED = intent.getSerializableExtra(KEY_CONNECTION_SPEED) as ChooseConnectivityCheckActivity.SPEED
        val list =  ArrayList<Card>()
        if(speed.equals(ChooseConnectivityCheckActivity.SPEED.EXCELLENT) ||
            speed.equals(ChooseConnectivityCheckActivity.SPEED.GOOD)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                list.add(bankRepository.getCardInformation(1))
                list.add(bankRepository.getCardInformation(2))
                list.add(bankRepository.getCardInformation(3))
                list.add(bankRepository.getCardInformation(4))
        }}else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    list.add(bankRepository.getCardInformation(5))
                    list.add(bankRepository.getCardInformation(6))
                    list.add(bankRepository.getCardInformation(7))
                    list.add(bankRepository.getCardInformation(8))
                }
        }

        val cardAdapter = CardsAdapter(list,this)
        val viewPager = findViewById<ViewPager>(R.id.card_view_pager)

        findViewById<View>(R.id.bonifico_button).setOnClickListener {
            startActivity(BonificoActivity.openBonificoIntent(this))
        }

        supportActionBar?.title = "Home page"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        viewPager.adapter = cardAdapter

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}
