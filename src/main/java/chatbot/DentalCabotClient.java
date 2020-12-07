package chatbot;

import chatbot.message.Message;
import chatbot.message.TextMessage;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author dzhang
 */
public class DentalCabotClient {

    private final static HttpClient HTTPCLIENT = HttpClients.createDefault();

    private static final String WEB_HOOK_PREFIX = "https://oapi.dingtalk.com/robot/send?access_token=";

    private static SendResult push(String webHook, Message message) throws IOException {
        HttpPost httppost = new HttpPost(webHook);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(message.toJsonString(), "utf-8");
        httppost.setEntity(se);
        SendResult sendResult = new SendResult();
        HttpResponse response = HTTPCLIENT.execute(httppost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(response.getEntity());
            JSONObject obj = JSONObject.parseObject(result);
            Integer errCode = obj.getInteger("errcode");
            sendResult.setErrorCode(errCode);
            sendResult.setErrorMsg(obj.getString("errmsg"));
            sendResult.setSuccess(errCode.equals(0));
        }
        return sendResult;
    }

    public static void send(List<String> tokens, Message message) {
        for (String token : tokens) {
            send(token, message);
        }
    }

    public static SendResult send(String token, Message message, String secret) {
        if (StringUtils.isBlank(secret)) {
            throw new IllegalArgumentException("secret can not be blank");
        }
        String url = signUrl(WEB_HOOK_PREFIX + token, secret);
        try {
            return push(url, message);
        } catch (IOException e) {
            return new SendResult(false, 500, e.getLocalizedMessage());
        }
    }

    public static SendResult send(String token, Message message) {
        try {
            return push(WEB_HOOK_PREFIX + token, message);
        } catch (IOException e) {
            return new SendResult(false, 500, e.getLocalizedMessage());
        }
    }

    private String[] parseToken(String tokens) {
        if (StringUtils.isNotEmpty(tokens)) {
            return tokens.split("\\|");
        }
        return new String[]{};
    }

    /**
     * 签名
     * <p>第一步，把timestamp+"\n"+密钥当做签名字符串，使用HmacSHA256算法计算签名，然后进行Base64 encode，最后再把签名参数再进行urlEncode，得到最终的签名（需要使用UTF-8字符集）。
     * 第二步，把 timestamp和第一步得到的签名值拼接到URL中。
     * https://oapi.dingtalk.com/robot/send?access_token=XXXXXX&timestamp=XXX&sign=XXX
     * </p>
     *
     * @param timestamp 时间戳
     * @param secret    密钥
     */
    public static String sign(Long timestamp, String secret) {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        try {
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        } catch (InvalidKeyException | UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 对 url 处理，加签
     *
     * @param url    url
     * @param secret 密钥
     * @return 处理后的请求地址
     */
    private static String signUrl(String url, String secret) {
        long timestamp = System.currentTimeMillis();
        String sign = sign(timestamp, secret);
        return url + "&timestamp=" + timestamp + "&sign=" + sign;
    }

    /**
     * 测试 main 方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
//        https://oapi.dingtalk.com/robot/send?access_token=6e165a35ab053b64d095b93f5eed225fb0b37986e0034a083381b92da143035a
        SendResult result = DentalCabotClient.send("6e165a35ab053b64d095b93f5eed225fb0b37986e0034a083381b92da143035a",
                new TextMessage("你好~😄"), "SECdbff08950c3eb19c5f8de1a9cdf2b76c4970d82db7e25d79d82cc33e82ae0578");
        System.out.println(result.toString());
    }
}


