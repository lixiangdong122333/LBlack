package top.lixiangdong.blackwidow.pojo;

import lombok.Data;

@Data
public class MoveReq {
    private String blackId;
    private Integer currentCoordinate;
    private Integer destinationCoordinate;
}
