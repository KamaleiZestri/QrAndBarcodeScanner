package com.example.barcodescanner.usecase

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.net.Socket

class ConnectionManager {
    companion object
    {
        lateinit var client: Socket
        lateinit var currAddress: String


        fun connectAndSend(address: String, message: String): Completable {
            return Completable.create{
                emitter ->
                try {
                    if(!isConnected())
                        connect(address)
                    send(message)
                    emitter.onComplete()
                } catch(e: Exception)
                {
                    Logger.log(e)
                    emitter.onError(e)
                }
            }
                .subscribeOn(Schedulers.newThread())
        }

        private fun connect(address : String)
        {
            currAddress = address
            var connectionSuccessStatus = true
            try
            {
                client = Socket(address, 24234)
            }
            catch(ce: ConnectException)
            {

                throw ConnectException("Connection Refused. Make sure server is running.");
            }
        }

        private fun send(msg: String)
        {
            try {
                client.getOutputStream().write(msg.toByteArray())
            }
            catch(upae: UninitializedPropertyAccessException)
            {
                throw UninitializedPropertyAccessException("Error Connecting to given IP Address. Make sure IP is correct.")
            }
            catch(ioe: IOException) //Occurs when broken pipe.
            {
                //close out and connect again.
                client.close()

                try
                {
                    connect(currAddress)
                }
                catch (e: Exception)
                {
                    throw e
                }

                send(msg)
            }
        }

//        fun close()
//        {
//            client.close()
//        }

        private fun isConnected() : Boolean
        {
            if(this::client.isInitialized)
                return client.isConnected
            else
                return false
        }
    }
}