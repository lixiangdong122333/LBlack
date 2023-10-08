package top.lixiangdong.blackwidow.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.lixiangdong.blackwidow.classic.board.Board;
import top.lixiangdong.blackwidow.classic.board.Move;
import top.lixiangdong.blackwidow.classic.board.MoveTransition;
import top.lixiangdong.blackwidow.classic.pieces.Piece;
import top.lixiangdong.blackwidow.pojo.JoinBlackReq;
import top.lixiangdong.blackwidow.pojo.MoveReq;
import top.lixiangdong.blackwidow.pojo.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Slf4j
public class BlackController {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @PostMapping("/create-black")
    public R<String> createBlack() {
        String blackId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("black:board:" + blackId, Board.createStandardBoard(), 1, TimeUnit.DAYS);
        return R.ok(blackId);
    }

    @PostMapping("/join-black")
    public R<List<Piece>> joinBlack(@RequestBody JoinBlackReq joinBlackReq) {
        Board black = (Board) redisTemplate.opsForValue().get("black:board:" + joinBlackReq.getBlackId());
        Collection<Piece> whitePieces = new ArrayList<>();
        Collection<Piece> blackPieces = new ArrayList<>();
        if (black != null) {
            whitePieces = black.getWhitePieces();
            blackPieces = black.getBlackPieces();
        }
        List<Piece> collect = Stream.of(whitePieces, blackPieces).flatMap(Collection::stream).collect(Collectors.toList());
        return R.ok(collect);
    }

    @PostMapping("/move")
    public R<List<Piece>> move(@RequestBody MoveReq moveReq) {
        Board black = (Board) redisTemplate.opsForValue().get("black:board:" + moveReq.getBlackId());
        //TODO:Check if the chessboard exists
        Move move = Move.MoveFactory.createMove(black, moveReq.getCurrentCoordinate(), moveReq.getDestinationCoordinate());

        MoveTransition moveTransition = black.currentPlayer().makeMove(move);
        if (moveTransition.getMoveStatus().isDone()) {
            log.info("\n" + moveTransition.getToBoard());
            redisTemplate.opsForValue().set("black:board:" + moveReq.getBlackId(), moveTransition.getToBoard(), 1, TimeUnit.DAYS);

            log.info("move ok");
        }
        Collection<Piece> whitePieces = new ArrayList<>();
        Collection<Piece> blackPieces = new ArrayList<>();
        if (black != null) {
            whitePieces = black.getWhitePieces();
            blackPieces = black.getBlackPieces();
        }
        List<Piece> collect = Stream.of(whitePieces, blackPieces).flatMap(Collection::stream).collect(Collectors.toList());
        return R.ok(collect);
    }
}
