package lewiszlw.redenvelope.validator;

import com.google.common.base.Preconditions;
import lewiszlw.redenvelope.constant.Constants;
import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.entity.EnvelopeGrabberEntity;
import lewiszlw.redenvelope.model.VerifyResult;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;
import lewiszlw.redenvelope.model.ValidationResult;
import lewiszlw.redenvelope.util.MoneyUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

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
            Preconditions.checkArgument(envelopeId > 0, "红包id不正确");
            Preconditions.checkArgument(!StringUtils.isEmpty(grabber), "抢红包者为空");
            return ValidationResult.createPassValidationResult();
        } catch (IllegalArgumentException e) {
            return ValidationResult.createFailValidationResult(e.getMessage());
        }
    }

    public static VerifyResult verifyEntity(Integer envelopeId,
                                            EnvelopeDetailEntity envelopeDetailEntity,
                                            List<EnvelopeGrabberEntity> envelopeGrabberEntities) {
        try {
            Preconditions.checkState(null != envelopeDetailEntity, "envelopeDetailEntity为空");
            Preconditions.checkState(envelopeDetailEntity.getRemainMoney() <= envelopeDetailEntity.getAmount(), "红包余额大于总额");
            Preconditions.checkState(envelopeDetailEntity.getRemainSize() <= envelopeDetailEntity.getSize(), "红包余份大于总份数");

            Integer allocatedAmount = 0;
            Integer allocatedSize = 0;
            if (!CollectionUtils.isEmpty(envelopeGrabberEntities)) {
                allocatedSize = envelopeGrabberEntities.size();
                for (EnvelopeGrabberEntity envelopeGrabberEntity : envelopeGrabberEntities) {
                    allocatedAmount += envelopeGrabberEntity.getMoney();
                }
            }

            Preconditions.checkState(allocatedAmount + envelopeDetailEntity.getRemainMoney() == envelopeDetailEntity.getAmount(),
                    "分配出去的金额 + 剩余金额 != 总金额");
            Preconditions.checkState(allocatedSize + envelopeDetailEntity.getRemainSize() == envelopeDetailEntity.getSize(),
                    "分配出去的份数 + 剩余份数 != 总份数");

            return VerifyResult.createPassVerifyResult(envelopeId);
        } catch (Exception e) {
            return VerifyResult.createFailVerifyResult(envelopeId, e.getMessage());
        }
    }
}
