import java.awt.*;
import java.util.*;

class OXOModel {
    private ArrayList<ArrayList<OXOPlayer>> cells;
    //private OXOPlayer cells[][];
    private ArrayList<OXOPlayer> players;
    private OXOPlayer currentPlayer;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        gameDrawn = false;
        winThreshold = winThresh;
        players = new ArrayList<>();
        cells = new ArrayList<>(numberOfRows);

        /*add lists to comprise each row of cells*/
        for (int i = 0; i < numberOfRows; i++) {
            cells.add(new ArrayList<>(numberOfColumns));
        }
        /*add empty player to each rows list to populate columns*/
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                cells.get(i).add(new OXOPlayer(' '));
            }
        }
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void addPlayer(OXOPlayer player) {
        players.add(player);
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players.get(number);
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public OXOPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(OXOPlayer player) {
        currentPlayer = player;
    }

    public int getNumberOfRows() {
        return cells.size();
    }

    public int getNumberOfColumns() {
        return cells.get(0).size();
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        cells.get(rowNumber).set(colNumber, player);
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn() {
        gameDrawn = true;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }
}
