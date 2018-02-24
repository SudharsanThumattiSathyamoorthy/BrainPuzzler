package labs.achu.sudharsan.com.brainpuzzler;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import labs.achu.sudharsan.com.brainpuzzler.utils.PuzzleAnswer;

import static labs.achu.sudharsan.com.brainpuzzler.utils.BrainPuzzlerConstants.BRAIN_PUZZLER;
import static labs.achu.sudharsan.com.brainpuzzler.utils.BrainPuzzlerConstants.MAIN_ACTIVITY;

public class MainActivity extends AppCompatActivity {

    private static final GradientDrawable CORRECT_ANSWER;
    private static final GradientDrawable WRONG_ANSWER;
    private static final Map<PuzzleAnswer, GradientDrawable> GRADIENT_DRAWABLE_MAP;

    static {
        CORRECT_ANSWER = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{0xFF24FE41, 0xFFFDFC47});
        CORRECT_ANSWER.setCornerRadius(25.0f);
        CORRECT_ANSWER.setShape(GradientDrawable.RECTANGLE);

        WRONG_ANSWER = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{0xFFEE0979, 0xFFFF6A00});
        WRONG_ANSWER.setCornerRadius(25.0f);
        WRONG_ANSWER.setShape(GradientDrawable.RECTANGLE);

        final Map<PuzzleAnswer, GradientDrawable> gradientDrawableMap = new HashMap<>();
        gradientDrawableMap.put(PuzzleAnswer.CORRECT_ANSWER, CORRECT_ANSWER);
        gradientDrawableMap.put(PuzzleAnswer.WRONG_ANSER, WRONG_ANSWER);

        GRADIENT_DRAWABLE_MAP = Collections.unmodifiableMap(gradientDrawableMap);
    }

    private Button startButton;
    private List<Integer> answerList = new ArrayList<>();
    private int correctAnswerLocation;
    private int noOfCorrectAnswers;
    private int noOfTotalQuestions;

    public void changeToGameLayout(final View view) {
        this.startButton.setVisibility(View.INVISIBLE);

        setContentView(R.layout.game_layout);
        initializeTimer();
    }

    private void initializeTimer() {
        final TextView counterTextView = findViewById(R.id.counterTextView);
        final TextView resulTextView = findViewById(R.id.resultTextView);

        new CountDownTimer(45000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                counterTextView.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                counterTextView.setText("0s");

                resulTextView.setVisibility(View.VISIBLE);
                resulTextView.setText("Game Over!!");

                resulTextView.setBackgroundResource(R.color.gameOver);

                setContentView(R.layout.game_over_layout);

                final TextView scoreTextView = findViewById(R.id.scoreTextView);
                scoreTextView.setText("You scored: " + noOfCorrectAnswers + "/" + noOfTotalQuestions);
            }
        }.start();

        setNextQuestion();

        Toast.makeText(this, "Please select answer from above 4 buttons.", Toast.LENGTH_LONG).show();
    }

    public void restartGame(final View view) {
        this.noOfCorrectAnswers = 0;
        this.noOfTotalQuestions = 0;

        changeToGameLayout(view);
    }

    private void setNextQuestion() {
        // Clear the existing entries in answer list.
        this.answerList.clear();

        final Random random = new Random();
        final int firstNo = random.nextInt(49);
        final int secondNo = random.nextInt(49);

        final TextView questionTextView = findViewById(R.id.questionTextView);
        questionTextView.setText(Integer.toString(firstNo) + " + " + Integer.toString(secondNo));

        final Button resultButton1 = findViewById(R.id.result_button_1);
        final Button resultButton2 = findViewById(R.id.result_button_2);
        final Button resultButton3 = findViewById(R.id.result_button_3);
        final Button resultButton4 = findViewById(R.id.result_button_4);

        correctAnswerLocation = random.nextInt(4);

        for (int i = 0; i < 4; i++) {
            if (i == correctAnswerLocation) {
                answerList.add(firstNo + secondNo);
            } else {

                int incorrectAnswer = random.nextInt(98);

                while (incorrectAnswer == firstNo + secondNo) {
                    incorrectAnswer = random.nextInt(98);
                }

                answerList.add(incorrectAnswer);
            }
        }

        resultButton1.setText(Integer.toString(answerList.get(0)));
        resultButton2.setText(Integer.toString(answerList.get(1)));
        resultButton3.setText(Integer.toString(answerList.get(2)));
        resultButton4.setText(Integer.toString(answerList.get(3)));
    }

    public void verifyAnswer(final View view) {
        Log.i(BRAIN_PUZZLER, (String) view.getTag());

        final String selectedTag = (String) view.getTag();

        final TextView resultTextView = findViewById(R.id.resultTextView);
        resultTextView.setVisibility(View.VISIBLE);

        if (selectedTag.equals(String.valueOf(correctAnswerLocation))) {
            resultTextView.setText("Correct!!");

            resultTextView.setBackground(GRADIENT_DRAWABLE_MAP.get(PuzzleAnswer.CORRECT_ANSWER));
            this.noOfCorrectAnswers++;
        } else {
            resultTextView.setText("Wrong!!");

            resultTextView.setBackground(GRADIENT_DRAWABLE_MAP.get(PuzzleAnswer.WRONG_ANSER));
        }

        // Update the score text view.
        this.noOfTotalQuestions++;
        final TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(Integer.toString(this.noOfCorrectAnswers) + "/" + Integer.toString(this.noOfTotalQuestions));

        setNextQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_start);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        startButton = findViewById(R.id.start_button);
        startButton.setVisibility(View.VISIBLE);
    }

    @SuppressLint("LongLogTag")
    public void startGame(final View view) {
        Log.i(MAIN_ACTIVITY, "Game is launched");

        changeToGameLayout(view);
    }
}
