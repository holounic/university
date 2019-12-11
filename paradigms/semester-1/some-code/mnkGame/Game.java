package mnkGame;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Integer> results = new HashMap<>();
        String nickName1 = player1.getNickName();
        String nickName2 = player2.getNickName();
        results.put(nickName1, 0);
        results.put(nickName2, 0);

        log("Are you ready for the most epic battle?\n" + nickName1 + " VS " + nickName2);

        while (results.get(nickName1) + results.get(nickName2) != rounds) {
            log("____________________________");
            log(player1.getNickName() + " begins");
            int result = this.play(board);
            switch (result) {
                case 1:
                    results.replace(player1.getNickName(), results.get(player1.getNickName()) + 1);
                    break;
                case 2:
                    results.replace(player2.getNickName(), results.get(player2.getNickName()) + 1);
                    continue;
            }
            swapPlayers();
        }

        if ((results.get(nickName1) == results.get(nickName2))) {
            log("draw");
            return -1;
        }
        log((results.get(nickName1) > results.get(nickName2) ?
                nickName1 : nickName2) + " wins");
        return (results.get(nickName1) > results.get(nickName2) ? 1 : 2);
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