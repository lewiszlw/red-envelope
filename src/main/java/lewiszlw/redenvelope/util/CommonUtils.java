package lewiszlw.redenvelope.util;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Predicate;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-21
 */
public class CommonUtils {

    /**
     * 在list中查找满足条件的一项
     * @param list
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> T find(List<T> list, Predicate<T> predicate) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        for (T t: list) {
            if (predicate.test(t)) {
                return t;
            }
        }
        return null;
    }
}
