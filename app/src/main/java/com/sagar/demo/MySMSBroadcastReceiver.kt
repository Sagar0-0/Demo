package com.sagar.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class MySMSBroadcastReceiver : BroadcastReceiver() {

    private var listener: Listener? = null

    fun initListener(listener: Listener) {
        this.listener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action==SmsRetriever.SMS_RETRIEVED_ACTION){
            val extras = intent.extras
            val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
            when(smsRetrieverStatus.statusCode){
                CommonStatusCodes.SUCCESS->{
                    val sms = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
                    val otp = parseOtp(sms)
                    listener?.onOtpReceived(otp)
                }
                else -> {}
            }
        }
    }

    private fun parseOtp(sms: String?): String? {
        return sms?.substring(0,6)
    }

    interface Listener {
        fun onOtpReceived(value: String?)
    }
}