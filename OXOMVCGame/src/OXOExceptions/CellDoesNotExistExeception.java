package OXOExceptions;

public class CellDoesNotExistExeception extends OXOMoveException{
    public CellDoesNotExistExeception() {
    }

    public CellDoesNotExistExeception(int row, int column) {
        super(row, column);
    }

    @Override
    public String toString() {
        return "Cell-Does-Not-Exist-Exeception-.-?";
    }
}
