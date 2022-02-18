package gst.trainingcourse.persomrealmmanagement.model


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Person (
    @PrimaryKey
    var id: Int? = null,
    var name: String? = null,
    var address: String? = null

): RealmObject()
