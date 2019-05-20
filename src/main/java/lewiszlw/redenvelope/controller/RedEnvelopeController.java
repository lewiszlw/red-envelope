package lewiszlw.redenvelope.controller;

import lewiszlw.redenvelope.model.WebResponse;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;
import lewiszlw.redenvelope.model.ValidationResult;
import lewiszlw.redenvelope.service.RedEnvelopeService;
import lewiszlw.redenvelope.service.RedEnvelopeRedisService;
import lewiszlw.redenvelope.validator.RedEnvelopeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 创建红包
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public WebResponse createEnvelope(CreateEnvelopeReq req) {
        // 验收请求参数
        ValidationResult validationResult = RedEnvelopeValidator.validateCreateEnvelopeReq(req);
        if (!validationResult.isPass()) {
            return validationResult.transformWebResponse();
        }
        // 创建红包并存入redis
        redEnvelopeService.insertRedEnvelope(req);
        return WebResponse.createSuccessWebResponse();
    }

    /**
     * 抢红包
     */
    @RequestMapping("/grab")
    public WebResponse grabEnvelope(@RequestParam(value = "envelopeId") Integer envelopeId) {
        // redis查看红包剩余
        return null;
    }
}
