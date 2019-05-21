package lewiszlw.redenvelope.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-21
 */
@Data
@Accessors(chain = true)
public class VerifyResult extends ValidationResult {

    private Integer envelopeId;

    public static VerifyResult createPassVerifyResult(Integer envelopeId) {
        return (VerifyResult) new VerifyResult().setEnvelopeId(envelopeId).setPass(true);
    }

    public static VerifyResult createFailVerifyResult(Integer envelopeId, String message) {
        return (VerifyResult) new VerifyResult().setEnvelopeId(envelopeId).setMessage(message).setPass(false);
    }
}
