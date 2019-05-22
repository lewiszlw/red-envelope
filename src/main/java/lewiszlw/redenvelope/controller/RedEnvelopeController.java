package lewiszlw.redenvelope.controller;

import lewiszlw.redenvelope.annotation.RateLimit;
import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.entity.EnvelopeGrabberEntity;
import lewiszlw.redenvelope.mapper.RedEnvelopeDetailMapper;
import lewiszlw.redenvelope.mapper.RedEnvelopeGrabberMapper;
import lewiszlw.redenvelope.model.GrabbingResult;
import lewiszlw.redenvelope.model.ValidationResult;
import lewiszlw.redenvelope.model.VerifyResult;
import lewiszlw.redenvelope.model.WebResponse;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;
import lewiszlw.redenvelope.service.RedEnvelopeRedisService;
import lewiszlw.redenvelope.service.RedEnvelopeService;
import lewiszlw.redenvelope.validator.RedEnvelopeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@RestController
@RequestMapping("/red-envelope")
public class RedEnvelopeController {

    @Autowired
    private RedEnvelopeService redEnvelopeService;

    @Autowired
    private RedEnvelopeRedisService redEnvelopeRedisService;

    @Autowired
    private RedEnvelopeDetailMapper redEnvelopeDetailMapper;

    @Autowired
    private RedEnvelopeGrabberMapper redEnvelopeGrabberMapper;

    /**
     * 创建红包
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public WebResponse createEnvelope(@RequestBody  CreateEnvelopeReq req) {
        // 验收请求参数
        ValidationResult validationResult = RedEnvelopeValidator.validateCreateEnvelopeReq(req);
        if (!validationResult.isPass()) {
            return validationResult.transformWebResponse();
        }
        // 创建红包并存入redis
        Integer envelopeId = redEnvelopeService.insertRedEnvelope(req);
        return WebResponse.createSuccessWebResponse(envelopeId);
    }

    /**
     * 抢红包
     */
    @RateLimit
    @RequestMapping("/grab")
    public WebResponse grabEnvelope(@RequestParam(value = "envelopeId") Integer envelopeId,
                                    @RequestParam(value = "grabber") String grabber) {
        // 验收请求参数
        ValidationResult validationResult = RedEnvelopeValidator.validateGrabEnvelopeReqParam(envelopeId, grabber);
        if (!validationResult.isPass()) {
            return validationResult.transformWebResponse();
        }
        // 抢红包
        GrabbingResult grabbingResult = redEnvelopeService.grabRedEnvelope(envelopeId, grabber);
        return grabbingResult.transformWebResponse();
    }

    /**
     * 清除redis缓存
     */
    @RequestMapping("/redis/del")
    public WebResponse redisDel(@RequestParam("envelopeIds") List<Integer> envelopeIds) {
        if (CollectionUtils.isEmpty(envelopeIds)) {
            return WebResponse.createSuccessWebResponse();
        }
        envelopeIds.forEach(envelopeId -> redEnvelopeRedisService.del(envelopeId));
        return WebResponse.createSuccessWebResponse();
    }

    /**
     * 验证红包发放金额数量的正确性
     */
    @RequestMapping("/verify")
    public WebResponse verify(@RequestParam("envelopeIds") List<Integer> envelopeIds) {
        if (CollectionUtils.isEmpty(envelopeIds)) {
            return WebResponse.createSuccessWebResponse();
        }
        List<VerifyResult> failVerifyResult = new ArrayList<>();
        for (Integer envelopeId : envelopeIds) {
            EnvelopeDetailEntity envelopeDetailEntity = redEnvelopeDetailMapper.selectOne(envelopeId);
            List<EnvelopeGrabberEntity> envelopeGrabberEntities = redEnvelopeGrabberMapper.selectByEnvelopeId(envelopeId);
            VerifyResult verifyResult = RedEnvelopeValidator.verifyEntity(envelopeId, envelopeDetailEntity, envelopeGrabberEntities);
            if (!verifyResult.isPass()) {
                failVerifyResult.add(verifyResult);
            }
        }
        if (CollectionUtils.isEmpty(failVerifyResult)) {
            return WebResponse.createSuccessWebResponse();
        } else {
            return WebResponse.createFailWebResponse("验证失败", failVerifyResult);
        }
    }

}
