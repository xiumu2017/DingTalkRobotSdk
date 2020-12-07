package chatbot.message;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author dustin
 * @date 2017/3/17
 */
@Getter
@Setter
public class ActionCardMessage implements Message {
    /**
     * 最大按钮数量
     */
    public static final int MAX_ACTION_BUTTON_CNT = 5;
    /**
     * 最小按钮数量
     */
    public static final int MIN_ACTION_BUTTON_CNT = 1;

    /**
     * 标题
     */
    private String title;
    /**
     * 跳转路径
     */
    private String bannerUrl;
    private String briefTitle;
    private String briefText;
    private boolean hideAvatar;
    private ActionButtonStyle actionButtonStyle = ActionButtonStyle.VERTICAL;
    private final List<ActionCardAction> actions = new ArrayList<>();

    /**
     * 添加按钮
     *
     * @param action {@link ActionCardAction}
     */
    public void addAction(ActionCardAction action) {
        if (actions.size() >= MAX_ACTION_BUTTON_CNT) {
            throw new IllegalArgumentException("number of actions can't more than " + MAX_ACTION_BUTTON_CNT);
        }
        actions.add(action);
    }

    @Override
    public String toJsonString() {

        Map<String, Object> items = new HashMap<>();
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

        if (actions.size() < MIN_ACTION_BUTTON_CNT) {
            throw new IllegalArgumentException("number of actions can't less than " + MIN_ACTION_BUTTON_CNT);
        }
        actionCardContent.put("btns", actions);

        if (actions.size() == 2 && actionButtonStyle == ActionButtonStyle.HORIZONTAL) {
            actionCardContent.put("btnOrientation", "1");
        }

        items.put("actionCard", actionCardContent);

        return JSON.toJSONString(items);
    }
}
