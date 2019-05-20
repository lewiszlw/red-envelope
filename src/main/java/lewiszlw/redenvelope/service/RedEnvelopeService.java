package lewiszlw.redenvelope.service;

import lewiszlw.redenvelope.constant.EnvelopeStatus;
import lewiszlw.redenvelope.converter.RedEnvelopeConverter;
import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.mapper.RedEnvelopeDetailMapper;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Service
public class RedEnvelopeService {

    @Autowired
    private RedEnvelopeDetailMapper redEnvelopeDetailMapper;

    @Autowired
    private RedEnvelopeRedisService redEnvelopeRedisService;

    public void insertRedEnvelope(CreateEnvelopeReq req) {
        // 插入数据库
        EnvelopeDetailEntity envelopeDetailEntity = RedEnvelopeConverter.convertToEnvelopeDetailEntity(req);
        envelopeDetailEntity.setStatus(EnvelopeStatus.Unexpired);
        envelopeDetailEntity.setRemainMoney(envelopeDetailEntity.getAmount());
        envelopeDetailEntity.setRemainSize(envelopeDetailEntity.getSize());
        redEnvelopeDetailMapper.insertOne(envelopeDetailEntity);
        // 存入redis
        redEnvelopeRedisService.insertRedEnvelope(envelopeDetailEntity);
    }
}
