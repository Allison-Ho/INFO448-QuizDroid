package edu.uw.ischool.qnho.quizdroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OverviewPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class OverviewPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val name: Map<String, String> = mapOf("math" to "Math", "physics" to "Physics", "marvel" to "Marvel Super Heroes")
    private val questionPage = QuestionPage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        val quizName = arguments?.getString("quizId")
        val overview = view.findViewById<TextView>(R.id.overview)
        overview.text = name[quizName]
        overview.textSize = 35.0F
        val test = view.findViewById<Button>(R.id.beginBtn)

        val bundle = Bundle()
        bundle.putString("quizId", quizName)
        questionPage.arguments = bundle

//        can only reference activity in onViewCreated
        test.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.app, questionPage)?.addToBackStack(null)?.commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OverviewPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OverviewPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

