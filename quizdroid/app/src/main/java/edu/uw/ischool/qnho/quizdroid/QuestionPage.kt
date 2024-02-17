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
    private var total: Int = 0

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

        total = arguments?.getInt("totalQuest")!!

        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizName = arguments?.getString("quizId")

        val quizApp = requireActivity().application as QuizApp
        val repo = quizApp.quizzes
        val quiz = repo.quizByTopic(quizName!!)

        if (quiz != null) {
            displayQuestion(view, quiz[questNum - 1].text, quiz[questNum - 1].ans)

            var chosen : RadioButton = view.findViewById(R.id.option_1)
            val radioGroup = view.findViewById<RadioGroup>(R.id.choices)
            radioGroup.setOnCheckedChangeListener { group, id ->
                Log.i("MEOW", id.toString())
                if (id > 0) {
                    chosen = view.findViewById(id)
                }

            }

            val submitBtn = view.findViewById<Button>(R.id.submit_btn)
            val nextBtn = view.findViewById<Button>(R.id.next_btn)

            submitBtn.setOnClickListener {
                submitAns(chosen, quiz)

                submitBtn.visibility = View.GONE
                nextBtn.visibility = View.VISIBLE

                if(questNum < total){
                    questNum++
                    nextBtn.setOnClickListener{
                        submitBtn.visibility = View.VISIBLE
                        nextBtn.visibility = View.GONE
                        displayQuestion(view, quiz[questNum - 1].text, quiz[questNum - 1].ans)
                    }
                }else{
                    nextBtn.text = "Finish"
                    nextBtn.setOnClickListener{
                        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.app, HomePage())?.commit()
                    }
                }
            }
        }
    }

    private fun submitAns(chosenBtn: RadioButton, questions: List<Question>) {
        val currQuestion = questions[questNum - 1]

        if(chosenBtn.text == currQuestion.ans[currQuestion.correct]) {
            numCorrect++
            chosenBtn.setTextColor(Color.parseColor("#24293E"))
            chosenBtn.setBackgroundColor(Color.parseColor("#77DD77"))

        } else {
            chosenBtn.setTextColor(Color.parseColor("#24293E"))
            chosenBtn.setBackgroundColor(Color.parseColor("#FE805D"))
        }

        Toast.makeText(activity, "You have $numCorrect out of 3 correct", Toast.LENGTH_SHORT).show()

    }

    private fun displayQuestion(layout: View, text: String, answers: List<String>) {
        val questionNum = layout.findViewById<TextView>(R.id.num_question)
        val question = layout.findViewById<TextView>(R.id.question)

        if (questNum <= total) {
            questionNum.text = "Question $questNum of $total"
        }

        question.text = text

        val trying = layout.findViewById<RadioGroup>(R.id.choices)
        Log.i("BRO", trying.toString())
        trying.clearCheck()

        val choices = arrayOf(R.id.option_1, R.id.option_2, R.id.option_3, R.id.option_4)

        // show the 4 options
        for (i in choices.indices) {
            val choice = layout.findViewById<RadioButton>(choices[i])
            choice.background = null
            choice.text = answers[i]
            choice.setTextColor(Color.parseColor("#F4F5FC"))
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