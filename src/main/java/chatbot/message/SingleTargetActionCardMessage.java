package chatbot.message;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dustin
 * @date 2017/3/19
 */
@Getter
@Setter
public class SingleTargetActionCardMessage implements Message {
    private String title;

    private String bannerUrl;
    private String briefTitle;
    private String briefText;

    private String singleTitle;
    private String singleUrl;

    private boolean hideAvatar;

    @Override
    public String toJsonString() {
        if (StringUtils.isBlank(singleTitle)) {
            throw new IllegalArgumentException("singleTitle should not be blank");
        }
        if (StringUtils.isBlank(singleUrl)) {
            throw new IllegalArgumentException("singleURL should not be blank");
        }

        Map<String, Object> items = new HashMap<>(2);
        items.put("msgtype", "actionCard");

        Map<String, Object> actionCardContent = new HashMap<>();
        actionCardContent.put("title", title);

        StringBuilder text = new StringBuilder();
        if (StringUtils.isNotBlank(bannerUrl)) {
            text.append(MarkdownMessage.getImageText(bannerUrl)).append("\n");
        }
        if (StringUtils.isNotBlank(briefTitle)) {
            text.append(MarkdownMessage.getHeaderText(3, briefTitle)).append("\n");
        }
        if (StringUtils.isNotBlank(briefText)) {
            text.append(briefText).append("\n");
        }
        actionCardContent.put("text", text.toString());

        if (hideAvatar) {
            actionCardContent.put("hideAvatar", "1");
        }

        actionCardContent.put("singleTitle", singleTitle);
        actionCardContent.put("singleURL", singleUrl);

        items.put("actionCard", actionCardContent);

        return JSON.toJSONString(items);
    }
}
