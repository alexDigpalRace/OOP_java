import OXOExceptions.*;

class OXOController {
    OXOModel gameModel;
    private int currentPlayer;
    boolean finished = false;

    public OXOController(OXOModel model) {
        gameModel = model;
        this.currentPlayer = 0;
        gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(this.currentPlayer));
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {
        if(!finished){
            validateCommand(command);
            mapCommand(command);
            finished = detectWin() || gameDrawn();
            nextPlayer();
        }
    }
    //check is game is drawn - board full
    private boolean gameDrawn() {
        for(int i = 0; i < gameModel.getNumberOfRows(); i++){
            for(int j = 0; j < gameModel.getNumberOfColumns(); j++){
                if(gameModel.getCellOwner(i, j).getPlayingLetter() == ' '){
                    //bit pointless using isGameDrawn but didnt want to exclude it
                    return gameModel.isGameDrawn();
                }
            }
        }
        gameModel.setGameDrawn();
        return gameModel.isGameDrawn();
    }
    /*keeps player inbound when incrementing through players*/
    private int playerInbound(int num) {
        if (num >= gameModel.getNumberOfPlayers()) {
            num %= gameModel.getNumberOfPlayers();
        }
        return num;
    }
    /*letters are the rows, numbers are the cols a2 - zeroth row, second column*/
    private void mapCommand(String command) throws OXOMoveException {
        this.validateCommand(command);
        int row = (command.toLowerCase().charAt(0) - 'a');
        int col = (command.charAt(1) - '1');
        if (gameModel.getCellOwner(row, col).getPlayingLetter() == ' ') {
            gameModel.setCellOwner(row, col, gameModel.getCurrentPlayer());
        } else {
            //cell already taken
            throw new CellAlreadyTakenException(command.charAt(0), command.charAt(1));
        }
    }
    /*increment to the next player, loop back round when final player*/
    private void nextPlayer() {
        gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(
                                   playerInbound(++currentPlayer)));
    }
    //check command is valid
    private void validateCommand(String command) throws OXOMoveException {
        int maxCommandLen = 2;
        //more than two chars
        if (command.length() != maxCommandLen) {
            throw new InvalidIdentifierLengthException(command.length());
        }
        //row char wrong
        else if (command.toLowerCase().charAt(0) < 'a' || command.toLowerCase().charAt(0) > 'i') {
            throw new InvalidIdentifierCharacterExeception(command.charAt(0), RowOrColumn.ROW);
        }
        //col char wrong
        else if (command.toLowerCase().charAt(1) < '1' || command.toLowerCase().charAt(1) > '9') {
            throw new InvalidIdentifierCharacterExeception(command.charAt(1), RowOrColumn.COLUMN);
        }
        //outside range of rows
        else if (command.toLowerCase().charAt(0) > gameModel.getNumberOfRows() + ('a' - 1)) {
            throw new OutsideCellRangeException(command.charAt(0), RowOrColumn.ROW);
        }
        //outside range of cols
        else if ((command.charAt(1) - '0') > gameModel.getNumberOfColumns()) {
            throw new OutsideCellRangeException(command.charAt(1), RowOrColumn.COLUMN);
        }
    }
    private boolean detectWin()
    {
        return (horizontal() | vertical() | diagonal());
    }
    //check for horizontal wins
    private boolean horizontal() {
        for(int j = 0; j < gameModel.getNumberOfRows(); j++){
            char player = gameModel.getCellOwner(j, 0).getPlayingLetter();
            int count = 1;
            for (int i = 1; i < gameModel.getNumberOfColumns(); i++) {
                if(player == gameModel.getCellOwner(j, i).getPlayingLetter()){
                    count++;
                } else{
                    player = gameModel.getCellOwner(j, i).getPlayingLetter();
                    count = 1;
                }
                if(count == gameModel.getWinThreshold() && player != ' '){
                    gameModel.setWinner(gameModel.getCellOwner(j, i));
                    return true;
                }
            }
        }
        return false;
    }
    //check for vertical wins
    private boolean vertical() {
        for(int j = 0; j < gameModel.getNumberOfColumns(); j++){
            char player = gameModel.getCellOwner(0, j).getPlayingLetter();
            int count = 1;
            for (int i = 1; i < gameModel.getNumberOfRows(); i++) {
                if(player == gameModel.getCellOwner(i, j).getPlayingLetter()){
                    count++;
                } else{
                    player = gameModel.getCellOwner(i, j).getPlayingLetter();
                    count = 1;
                }
                if(count == gameModel.getWinThreshold() && player != ' '){
                    gameModel.setWinner(gameModel.getCellOwner(i, j));
                    return true;
                }
            }
        }
        return false;
    }
    //go through each cell and check diagonal wins
    private boolean diagonal()
    {
        for(int i = 0; i < gameModel.getNumberOfRows(); i++){
            for(int j = 0; j < gameModel.getNumberOfColumns(); j++){
                if(checkCosDiagonal(i, j) || checkSinDiagonal(i, j)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean checkCosDiagonal(int row, int col)
    {
        int count = 1;
        char player = gameModel.getCellOwner(row, col).getPlayingLetter();
        while(true){
            try {
                if(player == gameModel.getCellOwner(++row, ++col).getPlayingLetter()){
                    count++;
                } else {
                    count = 1;
                    player = gameModel.getCellOwner(row, col).getPlayingLetter();
                }
                if(count == gameModel.getWinThreshold() && player != ' '){
                    gameModel.setWinner(gameModel.getCellOwner(row, col));
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
    }
    private boolean checkSinDiagonal(int row, int col)
    {
        int count = 1;
        char player = gameModel.getCellOwner(row, col).getPlayingLetter();
        while(true){
            try {
                if(player == gameModel.getCellOwner(--row, ++col).getPlayingLetter()){
                    count++;
                } else {
                    count = 1;
                    player = gameModel.getCellOwner(row, col).getPlayingLetter();
                }
                if(count == gameModel.getWinThreshold() && player != ' '){
                    gameModel.setWinner(gameModel.getCellOwner(row, col));
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
    }
}
