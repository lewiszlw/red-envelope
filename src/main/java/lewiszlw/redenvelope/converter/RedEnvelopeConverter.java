package lewiszlw.redenvelope.converter;

import lewiszlw.redenvelope.constant.ExistentStatus;
import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.entity.EnvelopeGrabberEntity;
import lewiszlw.redenvelope.model.redis.EnvelopeRedisModel;
import lewiszlw.redenvelope.model.redis.GrabbingDetail;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;
import lewiszlw.redenvelope.util.AllocationUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
                .setExistentStatus(ExistentStatus.EXISTENT)
                .setAmount(envelopeDetailEntity.getAmount())
                .setSize(envelopeDetailEntity.getSize())
                .setType(envelopeDetailEntity.getType())
                .setRemainMoney(envelopeDetailEntity.getRemainMoney())
                .setRemainSize(envelopeDetailEntity.getRemainSize())
                // 分配金额
                .setAllocations(AllocationUtils.allocate(envelopeDetailEntity.getRemainMoney(), envelopeDetailEntity.getRemainSize()))
                .setGrabbingDetails(new ArrayList<>());
    }

    public static EnvelopeRedisModel convertToEnvelopeRedisModel(EnvelopeDetailEntity envelopeDetailEntity,
                                                                 List<EnvelopeGrabberEntity> envelopeGrabberEntities) {
        EnvelopeRedisModel envelopeRedisModel = convertToEnvelopeRedisModel(envelopeDetailEntity);

        List<GrabbingDetail> grabbingDetails = new ArrayList<>();
        if (!CollectionUtils.isEmpty(envelopeGrabberEntities)) {
            for (EnvelopeGrabberEntity envelopeGrabberEntity: envelopeGrabberEntities) {
                grabbingDetails.add(new GrabbingDetail()
                        .setGrabber(envelopeGrabberEntity.getGrabber())
                        .setAmount(envelopeDetailEntity.getAmount())
                );
            }
        }
        envelopeRedisModel.setGrabbingDetails(grabbingDetails);

        return envelopeRedisModel;
    }

}
