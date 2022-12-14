package com.example.firebase.Fragments

import android.content.Context
import android.os.Bundle
import android.text.Selection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.firebase.R
import com.example.firebase.classes.Data
import com.google.firebase.database.*

class Add : Fragment() {

    lateinit var sitee_: EditText
    lateinit var id_: EditText
    lateinit var password_: EditText
    lateinit var image_: EditText
    lateinit var b3: TextView
    lateinit var ids: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_add, container, false)

        var b1 = view.findViewById<Button>(R.id.b1)
        var b2 = view.findViewById<Button>(R.id.b2)
        b3 = view.findViewById(R.id.t1)


        sitee_ = view.findViewById(R.id.site)
        id_ = view.findViewById(R.id.userid)

        password_ = view.findViewById(R.id.password)

        image_ = view.findViewById(R.id.image)



        var ref =
            FirebaseDatabase.getInstance("https://retrieve-4329f-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        b3.visibility = View.GONE

        b1.setOnClickListener {

            ids = id_.text.toString()
            var site = sitee_.text.toString()
            var pass = password_.text.toString()
            var images = image_.text.toString()
            if (ids.isNotEmpty() && site.isNotEmpty() && pass.isNotEmpty() && images.isNotEmpty()) {

                var data = Data(ids, images, pass, site)

                ref.child("sites").child(site).setValue(data)
                    .addOnSuccessListener {

                        Toast.makeText(activity as Context, "Successful", Toast.LENGTH_SHORT)
                            .show()
                        sitee_.setText("")
                        id_.setText("")
                        password_.setText("")
                        image_.setText("")

                        b3.visibility = View.VISIBLE
                        b3.setOnClickListener {
                            view.findNavController().navigate(R.id.action_add2_to_read)
                        }
                    }
            } else {

                Toast.makeText(
                    activity as Context,
                    "Fields should not be Empty",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        b2.setOnClickListener {
            ids = id_.text.toString()
            var site = sitee_.text.toString()
            var pass = password_.text.toString()

            var images = image_.text.toString()
            if (ids.isNotEmpty() && site.isNotEmpty() && pass.isNotEmpty() && images.isNotEmpty()) {
                editdata(ids, pass, images, site, ref)
            }else{
                Toast.makeText(
                    activity as Context,
                    "Fields should not be Empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view
    }

    fun editdata(
        id: String,
        password: String,
        image: String,
        sitename: String,
        ref: DatabaseReference
    ) {
        try {


            var hash = HashMap<String, Any>()
            hash.put("ids", id)
            hash.put("image", image)
            hash.put("password", password)

            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(sitename)){

                        ref.child("sites").child(sitename).updateChildren(hash)
                            .addOnSuccessListener {
                                Toast.makeText(activity as Context, "Successful", Toast.LENGTH_SHORT).show()
                                sitee_.setText("")
                                id_.setText("")
                                password_.setText("")
                                image_.setText("")
                                b3.visibility = View.VISIBLE
                                b3.setOnClickListener {
                                    view?.findNavController()?.navigate(R.id.action_add2_to_read)
                                }


                            }
                    }else{
                        Toast.makeText(activity as Context, "Site not Exist", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        } catch (e: Exception) {

            Toast.makeText(activity as Context, "Sucfythfcessful", Toast.LENGTH_SHORT).show()
        }

    }
}