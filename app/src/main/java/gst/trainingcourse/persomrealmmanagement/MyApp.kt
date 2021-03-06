package gst.trainingcourse.persomrealmmanagement

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()

        //init Realm
        Realm.init(this)

        val configuration = RealmConfiguration.Builder()
            .name("Person.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()

        Realm.setDefaultConfiguration(configuration)
    }
}