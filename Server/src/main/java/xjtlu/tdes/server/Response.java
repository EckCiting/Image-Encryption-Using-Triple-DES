package xjtlu.tdes.server;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response<T> {
    private Integer status;
    private String message;
    private T data;

    public static Response<?> ok() {
        return Response.builder().status(0).message("Success").data(new ArrayList<>())
                .build();
    }

    public static Response<?> ok(Object data) {
        return Response.builder().status(0).message("Success").data(data).build();
    }


    public static Response<?> ok(String message, Object data) {
        return Response.builder().status(0).message(message).data(data).build();
    }

    public static Response<?> exceptionHandling(int statusCode, String message,Object data) {
        return Response.builder().status(statusCode).message(message).data(data).build();
    }

    public static Response<?> exceptionHandling(int statusCode, String message) {
        return Response.builder().status(statusCode).message(message).build();
    }

}