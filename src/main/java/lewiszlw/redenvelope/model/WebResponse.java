package lewiszlw.redenvelope.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Data
@Accessors(chain = true)
public class WebResponse {

    /**
     * 状态
     */
    private boolean status;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private Object data;

    public static WebResponse createSuccessWebResponse() {
        return new WebResponse().setStatus(true).setMsg("请求成功");
    }

    public static WebResponse createSuccessWebResponse(Object data) {
        return new WebResponse().setStatus(true).setMsg("请求成功").setData(data);
    }

    public static WebResponse createFailWebResponse(Object data) {
        return new WebResponse().setStatus(false).setMsg("请求失败").setData(data);
    }

    public static WebResponse createFailWebResponse(String msg, Object data) {
        return new WebResponse().setStatus(false).setMsg(msg).setData(data);
    }
}
