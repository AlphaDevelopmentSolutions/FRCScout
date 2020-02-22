package com.alphadevelopmentsolutions.frcscout.interfaces

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

interface IntentAction {
    companion object {

        /**
         * Fires an email intent
         * @param context [Context] context to create and fire intent
         * @param email [String] email address to email to
         * @param subject [String] subject of the email
         * @param startingBody [String] starting body of the email
         */
        fun email(context: Context, email: String, subject: String? = null, startingBody: String? = null) {
            context.startActivity(
                    Intent(
                            Intent.ACTION_SENDTO,
                            Uri.parse("mailto:$email")
                    ).apply {
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                        putExtra(Intent.EXTRA_SUBJECT, subject ?: "Information Regarding an Estimate")
                        putExtra(Intent.EXTRA_TEXT, startingBody ?: "Good Afternoon,\n\n")
                    }
            )
        }

        /**
         * Fires an sms intent
         * @param context [Context] context to create and fire intent
         * @param phone [String] phone number to sms
         * @param startingBody [String] starting body of the sms
         */
        fun sms(context: Context, phone: String, startingBody: String? = null) {
            context.startActivity(
                    Intent(
                            Intent.ACTION_SENDTO,
                            Uri.parse("smsto:$phone")
                    ).apply {
                        putExtra("sms_body", startingBody ?: "Good Afternoon,\n\n")
                    }
            )
        }

        /**
         * Fires a call intent
         * @param context [Context] context to create and fire intent
         * @param phone [String] phone to call
         */
        fun call(context: Context, phone: String) {
            context.startActivity(
                    Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:$phone")
                    )
            )
        }

        /**
         * Fires a map intent
         * @param context [Context] context to create and fire intent
         * @param address [String] address to search for on system map app
         */
        fun map(context: Context, address: String) {
            context.startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                    String.format(
                                            "geo:0,0?q=%s",
                                            URLEncoder.encode(
                                                    address,
                                                    UTF_8.toString())
                                    )
                            )
                    )
            )
        }

        /**
         * Fires a website intent
         * @param context [Context] context to create and fire intent
         * @param url [String] url to open system web browser to
         */
        fun website(context: Context, url: String) {
            context.startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                    url
                            )
                    )
            )
        }
    }
}