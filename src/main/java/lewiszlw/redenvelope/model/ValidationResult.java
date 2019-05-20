package lewiszlw.redenvelope.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Data
@Accessors(chain = true)
public class ValidationResult {

    private boolean pass;

    private String message;

    public WebResponse transformWebResponse() {
        return new WebResponse().setStatus(pass).setMsg(message);
    }

    public static ValidationResult createPassValidationResult() {
        return new ValidationResult().setPass(true).setMessage("验证通过");
    }

    public static ValidationResult createFailValidationResult(String message) {
        return new ValidationResult().setPass(false).setMessage(message);
    }
}
