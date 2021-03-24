package pollub.ism.lab04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private boolean isPlayer1Turn = true;
    private int roundCount = 0;
    private Button[][] boardFields = new Button[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Przygotowanie tablicy z polami gry
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                int resourceId = getResources().getIdentifier("field_" + i + j, "id", getPackageName());
                boardFields[i][j] = findViewById(resourceId);
                boardFields[i][j].setText("");
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putBoolean("isPlayer1Turn", isPlayer1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        isPlayer1Turn = savedInstanceState.getBoolean("isPlayer1Turn");
    }

    public void onFieldClick(View view)
    {
        Resources res = getResources();

        if (isPlayer1Turn)
            ((Button) view).setText(res.getString(R.string.player1_symbol));
        else
            ((Button) view).setText(res.getString(R.string.player2_symbol));

        ((Button) view).setClickable(false);
        roundCount++;

        if (isSomebodyWon())        // Wygrana któregoś z graczy
        {
            if (isPlayer1Turn)
                Toast.makeText(this, res.getString(R.string.player1_wins), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, res.getString(R.string.player2_wins), Toast.LENGTH_LONG).show();

            resetBoard();
        }
        else if (roundCount == 9)   // Remis
        {
            Toast.makeText(this, res.getString(R.string.nobody_wins), Toast.LENGTH_LONG).show();

            resetBoard();
        }
        else
            isPlayer1Turn = !isPlayer1Turn;
    }

    private boolean isSomebodyWon() {
        if(roundCount < 5)          // Wygrana jednego z graczy jest możliwa dopiero po wykonaniu 5 ruchów
        {
            // Pozyskanie aktualnego stanu planszy
            String[][] currentBoardState = new String[3][3];
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    currentBoardState[i][j] = boardFields[i][j].getText().toString();

            // Sprawdzenie wierszy planszy
            for (int i = 0; i < 3; i++)
                if (currentBoardState[i][0].equals(currentBoardState[i][1]) && currentBoardState[i][0].equals(currentBoardState[i][2]) &&
                        !currentBoardState[i][0].equals(""))
                    return true;

            // Sprawdzenie kolumn planszy
            for (int i = 0; i < 3; i++)
                if (currentBoardState[0][i].equals(currentBoardState[1][i]) && currentBoardState[0][i].equals(currentBoardState[2][i]) &&
                        !currentBoardState[0][i].equals(""))
                    return true;

            // Sprawdzenie przekątnych planszy
            if (currentBoardState[0][0].equals(currentBoardState[1][1]) && currentBoardState[0][0].equals(currentBoardState[2][2]) &&
                    !currentBoardState[0][0].equals(""))
                return true;

            if (currentBoardState[0][2].equals(currentBoardState[1][1]) && currentBoardState[0][2].equals(currentBoardState[2][0]) &&
                    !currentBoardState[0][2].equals(""))
                return true;
        }

        return false;
    }

    private void resetBoard() {
        // Odblokowanie przycisków i wyczyszczenie ich etykiet
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                boardFields[i][j].setText("");
                boardFields[i][j].setClickable(true);
            }
        }

        // Przywrócenie zmiennych pomocniczych
        roundCount = 0;
        isPlayer1Turn = true;
    }
}