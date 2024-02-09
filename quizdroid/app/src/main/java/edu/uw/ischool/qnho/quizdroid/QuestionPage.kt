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
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

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

    private var questNum: Int = 1
    private var numCorrect = 0

    private val mathOption = mapOf<Int, Array<String>>(1 to arrayOf("2", "4", "6", "8"), 2 to arrayOf("20", "14", "10", "18"), 3 to arrayOf("5", "15", "25", "35"))
    private val physOption = mapOf<Int, Array<String>>(1 to arrayOf("Blah", "Watts", "Yay", "Ohms"), 2 to arrayOf("Fire", "Water", "Wind", "The sun"),
                                                       3 to arrayOf("Einstein", "Galileo", "Newton", "Copernicus"))
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

        if(questNum <= 3) {
            updateQuestion(layout)
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

        var isChosen = false
        var option = ""
        var chosenBtn = choice1

        choice1.setOnCheckedChangeListener {buttonView, isChecked ->
            option = choice1.text.toString()
            chosenBtn = choice1
            submitFun(view, option, quizName, chosenBtn)
        }

        choice2.setOnCheckedChangeListener {buttonView, isChecked ->
            option = choice2.text.toString()
            chosenBtn = choice2
            submitFun(view, option, quizName, chosenBtn)
        }

        choice3.setOnCheckedChangeListener {buttonView, isChecked ->
            option = choice3.text.toString()
            chosenBtn = choice3
            submitFun(view, option, quizName, chosenBtn)
        }

        choice4.setOnCheckedChangeListener {buttonView, isChecked ->
            option = choice4.text.toString()
            chosenBtn = choice4
            submitFun(view, option, quizName, chosenBtn)
        }
    }

    private fun submitFun(view: View, option: String, quizName: String?, chosenBtn: RadioButton){
        val submit = view.findViewById<Button>(R.id.submit_btn)
        val nextBtn = view.findViewById<Button>(R.id.next_btn)

        submit.setOnClickListener {
            if(chooseAnswer(view, option, quizName, questNum.toString(), chosenBtn)){
                numCorrect++
            }

            val toast = Toast.makeText(activity, "You have $numCorrect out of 3 correct", Toast.LENGTH_SHORT)
            toast.show()

            submit.visibility = View.GONE
            nextBtn.visibility = View.VISIBLE

            if(questNum < 3){
                nextBtn.setOnClickListener{
                    updateQuestion(view)
                    submit.visibility = View.VISIBLE
                    nextBtn.visibility = View.GONE
                }
            }else{
                nextBtn.text = "Finish"
                nextBtn.setOnClickListener{
                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.app, HomePage())?.commit()
                }
            }

            questNum++
        }
    }

//    override fun onResume() {
//        super.onResume()
//        Log.i("TEST5", "num of questions: $questNum")
//        val btn = view?.findViewById<Button>(R.id.submit_btn)
//            Log.i("TEST5", "whyyyyy")
//            view?.let { updateQuestion(it) }
//    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun chooseAnswer(layout: View, choice: String, quizId: String?, question: String, btn: RadioButton): Boolean {
        var quiz: Map<String, String> = mathAns;
        var correct = false

        val choice1 = layout.findViewById<RadioButton>(R.id.option_1)
        val choice2 = layout.findViewById<RadioButton>(R.id.option_2)
        val choice3 = layout.findViewById<RadioButton>(R.id.option_3)
        val choice4 = layout.findViewById<RadioButton>(R.id.option_4)

        val choices = arrayOf(choice1, choice2, choice3, choice4)

        when(quizId){
            "math" -> quiz = mathAns
            "physics" -> quiz = physicsAns
            "marvel" -> quiz = marvelAns
        }

        if(questNum <= quiz.size){
            val ans = quiz[quiz.keys.toList()[questNum - 1]]
            if (ans != null){
                if (ans == choice) {
                    btn.setTextColor(Color.parseColor("#24293E"))
                    btn.setBackgroundColor(Color.parseColor("#77DD77"))
                    correct = true
                } else {
                    btn.setTextColor(Color.parseColor("#24293E"))
                    btn.setBackgroundColor(Color.parseColor("#FE805D"))
                    correct = false

                    for(i in choices.indices){
                        if (ans == choices[i].text){
                            choices[i].setTextColor(Color.parseColor("#24293E"))
                            choices[i].setBackgroundColor(Color.parseColor("#77DD77"))
                        }
                    }
                }
            }
        }

        return correct
    }

    private fun updateQuestion(layout: View){
        val questionNum = layout.findViewById<TextView>(R.id.num_question)
        val question = layout.findViewById<TextView>(R.id.question)
        val quizName = arguments?.getString("quizId")

        val radioGroup = layout.findViewById<RadioGroup>(R.id.choices)
        radioGroup.clearCheck()

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

        if(questNum <= 3) {
            Log.i("TEST4", "do you go in here?")
            questionNum.text = "Question $questNum of 3"
            question.text = quiz.keys.toList()[questNum - 1]
            val questions = options[questNum]

            if (questions != null) {
                for(i in choices.indices) {
                    choices[i].background = null
                    choices[i].text = options[questNum]?.get(i) ?: ""
                    choices[i].setTextColor(Color.parseColor("#F4F5FC"))
                }
            }
        }
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