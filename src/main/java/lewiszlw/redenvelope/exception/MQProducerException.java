package lewiszlw.redenvelope.exception;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-17
 */
public class MQProducerException extends RuntimeException {

    public MQProducerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
