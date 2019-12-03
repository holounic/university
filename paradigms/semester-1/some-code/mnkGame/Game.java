package mnkGame;

public class Game {
    private final Player player1, player2;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public int play(Board board) {
        log(board.toString());
        while (true) {
            final int result1 = move(board, player1, 1);
            if (result1 != -1) {
                return result1;
            }
            final int result2 = move(board, player2, 2);
            if (result2 != -1) {
                return result2;
            }
        }
    }

    public int move(final Board board, final Player player, final int no) {
        final Move move = player.makeMove(board.getPosition(), board.getCell());
        final Result result = board.makeMove(move);
        log("Player " + no + " move: " + move);
        log("Position:\n" + board);

        switch(result) {
            case WIN:
                log("Player " + no + " won");
                return no;
            case LOSE:
                log("Player " + no + " lose");
                return 3 - no;
            case DRAW:
                log("Draw");
                return 0;
            default:
                    return -1;
        }
    }

    private void log(String message) {
        System.out.println(message);
    }
}