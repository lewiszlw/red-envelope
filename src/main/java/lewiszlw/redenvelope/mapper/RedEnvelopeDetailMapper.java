package lewiszlw.redenvelope.mapper;

import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Repository
public interface RedEnvelopeDetailMapper {
    /**
     * 插入一个红包
     */
    Integer insertOne(@Param("envelopeDetailEntity") EnvelopeDetailEntity envelopeDetailEntity);
}
