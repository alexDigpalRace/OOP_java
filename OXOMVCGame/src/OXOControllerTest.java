import OXOExceptions.InvalidIdentifierCharacterExeception;
import OXOExceptions.InvalidIdentifierLengthException;
import OXOExceptions.OXOMoveException;
import OXOExceptions.OutsideCellRangeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OXOControllerTest {
    OXOModel gameModel = new OXOModel(3,3,3);
    OXOPlayer player1 = new OXOPlayer('X');
    OXOPlayer player2 = new OXOPlayer('O');

    @Test
    void handleIncomingCommand() throws OXOMoveException {
        gameModel.addPlayer(player1);
        gameModel.addPlayer(player2);
        OXOController gameController = new OXOController(gameModel);

        assertThrows(InvalidIdentifierLengthException.class, () -> {
                gameController.handleIncomingCommand("aa2");
        });
        assertThrows(InvalidIdentifierCharacterExeception.class, () -> {
            gameController.handleIncomingCommand("!1");
        });
        assertThrows(OutsideCellRangeException.class, () -> {
            gameController.handleIncomingCommand("c4");
        });
        assertThrows(InvalidIdentifierLengthException.class, () -> {
            gameController.handleIncomingCommand("aa2");
        });
    }
}