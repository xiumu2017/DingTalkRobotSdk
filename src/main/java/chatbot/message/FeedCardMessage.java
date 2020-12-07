package chatbot.message;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dustin
 * @date 2017/3/19
 */
@Getter
@Setter
public class FeedCardMessage implements Message {

    private List<FeedCardMessageItem> feedItems;

    @Override
    public String toJsonString() {
        Map<String, Object> items = new HashMap<>(2);
        items.put("msgtype", "feedCard");

        if (feedItems == null || feedItems.isEmpty()) {
            throw new IllegalArgumentException("feedItems should not be null or empty");
        }

        for (FeedCardMessageItem item : feedItems) {
            if (StringUtils.isBlank(item.getTitle())) {
                throw new IllegalArgumentException("title should not be blank");
            }
            if (StringUtils.isBlank(item.getMessageUrl())) {
                throw new IllegalArgumentException("messageURL should not be blank");
            }
            if (StringUtils.isBlank(item.getPicUrl())) {
                throw new IllegalArgumentException("picURL should not be blank");
            }
        }
        Map<String, Object> feedCard = new HashMap<>(feedItems.size());
        feedCard.put("links", feedItems);
        items.put("feedCard", feedCard);

        return JSON.toJSONString(items);
    }
}
