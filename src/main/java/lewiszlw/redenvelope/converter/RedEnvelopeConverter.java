package lewiszlw.redenvelope.converter;

import lewiszlw.redenvelope.constant.EnvelopeStatus;
import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.model.redis.EnvelopeRedisModel;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;
import lewiszlw.redenvelope.util.AllocationUtils;

import java.util.ArrayList;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
public class RedEnvelopeConverter {

    public static EnvelopeDetailEntity convertToEnvelopeDetailEntity(CreateEnvelopeReq req) {
        return new EnvelopeDetailEntity().setAmount(req.getAmount())
                                        .setSize(req.getSize())
                                        .setType(req.getType())
                                        .setUser(req.getUser());
    }

    public static EnvelopeRedisModel convertToEnvelopeRedisModel(EnvelopeDetailEntity envelopeDetailEntity) {
        return new EnvelopeRedisModel()
                .setEnvelopeId(envelopeDetailEntity.getId())
                .setAmount(envelopeDetailEntity.getAmount())
                .setSize(envelopeDetailEntity.getSize())
                .setType(envelopeDetailEntity.getType())
                .setRemainMoney(envelopeDetailEntity.getRemainMoney())
                .setRemainSize(envelopeDetailEntity.getRemainSize())
                .setAllocations(AllocationUtils.allocate(envelopeDetailEntity.getAmount(), envelopeDetailEntity.getSize()))
                .setGrabbingDetails(new ArrayList<>())
                .setStatus(EnvelopeStatus.Unexpired);
    }

}
