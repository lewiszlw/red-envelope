package lewiszlw.redenvelope.converter;

import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;

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


}
