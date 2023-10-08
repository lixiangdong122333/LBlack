package top.lixiangdong.blackwidow.classic.player.ai;

import top.lixiangdong.blackwidow.classic.board.Board;
import top.lixiangdong.blackwidow.classic.board.Move;

public interface MoveStrategy {

    long getNumBoardsEvaluated();

    Move execute(Board board);

}
