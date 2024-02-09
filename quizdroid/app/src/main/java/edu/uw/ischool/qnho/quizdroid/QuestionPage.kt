package edu.uw.ischool.qnho.quizdroid

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val popUp = AnswerPopUp()

    private var questNum: Int = 1

    private val mathOption = mapOf<Int, Array<String>>(1 to arrayOf("2", "4", "6", "8"), 2 to arrayOf("20", "14", "10", "18"), 3 to arrayOf("5", "15", "25", "35"))
    private val physOption = mapOf<Int, Array<String>>(1 to arrayOf("Blah", "Watts", "Yay", "Ohms"), 2 to arrayOf("Fire", "Water", "Wind", "The sun"),
                                                       3 to arrayOf("Einstein", "Galileo", "Copernicus", "Newton"))
    private val marvelOption = mapOf<Int, Array<String>>(1 to arrayOf("Midgard", "Asgard", "Nidavellir", "Jotunheim"),
                                                         2 to arrayOf("Captain America", "Thor", "Iron Man", "Doctor Strange"),
                                                         1 to arrayOf("Time stone", "Soul stone", "Mind stone", "Space stone"))

    private val mathAns = mapOf<String, String>("1 + 1" to "2", "4 + 6" to "10", "3 * 5" to "15")
    private val physicsAns = mapOf<String, String>("Electric power is typically measured in what units" to "Watts",
                                                    "What is the earth's primary source of energy" to "The sun",
                                                    "Who became famous after being hit in the head by an apple?" to "Newton")
    private val marvelAns = mapOf<String, String>("Where do Thor and Loki live?" to "Asgard",
                                                  "Who is the first super hero of the MCU?" to "Iron Man",
                                                  "What is the name of the infinity stone that Thanos took from Loki?" to "Mind stone")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("quizId")
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_question_page, container, false)
        val questionNum = layout.findViewById<TextView>(R.id.num_question)
        val question = layout.findViewById<TextView>(R.id.question)
        val quizName = arguments?.getString("quizId")

        val choice1 = layout.findViewById<RadioButton>(R.id.option_1)
        val choice2 = layout.findViewById<RadioButton>(R.id.option_2)
        val choice3 = layout.findViewById<RadioButton>(R.id.option_3)
        val choice4 = layout.findViewById<RadioButton>(R.id.option_4)

        val choices = arrayOf(choice1, choice2, choice3, choice4)

        //get questions
        var quiz = mathAns

        //get answer options
        var options = mathOption

        when(quizName){
            "math" -> {options = mathOption; quiz = mathAns}
            "physics" -> {options = physOption; quiz = physicsAns}
            "marvel" -> {options = marvelOption; quiz = marvelAns}
        }

        questionNum.text = "Question $questNum of 3"
        question.text = quiz.keys.toList()[questNum - 1]
        val questions = options[questNum]

        if (questions != null) {
            for(i in choices.indices) {
                choices[i].text = options[questNum]?.get(i) ?: ""
            }
        }

        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizName = arguments?.getString("quizId")

        val choice1 = view.findViewById<RadioButton>(R.id.option_1)
        val choice2 = view.findViewById<RadioButton>(R.id.option_2)
        val choice3 = view.findViewById<RadioButton>(R.id.option_3)
        val choice4 = view.findViewById<RadioButton>(R.id.option_4)
        val submit = view.findViewById<Button>(R.id.submit_btn)
        
        var option = ""
        var chosenBtn = choice1

        choice1.setOnClickListener{
            option = choice1.text.toString()
            chosenBtn = choice1
        }

        choice2.setOnClickListener{
            option = choice2.text.toString()
            chosenBtn = choice2
        }

        choice3.setOnClickListener{
            option = choice3.text.toString()
            chosenBtn = choice3
        }

        choice4.setOnClickListener{
            option = choice4.text.toString()
            chosenBtn = choice4
        }

        submit.setOnClickListener {
            var numCorrect = 0
            if(chooseAnswer(option, quizName, questNum.toString(), chosenBtn)){ numCorrect++ }

            Log.i("TEST", numCorrect.toString())
            val bundle = Bundle()
            bundle.putString("numQuestion", questNum.toString())
            bundle.putString("correctNum", numCorrect.toString())
            popUp.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.app, popUp)?.commit()
            questNum++

        }
    }

    private fun chooseAnswer(choice: String, quizId: String?, question: String, btn: RadioButton): Boolean {
        var quiz: Map<String, String> = mathAns;

        when(quizId){
            "math" -> quiz = mathAns
            "physics" -> quiz = physicsAns
            "marvel" -> quiz = marvelAns
        }

        if (quiz[quiz.keys.toList()[questNum - 1]] != null){
            if (quiz[quiz.keys.toList()[questNum - 1]] == choice) {
                btn.setTextColor(Color.parseColor("#24293E"))
                btn.setBackgroundColor(Color.parseColor("#77DD77"))
                return true
            } else {
                btn.setTextColor(Color.parseColor("#24293E"))
                btn.setBackgroundColor(Color.parseColor("#FE805D"))
            }
        }

        return false
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuestionPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuestionPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}