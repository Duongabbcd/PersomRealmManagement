package gst.trainingcourse.persomrealmmanagement.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import gst.trainingcourse.persomrealmmanagement.R
import gst.trainingcourse.persomrealmmanagement.adapter.PersonAdapter
import gst.trainingcourse.persomrealmmanagement.databinding.ActivityMainBinding
import gst.trainingcourse.persomrealmmanagement.model.Person
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var personList :ArrayList<Person>
    private lateinit var searchView : SearchView
    private  var realm :Realm? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view  = binding.root
        setContentView(view)

        personList = ArrayList()
        realm = Realm.getDefaultInstance()

        binding.addPerson.setOnClickListener {
            val intent =Intent(this,AddPersonActivity::class.java)
            intent.putExtra("id","-1")
            startActivity(intent)
            finish()
        }
        searchView =findViewById(R.id.edt_search)
        binding.edtSearch?.queryHint=
            Html.fromHtml("<font color = #ffffff>" + resources.getString(R.string.search_something) + "</font>")

        binding.personRV.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        getAllPersonInfo()
        searchPersonInfo()
        binding.menu.setOnClickListener {
            showPopUp(binding.menu,searchView.query.toString())
        }
    }

    private fun showPopUp(view:View,x:String) {

      val popUp = PopupMenu(this,view)
        popUp.inflate(R.menu.top_app_menu)
        popUp.setOnMenuItemClickListener (PopupMenu.OnMenuItemClickListener { item:MenuItem? ->
                when(item!!.itemId){
                    R.id.ascending->{
                        val results:RealmResults<Person> =realm!!.where(Person::class.java).contains("name",x,Case.INSENSITIVE)
                            .sort("name",Sort.ASCENDING).findAll()
                        binding.personRV.adapter=PersonAdapter(this,results)
                        binding.personRV.adapter!!.notifyDataSetChanged()
                    }
                    R.id.descending->{
                        val results:RealmResults<Person> =realm!!.where(Person::class.java).contains("name",x,Case.INSENSITIVE)
                            .sort("name",Sort.DESCENDING).findAll()
                        binding.personRV.adapter=PersonAdapter(this,results)
                        binding.personRV.adapter!!.notifyDataSetChanged()
                    }
                }
            true
        })
        popUp.show()
    }

    private fun searchPersonInfo() {
      searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
           override fun onQueryTextSubmit(query: String): Boolean {
             return false
           }

           override fun onQueryTextChange(name: String): Boolean {
                if(!name.isNullOrEmpty()){
                    filter(name)
                }else{
                    getAllPersonInfo()
                }
               return true
           }

       })
    }

    private fun filter(name:String){
        val results:RealmResults<Person> =realm!!.where(Person::class.java).contains("name",name,Case.INSENSITIVE).findAll()

        binding.personRV.adapter=PersonAdapter(this,results)
        binding.personRV.adapter!!.notifyDataSetChanged()

    }

    private fun getAllPersonInfo() {
        personList.clear()
        val results:RealmResults<Person> = realm!!.where(Person::class.java).findAll()

        binding.personRV.adapter = PersonAdapter(this,results)
        binding.personRV.adapter!!.notifyDataSetChanged()
    }
}

