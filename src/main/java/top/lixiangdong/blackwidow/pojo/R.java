package top.lixiangdong.blackwidow.pojo;

import lombok.Builder;
import lombok.Data;
import top.lixiangdong.blackwidow.pojo.enums.ErrorType;

import java.io.PrintWriter;
import java.io.StringWriter;


@Builder
@Data
public class R<T> {
    private String state;
    private ErrorType error;
    private String errorMessage;
    private String errorTrace;
    private T data;

    public static <T> R<T> ok(T data) {
        return new RBuilder<T>().state("ok").data(data).build();
    }

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> error(ErrorType error, Throwable throwable, String errorMessage, T data) {
        StringWriter sw = new StringWriter();
        if (throwable != null) {
            try (PrintWriter pw = new PrintWriter(sw)) {
                throwable.printStackTrace(pw);
            }
        }
        return new RBuilder<T>().state("ok").error(error).errorMessage(errorMessage).errorTrace(sw.toString()).data(data).build();
    }

    public static <T> R<T> error(ErrorType error, Throwable throwable, String errorMessage) {
        return error(error, throwable, errorMessage, null);
    }

    public static <T> R<T> error(ErrorType error, Throwable throwable) {
        return error(error, throwable, null);
    }

}
