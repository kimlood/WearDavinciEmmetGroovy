package com.tutosandroidfrance.wearsample

import com.github.florent37.EmmetWearableListenerService
import com.github.florent37.davinci.daemon.DaVinciDaemon
import com.google.android.gms.wearable.MessageEvent
import com.tutosandroidfrance.wearprotocol.SmartphoneProtocol
import com.tutosandroidfrance.wearprotocol.WearProtocol
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic



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

        def slurper = new JsonSlurper()

        def jokesList = new ArrayList()
        6.times {
            def randomJoke = slurper.parse(new URL("http://api.icndb.com/jokes/random?exclude=[explicit]"))
            jokesList.add(randomJoke.getAt("value"))
        }


        wearProtocol.onAndroidVersionsReceived(jokesList)


    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent)

        //allows DaVinciDaemon listen to messages@
        DaVinciDaemon.with(getApplicationContext()).handleMessage(messageEvent)
    }
}
