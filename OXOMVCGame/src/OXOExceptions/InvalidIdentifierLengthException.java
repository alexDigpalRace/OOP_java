package OXOExceptions;

public class InvalidIdentifierLengthException extends InvalidIdentifierException {
    int length;

    public InvalidIdentifierLengthException(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Invalid Command length, there should only be 2 characters e.g. a1 " +
                "length=" + length + " (InvalidIdentifierLengthException)";
    }
}
