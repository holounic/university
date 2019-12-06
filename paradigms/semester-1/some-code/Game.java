package mnkGame;

public class Game {
    private Player player1, player2;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    private void swapPlayers() {
        Player temp = player1;
        player1 = player2;
        player2 = temp;
    }

    public int series(Board board, int rounds) {
        int[] res = new int[2];

        while (res[0] != rounds && res[1] != rounds) {
            log("first move " + player1.getId() + " second move " + player2.getId());
            int result = this.play(board);
            switch (result) {
                case 1:
                    res[player1.getId()]++;
                    break;
                case 2:
                    res[player2.getId()]++;
                    continue;
            }
            swapPlayers();
        }
        log("player " + (res[0] > res[1] ? 1 : 2) + " wins");
        return (res[0] > res[1] ? 1 : 2);
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

    public int move(final Board board, final Player player, int no) {
        final Move move = player.makeMove(board.getPosition(), board.getCell());
        log("Player " + move.getValue() + " move: " + move);
        final Result result = board.makeMove(move);

        switch(result) {
            case WIN:
                log("Player " + move.getValue() + " won");
                return no;
            case LOSE:
                log("Player " + move.getValue() + " lose");
                return 3 - no;
            case DRAW:
                log("Draw");
                return 0;
            default:
                log("Position:\n" + board);
                    return -1;
        }
    }

    private void log(String message) {
        System.out.println(message);
    }
}