package OXOExceptions;

public class CellAlreadyTakenException extends OXOMoveException{

    public CellAlreadyTakenException() {
    }

    public CellAlreadyTakenException(int row, int column) {
        super(row, column);
    }

    @Override
    public String toString() {
        return ("Cell: " + (char)super.getRow() + (super.getColumn()-'0') +
                " Already Taken (CellAlreadyTakenException)");
    }

}
