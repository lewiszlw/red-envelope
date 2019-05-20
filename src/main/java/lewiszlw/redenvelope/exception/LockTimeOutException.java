package lewiszlw.redenvelope.exception;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
public class LockTimeOutException extends RuntimeException {
    public LockTimeOutException(String message) {
        super(message);
    }
}
