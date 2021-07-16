package OXOExceptions;

public class InvalidIdentifierCharacterExeception extends InvalidIdentifierException {
    char character;
    RowOrColumn type;

    public InvalidIdentifierCharacterExeception(char character, RowOrColumn type) {
        this.character = character;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Invalid-Identifier-Character-Exeception? " +
                "character=" + character +
                " type=" + type;
    }
}
