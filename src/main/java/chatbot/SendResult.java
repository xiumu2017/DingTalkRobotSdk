package chatbot;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送结果
 *
 * @author dustin
 * @date 2017/3/17
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendResult {
    private boolean isSuccess;
    private Integer errorCode;
    private String errorMsg;

    @Override
    public String toString() {
        Map<String, Object> items = new HashMap<>(3);
        items.put("errorCode", errorCode);
        items.put("errorMsg", errorMsg);
        items.put("isSuccess", isSuccess);
        return JSON.toJSONString(items);
    }
}
