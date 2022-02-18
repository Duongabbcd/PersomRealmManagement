package gst.trainingcourse.persomrealmmanagement.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.persomrealmmanagement.databinding.ItemPersonInfomationBinding
import gst.trainingcourse.persomrealmmanagement.model.Person
import gst.trainingcourse.persomrealmmanagement.view.AddPersonActivity
import io.realm.RealmResults

class PersonAdapter(private val context: Activity, private val personList: RealmResults<Person>)
    :RecyclerView.Adapter<PersonAdapter.PersonViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int):
            PersonViewHolder {
        val binding = ItemPersonInfomationBinding.inflate(LayoutInflater.from(context),parent,false)
        return PersonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]
       holder.display(position)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,AddPersonActivity::class.java)
            intent.putExtra("id" ,person!!.id.toString())
            val x =   person!!.id
            Log.d("send String Extra","$x" )
            context.startActivity(intent)
        }
    }

    inner class PersonViewHolder(private val binding:ItemPersonInfomationBinding):
        RecyclerView.ViewHolder(binding.root){
        fun display(position: Int) {
            binding.itemId.text = personList[position]!!.id.toString()
            binding.itemName.text=personList[position]!!.name.toString()
            binding.itemAddress.text =personList[position]!!.address.toString()
        }

    }

}