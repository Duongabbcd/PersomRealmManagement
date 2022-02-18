package gst.trainingcourse.persomrealmmanagement.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import gst.trainingcourse.persomrealmmanagement.databinding.ActivityAddPersonActivitiyBinding
import gst.trainingcourse.persomrealmmanagement.model.Person
import io.realm.Realm
import io.realm.kotlin.where


class AddPersonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPersonActivitiyBinding
    private lateinit var realm: Realm
    private lateinit var gettingValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPersonActivitiyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        realm = Realm.getDefaultInstance()
        gettingValue = intent.getStringExtra("id")!!
        Log.d("ABCDXYZ", "$gettingValue")


        binding.editPerson.text = ("Update")
        binding.deletePerson.text = ("Delete")

        binding.addPerson.setOnClickListener {
            addPersonToDB()
        }
        if (gettingValue.toInt() > 0) {
            binding.addPerson.isEnabled = false
            findPersonById()
            binding.editPerson.setOnClickListener {
                updatePersonById()
            }

            binding.deletePerson.setOnClickListener {
                deletePersonById()
            }
        } else if (gettingValue == "-1") {
            binding.editPerson.isEnabled = false
            binding.deletePerson.isEnabled = false
        }
    }

    private fun deletePersonById() {
        val result = realm.where<Person>().equalTo("id", gettingValue.toInt()).findAll()
        result.forEach { person ->
            realm.beginTransaction()
            person.deleteFromRealm()
            realm.commitTransaction()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updatePersonById() {
        realm.executeTransactionAsync(Realm.Transaction { bgRealm ->
            val person = bgRealm.where<Person>().equalTo("id", gettingValue.toInt()).findFirst()!!
            person.address = binding.personAddress.text.toString()
            person.name = binding.personName.text.toString()
        }, Realm.Transaction.OnSuccess {
            Toast.makeText(this, "Person Updated Successfully !", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })

    }

    private fun findPersonById() {
        realm.beginTransaction()
        val person = realm.where<Person>().equalTo("id", gettingValue.toInt()).findFirst()
        if (person != null) {
            binding.personAddress.setText(person.address)
            binding.personName.setText(person.name)
        }
        realm.commitTransaction()
    }

    private fun addPersonToDB() {
        try {
            //auto increment id
            realm.beginTransaction()
            val currentIdNumber: Number? = realm.where(Person::class.java).max("id")
            val nextId: Int = if (currentIdNumber == null) {
                1
            } else {
                currentIdNumber.toInt() + 1
            }
            val person = Person()
            val name = binding.personName.text.toString()
            val address = binding.personAddress.text.toString()
            person.address = address
            person.name = name
            person.id = nextId

            //copy this transaction and commit
            realm.copyToRealmOrUpdate(person)
            realm.commitTransaction()
            Toast.makeText(this, "Person Added Successfully !", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } catch (e: Exception) {
            Toast.makeText(this, "Failed $e", Toast.LENGTH_SHORT).show()
        }
    }
}