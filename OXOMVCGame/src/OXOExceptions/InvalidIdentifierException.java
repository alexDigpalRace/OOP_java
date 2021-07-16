package OXOExceptions;

public class InvalidIdentifierException extends CellDoesNotExistExeception{
    public InvalidIdentifierException() {
    }


    public InvalidIdentifierException(int row, int column) {
        super(row, column);
    }

    @Override
    public String toString() {
        return "Invalid-Identifier-Exception?";
    }
}
