package top.lixiangdong.blackwidow.pojo;

import lombok.Data;
import top.lixiangdong.blackwidow.pojo.enums.PlayerType;

@Data
public class JoinBlackReq {
    private String blackId;
    //black or white
    private PlayerType playerType;
}
