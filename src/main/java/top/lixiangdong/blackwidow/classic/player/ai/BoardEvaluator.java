package top.lixiangdong.blackwidow.classic.player.ai;

import top.lixiangdong.blackwidow.classic.board.Board;

public interface BoardEvaluator {

    int evaluate(Board board, int depth);

}
