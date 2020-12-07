package chatbot.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dustin
 * @date 2017/3/19
 */
@Getter
@Setter
@AllArgsConstructor
public class FeedCardMessageItem {
    private String title;
    private String picUrl;
    private String messageUrl;
}