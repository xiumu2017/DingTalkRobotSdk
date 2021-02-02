package chatbot;

import chatbot.message.LinkMessage;
import chatbot.message.MarkdownMessage;
import chatbot.message.Message;
import chatbot.message.TextMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class DingTalkClientTest {

    String token = "f730abe32503ce2863865b16ef66e15feca779ba8ce005bce155edb4deef8719";
    String secret = "SEC5cc60451651b6a292d27450c1983362cbbc32aa776dd46f5839f799a1cf4dbcc";
    Message defaultMsg = new TextMessage("Hello,World~");

    @Test
    void sendHelloWorld() {
        SendResult result = DingTalkClient.send(token, defaultMsg, secret);
        System.out.println(result.toString());
        Assertions.assertTrue(result.isSuccess());
    }

    @Test
    void sendLinkMsg() {
        LinkMessage linkMsg = new LinkMessage();
        linkMsg.setTitle("链接消息类型的标题");
        linkMsg.setText("链接消息类型的文本内容");
        linkMsg.setMessageUrl("https://www.yuque.com/inuter/bc7ikc");
        linkMsg.setPicUrl("https://static001.geekbang.org/resource/image/8a/3e/8a71a295ecd26449d19ec562ea8c863e.jpg");
        SendResult result = DingTalkClient.send(token, linkMsg, secret);
        Assertions.assertTrue(result.isSuccess());
    }

    @Test
    void sendMd() {
        MarkdownMessage message = new MarkdownMessage();
        message.setTitle("这个是整个消息的标题");
        message.add(MarkdownMessage.getHeaderText(1, "一级标题"));
        message.add(MarkdownMessage.getHeaderText(2, "②级标题"));
        message.add(MarkdownMessage.getHeaderText(3, "三级标题"));
        message.add(MarkdownMessage.getHeaderText(4, "④级标题"));
        message.add(MarkdownMessage.getHeaderText(5, "55级标题"));
        message.add(MarkdownMessage.getHeaderText(6, "⑥⑥⑥级标题"));

        message.add(MarkdownMessage.getBoldText("加粗文本"));
        message.add(MarkdownMessage.getItalicText("斜体文本"));
        message.add(MarkdownMessage.getReferenceText("这里是引用的文本"));

        List<String> orderList = new ArrayList<>();
        orderList.add("有序列表1");
        orderList.add("有序列表2");
        orderList.add("有序列表3");
        message.add(MarkdownMessage.getOrderListText(orderList));

        List<String> unList = new ArrayList<>();
        orderList.add("无序列表1");
        orderList.add("无序列表2");
        orderList.add("无序列表3");
        message.add(MarkdownMessage.getUnOrderListText(unList));

        message.add(MarkdownMessage.getLinkText("这个是链接", "https://www.yuque.com/inuter/bc7ikc"));
        message.add(MarkdownMessage.getImageText("https://static001.geekbang.org/resource/image/8a/3e/8a71a295ecd26449d19ec562ea8c863e.jpg"));

        SendResult result = DingTalkClient.send(token, message, secret);
        System.out.println(result.toString());
        Assertions.assertTrue(result.isSuccess());
    }

}