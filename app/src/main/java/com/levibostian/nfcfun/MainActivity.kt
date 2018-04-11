package com.levibostian.nfcfun

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.PendingIntent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.NfcAdapter
import android.os.PatternMatcher
import android.nfc.tech.NfcF
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.levibostian.nfcfun.util.LogUtil
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var nfcReadPendingIntent: PendingIntent
    private lateinit var intentFilters: Array<IntentFilter>
    private lateinit var techListsArray: Array<Array<String>>

    companion object {
        const val NFC_READ_REQUEST_CODE = 0

        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        startupNfcReading()
    }

    private fun startupNfcReading() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            activity_main_status_textview.text = "Your device does not support NFC."
            return
        }
        if (!nfcAdapter!!.isEnabled) {
            activity_main_status_textview.text = "Your NFC is not enabled."
            activity_main_enable_nfc_button.visibility = View.VISIBLE
            activity_main_enable_nfc_button.setOnClickListener {
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }
            return
        }

        activity_main_status_textview.text = "Warming up..."
        activity_main_enable_nfc_button.visibility = View.GONE
        nfcReadPendingIntent = PendingIntent.getActivity(
                this, NFC_READ_REQUEST_CODE, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            ndef.addCategory(Intent.CATEGORY_DEFAULT)
            ndef.addDataScheme("https")
            ndef.addDataAuthority("app.levibostian.com", null)
            ndef.addDataPath("/code", PatternMatcher.PATTERN_PREFIX)
        } catch (e: MalformedMimeTypeException) {
            throw RuntimeException("fail", e)
        }

        intentFilters = arrayOf(ndef)
        techListsArray = arrayOf(arrayOf(NfcF::class.java.name))

        nfcAdapter!!.enableForegroundDispatch(this, nfcReadPendingIntent, intentFilters, techListsArray)
        activity_main_status_textview.text = "Ready to scan NFC tag"
    }

    override fun onResume() {
        super.onResume()

        startupNfcReading()
    }

    override fun onPause() {
        super.onPause()

        nfcAdapter?.let {
            it.disableForegroundDispatch(this)
            activity_main_status_textview.text = "NFC reading turned off."
        }
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent == null) return

        Toast.makeText(this, "Read NFC tag!", Toast.LENGTH_LONG).show()
        Log.d(LogUtil.TAG, "Read new NFC tag.")
        val data = intent.data
        Log.d(LogUtil.TAG, "Tag data. Host: ${data.host}, path: ${data.path}, query: ${data.query}")
    }

}
