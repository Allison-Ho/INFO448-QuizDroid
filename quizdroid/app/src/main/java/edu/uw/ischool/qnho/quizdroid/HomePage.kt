package edu.uw.ischool.qnho.quizdroid

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment


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

        val quizApp = requireActivity().application as QuizApp
        val repo = quizApp.quizzes
        val allTopic = repo.getTopic()

        Log.i("MAYBE", allTopic.toString())

        val topics = arrayOf(R.id.topic_0, R.id.topic_1, R.id.topic_2)
        for (quiz in allTopic.indices) {
            val topic = view.findViewById<LinearLayout>(topics[quiz])
            val title = TextView(activity?.applicationContext)
            title.text = allTopic[quiz].first
            title.setTextColor(Color.parseColor("#F4F5FC"))
            title.textSize = 25.0F
            title.typeface = Typeface.DEFAULT_BOLD

            title.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            title.setPadding(40, 35, 0, 0)

            topic.addView(title)

            val desc = TextView(activity?.applicationContext)
            desc.text = allTopic[quiz].second
            desc.setTextColor(Color.parseColor("#F4F5FC"))
            desc.textSize = 14.0F

            desc.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            desc.setPadding(40, 2, 0, 0)

            topic.addView(desc)

            val topicInfo = repo.getTopicInfo(allTopic[quiz].first)

            topic.setOnClickListener{
                switchView(allTopic[quiz].first, topicInfo[0].toString(), topicInfo[1].toString().toInt(), topicInfo[2].toString().toInt())
            }
        }

    }

    private fun switchView(quizId: String, longDesc : String, totalQuest : Int, icon: Int){
        val bundle = Bundle()
        bundle.putString("quizId", quizId)
        bundle.putString("longDesc", longDesc)
        bundle.putInt("totalQuest", totalQuest)
        bundle.putInt("icon", icon)
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