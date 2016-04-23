package com.example.music.androidstudiocalculator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private int[] numericButtons = {R.id.buttonZero, R.id.buttonOne, R.id.buttonTwo, R.id.buttonThree, R.id.buttonFour, R.id.buttonFive,
                                    R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight, R.id.buttonNine};

    private int[] operatorButtons = {R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide};

    private TextView calcArea;

    private boolean lastNumeric;

    private boolean stateError;

    private boolean lastDot;

    private boolean firstUse = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.calcArea = (TextView) findViewById(R.id.calcArea);

        setNumberOnClickListener();

        setOperatorOnClickListener();
    }

    /**
     * create and set OnClickListener to the number buttons.
     */
    private void setNumberOnClickListener() {
        // Create an OnClickListener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just append/set the text of a clicked button
                Button button = (Button) v;
                if (stateError) {
                    // If there is an error, replace the error message

                    calcArea.setText(button.getText());
                    stateError = false;
                } else {
                    if (firstUse)
                    {
                        calcArea.setText("");
                        firstUse = false;
                    }
                    // there is a valid expression so append to it
                    calcArea.append(button.getText());
                }
                // Set the lastNumeric boolean/flag to true
                lastNumeric = true;
            }
        };
        // Assign the listener to all the number buttons
        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    /**
     * create and set OnClickListener to operators, equal button and decimal point.
     */
    private void setOperatorOnClickListener() {
        // Create a common OnClickListener for operators
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the current state is Error do not add the operator
                // If the last input is a number, add the operator
                if (lastNumeric && !stateError) {
                    Button button = (Button) v;
                    calcArea.append(button.getText());
                    lastNumeric = false;
                    // Reset the dot boolean
                    lastDot = false;
                }
            }
        };
        // Assign the listener to all the operator buttons
        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(listener);
        }
        // Decimal point
        findViewById(R.id.buttonDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError && !lastDot) {
                    calcArea.append(".");
                    lastNumeric = false;
                    lastDot = true;
                }
            }
        });
        // Clear button
        findViewById(R.id.buttonClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the screen
                calcArea.setText("");
                // Reset all the booleans
                lastNumeric = false;
                stateError = false;
                lastDot = false;
                firstUse = true;
            }
        });

        findViewById(R.id.buttonEquals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });
    }


    private void onEqual() {

        if (lastNumeric && !stateError) {

            String txt = calcArea.getText().toString();

            Expression expression = new ExpressionBuilder(txt).build();
            try {

                double result = expression.evaluate();
                calcArea.setText(Double.toString(result));

                lastDot = true;
            } catch (ArithmeticException ex) {

                calcArea.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }
}
