package com.example.contactutils.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.contactutils.R
import com.example.contactutils.db.ContactDatabase
import com.example.contactutils.ui.DialogCheckActivity
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit


private const val NOTIF_ID = 1337
public const val CHANNEL_WHATEVER = "whatever"

class Callreceiver : PhonecallReceiver()
{


    override fun onIncomingCallStarted(ctx: Context?, number: String?, start: Date?) {
      // Toast.makeText(ctx, "number-->$number", Toast.LENGTH_LONG).show()


        ctx.let {ctxs ->
            number?.let {
                callDialog(ctxs,it)
            }
        }

    }

    override fun onOutgoingCallStarted(ctx: Context?, number: String?, start: Date?) {
        TODO("Not yet implemented")
    }


    override fun onIncomingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {
        //Toast.makeText(ctx, "number-->$number", Toast.LENGTH_LONG).show()
        //Log.e("TAG", "--->onIncomingCallEnded")
        number?.let {
            callDialog(ctx,it)

        }

    }

    override fun onOutgoingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {
        TODO("Not yet implemented")
    }

    override fun onMissedCall(ctx: Context?, number: String?, start: Date?) {
        //Toast.makeText(ctx, "number-->$number", Toast.LENGTH_LONG).show()
       // Log.e("TAG", "--->onMissedCallonMissedCall")

        number?.let {
            callDialog(ctx,it)

        }

    }


    @SuppressLint("RestrictedApi")
    fun callDialog(ctx: Context?, number: String){

        ctx?.let { context ->

            val database = ContactDatabase.getInstance(context).contactListDao


            GlobalScope.launch (Dispatchers.IO){


                database.getCheckedNumber(true).let {

                    Log.e("dabase-->","-->${it.size}")


                    if(it.size>0) {

                        delay(2000L)


                        Log.e("Version","-->${Build.VERSION.SDK_INT}")


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                            val i = Intent(context, DialogCheckActivity::class.java)
                            i.putExtra("model",it.get(0))
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                            val pi = PendingIntent.getActivity(
                                    context,
                                    0,
                                    i,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            )

                            val builder = NotificationCompat.Builder(context, CHANNEL_WHATEVER)
                                    .setSmallIcon(R.drawable.ic_baseline_person_24)
                                    .setContentTitle("Call")
                                    .setContentText(it.get(0).ContactName +"\n"+number)
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    //.setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_CALL)
                                    .setFullScreenIntent(pi, true)

                            val mgr = context.getSystemService(NotificationManager::class.java)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                                    && mgr.getNotificationChannel(CHANNEL_WHATEVER) == null
                            ) {
                                mgr.createNotificationChannel(
                                        NotificationChannel(
                                                CHANNEL_WHATEVER,
                                                "Whatever",
                                                NotificationManager.IMPORTANCE_HIGH
                                        )
                                )
                            }

                            mgr.notify(123, builder.build())




                        }else{
                            val i = Intent(context, DialogCheckActivity::class.java)
                            i.putExtra("model",it.get(0))
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            // i.flags = FLAG_ACTIVITY_SINGLE_TOP
                            context.startActivity(i)
                        }



                      /* val pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)

                        try{
                        pi.send(context, 0, i);

                    } catch (e : PendingIntent.CanceledException) {
                        // the stack trace isn't very helpful here.  Just log the exception message.
                        System.out.println( "Sending contentIntent failed: " );
                    }*/


                        // }


                    }






                }
            }






        }


    }


}