package cn.itcast.jd.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class HttpUtils {

    private PoolingHttpClientConnectionManager cm;
    public HttpUtils(){
        this.cm = new PoolingHttpClientConnectionManager();
        // 设置最大连接数
        this.cm.setMaxTotal(100);
        // 设置每个主机的最大连接数
        this.cm.setDefaultMaxPerRoute(10);
    }
    /**
     * 根据地址下载数据
     *
     * @param url
     * @return
     */
    public String doGetHtml(String url){
        // 1.获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();
        // 2.创建httpGet请求对象,设置url地址
        HttpGet httpGet = new HttpGet(url);
        // 加上请求头
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
        // 设置请求信息
        httpGet.setConfig(this.getConfig());

        // 3.使用HttpClient发起请求,获取响应
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            // 4.解析响应,返回结果
            if(response.getStatusLine().getStatusCode() == 200){
                // 判断Entity是否不为空
                if(response.getEntity() != null){
                    String content = EntityUtils.toString(response.getEntity(),"UTF-8");
                    // 整个页面的内容
//                    System.out.println(content);
                    return content;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close response
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1000)
                .setConnectionRequestTimeout(500)
                .setSocketTimeout(10000)
                .build();
        return config;
    }

    public String doGetImg(String url){
        // 1.获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();
        // 2.创建httpGet请求对象,设置url地址
        HttpGet httpGet = new HttpGet(url);
        // 增加请求头
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");

//        作者：IT程序员小松
//        链接：https://juejin.im/post/6844903902178082829
//        来源：掘金
//        著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
        // 设置请求信息
        httpGet.setConfig(this.getConfig());

        // 3.使用HttpClient发起请求,获取响应
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            // 4.解析响应,返回结果
            if(response.getStatusLine().getStatusCode() == 200){
                // 判断Entity是否不为空
                if(response.getEntity() != null){
                    // 下载图片
                    // 获取图片后缀
                    String extName = url.substring(url.lastIndexOf("."));
                    // 创建图片名,重命名
                    String picName = UUID.randomUUID().toString() + extName;
                    // 下载图片
                    // 声明OutputStream
                    OutputStream outputStream = new FileOutputStream(new File("E:\\爬取的图片\\"+picName));
                    response.getEntity().writeTo(outputStream);
                    // 返回图片名称
                    return picName;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close response
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
