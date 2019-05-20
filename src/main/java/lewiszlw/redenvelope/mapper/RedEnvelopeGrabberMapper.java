package lewiszlw.redenvelope.mapper;

import lewiszlw.redenvelope.entity.EnvelopeGrabberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Repository
@Mapper
public interface RedEnvelopeGrabberMapper {

    Integer insertOne(@Param("envelopeGrabberEntity") EnvelopeGrabberEntity envelopeGrabberEntity);

    List<EnvelopeGrabberEntity> selectByEnvelopeId(@Param("envelopeId") Integer envelopeId);
}
