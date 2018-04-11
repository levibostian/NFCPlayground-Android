package com.levibostian.nfcfun

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast

class LaunchFromNFCActivity : AppCompatActivity() {

    companion object {
        const val TAG = "NFC_FUN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(MainActivity.getIntent(this))

        parseNfcTag()

        Toast.makeText(this, "Launched app from NFC", Toast.LENGTH_LONG).show()
    }

    private fun parseNfcTag() {
        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.let { rawMessages ->
                val messages = arrayOfNulls<NdefMessage>(rawMessages.size)
                for (i in 0 until rawMessages.size) {
                    val message = rawMessages[i] as NdefMessage
                    messages[i] = message
                    Log.d(TAG, "Message: ${message.records.joinToString { it.toUri().toString() }}")
                }
            }
        }
    }

}
