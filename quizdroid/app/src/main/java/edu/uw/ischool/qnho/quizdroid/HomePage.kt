package edu.uw.ischool.qnho.quizdroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    get the curr activity that store this fragment -> access fragment manager (supportFragment)
//    private val fragmentManager = activity -> no activity connected because layout has not been created -> always null
    private val overview = OverviewPage()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    //    inflate the layout only
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

//    add interactivity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        val math = view.findViewById<LinearLayout>(R.id.math_quiz)
        val physics = view.findViewById<LinearLayout>(R.id.physics_quiz)
        val marvel = view.findViewById<LinearLayout>(R.id.marvel_quiz)

//        can only reference activity in onViewCreated
        math.setOnClickListener{
            switchView("math")
        }

        physics.setOnClickListener{
            switchView("physics")
        }

        marvel.setOnClickListener{
            switchView("marvel")
        }
    }

    private fun switchView(quizId: String){
        val bundle = Bundle()
        bundle.putString("quizId", quizId)
        overview.arguments = bundle
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.app, overview)?.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}