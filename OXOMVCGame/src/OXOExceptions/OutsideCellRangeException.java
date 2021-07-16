package OXOExceptions;

public class OutsideCellRangeException extends CellDoesNotExistExeception{
    int position;
    RowOrColumn type;

    public OutsideCellRangeException(int position, RowOrColumn type) {
        this.position = position;
        this.type = type;
    }

    @Override
    public String toString() {
        return (type + ": " + (char)position + " not within dimensions of the current board " +
                "(OutsideCellRangeException)");
    }
}
