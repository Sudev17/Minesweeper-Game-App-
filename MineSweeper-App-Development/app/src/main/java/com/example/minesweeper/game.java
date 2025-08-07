package com.example.minesweeper;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class game extends AppCompatActivity {

    private GridView gridView;
    private TextView scoreTextView;
    private Button quitButton;
    private Button playAgainButton;
    private Cell[][] minesweeperGrid;
    private final int numColumns = 6;
    private final int numRows = 6;
    public final int numMines = 4;
    private boolean gameOver = false; // Game over flag
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gridView = findViewById(R.id.gridView);
        scoreTextView = findViewById(R.id.scoreTextView);
        quitButton = findViewById(R.id.quitButton);
        playAgainButton = findViewById(R.id.playAgainButton);

        minesweeperGrid = new Cell[numRows][numColumns];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                minesweeperGrid[i][j] = new Cell();
            }
        }
        gridView.setAdapter(new MinesweeperAdapter());

        initializeMines();
        calculateAdjacentMines();

        quitButton.setOnClickListener(v -> onQuitButtonClick());
        playAgainButton.setOnClickListener(v -> resetGame());
    }

    private void initializeMines() {
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < numMines) {
            int row = random.nextInt(numRows);
            int column = random.nextInt(numColumns);
            if (!minesweeperGrid[row][column].isMine()) {
                minesweeperGrid[row][column].setMine(true);
                placedMines++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                if (!minesweeperGrid[row][column].isMine()) {
                    int mineCount = 0;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            int newRow = row + i;
                            int newCol = column + j;
                            if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numColumns &&
                                    minesweeperGrid[newRow][newCol].isMine()) {
                                mineCount++;
                            }
                        }
                    }
                    minesweeperGrid[row][column].setAdjacentMines(mineCount);
                }
            }
        }
    }

    private class MinesweeperAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return numColumns * numRows;
        }

        @Override
        public Object getItem(int position) {
            int row = position / numColumns;
            int column = position % numColumns;
            return minesweeperGrid[row][column];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int row = position / numColumns;
            int column = position % numColumns;
            Cell cell = (Cell) getItem(position);
            TextView textView = (convertView == null) ? new TextView(game.this) : (TextView) convertView;
            textView.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
            textView.setPadding(8, 8, 8, 8);
            textView.setTextSize(30f);
            textView.setOnClickListener(v -> onCellClick(row, column));

            if (cell.isRevealed()) {
                if (cell.isMine()) {
                    textView.setText("\uD83D\uDCA3"); // Set "G" for revealed mines
                    score++; // Increment score
                } else {
                    textView.setText("\uD83D\uDC8E"); // No text for safe revealed cells
                }
                textView.setBackgroundResource(android.R.color.holo_green_light); // Change background color to blue for revealed cells
            } else {
                textView.setText(""); // No text for unrevealed cells
                textView.setBackgroundResource(android.R.color.darker_gray); // Change background color to grey for unrevealed cells
            }
            return textView;
        }
    }

    private void onCellClick(int row, int column) {
        if (!gameOver) { // Check if the game is already over
            Cell cell = minesweeperGrid[row][column];
            if (!cell.isRevealed()) {
                cell.setRevealed(true);
                if (cell.isMine()) {
                    // If the clicked cell is a mine, reveal all mines and handle end game logic
                    revealAllMines();
                    // Display a message indicating game over
                    Toast.makeText(this, " Opps u Lost...!! ", Toast.LENGTH_SHORT).show();
                    gameOver = true; // Set the game over flag
                } else {
                    // Increment score for a safe cell and update the scoreTextView
                    score++; // Increment the score for revealing a gem
                    scoreTextView.setText("Score: " + score);
                }
                ((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
            }
        }
    }


    private void revealAllMines() {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                if (minesweeperGrid[row][column].isMine()) {
                    minesweeperGrid[row][column].setRevealed(true);
                }
            }
        }
    }

    private void onQuitButtonClick() {
        // Show the score in a Toast message
        Toast.makeText(this, "Your score is: " + score, Toast.LENGTH_SHORT).show();
        // Reset the game
        resetGame();
    }

    private void resetGame() {
        score = 0;
        gameOver = false;
        scoreTextView.setText("Score: " + score);
        minesweeperGrid = new Cell[numRows][numColumns];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                minesweeperGrid[i][j] = new Cell();
            }
        }
        initializeMines();
        calculateAdjacentMines();
        ((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
    }

    private static class Cell {
        private boolean isMine;
        private int adjacentMines;
        private boolean isRevealed;

        public boolean isMine() {
            return isMine;
        }

        public void setMine(boolean mine) {
            isMine = mine;
        }

        public int getAdjacentMines() {
            return adjacentMines;
        }

        public void setAdjacentMines(int adjacentMines) {
            this.adjacentMines = adjacentMines;
        }

        public boolean isRevealed() {
            return isRevealed;
        }

        public void setRevealed(boolean revealed) {
            isRevealed = revealed;
        }
    }
}
