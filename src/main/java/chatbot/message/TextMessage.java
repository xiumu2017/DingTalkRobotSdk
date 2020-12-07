package chatbot.message;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 文本消息
 *
 * @author dustin
 * @date 2017/3/17
 */
@Getter
@Setter
public class TextMessage implements Message {

    private String text;
    private List<String> atMobiles;
    private boolean isAtAll;

    public TextMessage(String text) {
        this.text = text;
    }

    @Override
    public String toJsonString() {
        Map<String, Object> items = new HashMap<>(2);
        items.put("msgtype", "text");

        Map<String, String> textContent = new HashMap<>(4);
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("text should not be blank");
        }
        textContent.put("content", text);
        items.put("text", textContent);

        Map<String, Object> atItems = new HashMap<>(2);
        if (atMobiles != null && !atMobiles.isEmpty()) {
            atItems.put("atMobiles", atMobiles);
        }
        if (isAtAll) {
            atItems.put("isAtAll", true);
        }
        items.put("at", atItems);

        return JSON.toJSONString(items);
    }
}
