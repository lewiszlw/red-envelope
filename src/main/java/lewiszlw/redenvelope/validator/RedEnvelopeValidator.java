package lewiszlw.redenvelope.validator;

import com.google.common.base.Preconditions;
import lewiszlw.redenvelope.constant.Constants;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;
import lewiszlw.redenvelope.model.ValidationResult;
import lewiszlw.redenvelope.util.MoneyUtils;
import org.springframework.util.StringUtils;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
public class RedEnvelopeValidator {

    public static ValidationResult validateCreateEnvelopeReq(CreateEnvelopeReq req) {
        try {
            Preconditions.checkArgument(!StringUtils.isEmpty(req.getUser()), "红包创建者为空");
            Preconditions.checkArgument(req.getType() != null, "红包类型为空");
            Preconditions.checkArgument(req.getAmount() != null, "红包金额为空");
            Preconditions.checkArgument(req.getAmount() <= Constants.ENVELOPE_MAX_AMOUNT && req.getAmount() > 0,
                    String.format("红包金额必须大于0小于%f", MoneyUtils.dbToFE(req.getAmount())));
            Preconditions.checkArgument(req.getSize() != null, "红包份数为空");
            Preconditions.checkArgument(req.getSize() > 0, "红包份数小于等于0");
            Preconditions.checkArgument(req.getSize() * Constants.ENVELOPE_MIN_GRAB_AMOUNT <= req.getAmount(),
                    String.format("红包必须保证每个人至少分得%f", MoneyUtils.dbToFE(Constants.ENVELOPE_MIN_GRAB_AMOUNT)));
            return ValidationResult.createPassValidationResult();
        } catch (IllegalArgumentException e) {
            return ValidationResult.createFailValidationResult(e.getMessage());
        }
    }

    public static ValidationResult validateGrabEnvelopeReqParam(Integer envelopeId, String grabber) {
        try {
            Preconditions.checkArgument(envelopeId != null, "红包id为空");
            Preconditions.checkArgument(envelopeId <= 0, "红包id不正确");
            Preconditions.checkArgument(!StringUtils.isEmpty(grabber), "抢红包者为空");
            return ValidationResult.createPassValidationResult();
        } catch (IllegalArgumentException e) {
            return ValidationResult.createFailValidationResult(e.getMessage());
        }
    }
}
