package com.tutosandroidfrance.wearsample

import com.github.florent37.EmmetWearableListenerService
import com.github.florent37.davinci.daemon.DaVinciDaemon
import com.google.android.gms.wearable.MessageEvent
import com.tutosandroidfrance.wearprotocol.AndroidVersion
import com.tutosandroidfrance.wearprotocol.SmartphoneProtocol
import com.tutosandroidfrance.wearprotocol.WearProtocol
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

import java.util.List

import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response

@CompileStatic
public class WearService extends EmmetWearableListenerService implements SmartphoneProtocol {

    WearProtocol wearProtocol

    @Override
    public void onCreate() {
        super.onCreate()

        //initialized the data service
        getEmmet().registerReceiver(SmartphoneProtocol.class, this)

        //initializes sends data to the watch
        wearProtocol = getEmmet().createSender(WearProtocol.class)
    }

    //when the watch sends a hello message to the  smartphone
    @Override
    public void hello() {
        //Retrofit used to make a REST call
        /*AndroidService androidService = new RestAdapter.Builder()
                .setEndpoint(AndroidService.ENDPOINT)
                .build().create(AndroidService.class)*/
        def slurper = new JsonSlurper()

        def jokesList = new ArrayList()
        4.times {
            def randomJoke = slurper.parse(new URL("http://api.icndb.com/jokes/random"))
            jokesList.add(randomJoke.getAt("value"))
        }


        wearProtocol.onAndroidVersionsReceived(jokesList)
        //Retrieves and deserialise the contents of my JSON file into an object List<AndroidVersion>

        // let's groovify this!

        /*androidService.getElements(new Callback<List<AndroidVersion>>() {
            @Override
            public void success(List<AndroidVersion> androidVersions, Response response) {

                //envoie cette liste à la montre
                wearProtocol.onAndroidVersionsReceived(androidVersions)
            }

            @Override
            public void failure(RetrofitError error) {
            }
        })*/


    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent)

        //allows DaVinciDaemon listen to messages@
        DaVinciDaemon.with(getApplicationContext()).handleMessage(messageEvent)
    }
}
