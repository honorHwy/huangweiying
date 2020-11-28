package cn.itcast.jd.task;

import cn.itcast.jd.pojo.Item;
import cn.itcast.jd.service.ItemService;
import cn.itcast.jd.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class ItemTask {

    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private ItemService itemService;
    private static final ObjectMapper MAPPER = new ObjectMapper();


    // 下载任务完成后,间隔多长时间进行下一次的任务
    @Scheduled(fixedDelay = 100*1000)
    public void itemTask() throws Exception {
        // 声明需要解析的初始地址
         String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&suggest=2.def.0.base&wq=%E6%89%8B%E6%9C%BA&s=57&click=0&page=";
        // 遍历,按照页码,对手机的搜索结果进行解析
        for (int i = 1;i < 3;i = i+2){
            String html = httpUtils.doGetHtml(url);
            // 解析页面
            this.parse(html);
        }
        System.out.println("Successful");
    }

    private void parse(String html) throws IOException {
        // 解析html,获取dom
        Document doc = Jsoup.parse(html);
        // 获取spu
        Elements spuEles= doc.select("div#J_goodsList >ul > li");
        for (Element spuEle : spuEles){
            // 获取spu
            Long spu = Long.parseLong(spuEle.attr("data-sku"));
            // 获取sku
            Elements skuEles = spuEle.select("li.ps-item");
            for (Element skuEls : skuEles){
                Long sku = Long.parseLong(skuEles.select("[data-sku]").attr("data-sku"));
                // 根据sku查询商品数据
                Item item = new Item();
                item.setSku(sku);
                List<Item> list = this.itemService.findAll(item);
                if(list.size() > 0){
                    // 去重
                    continue;
                }
                // 设置商品的spu
                item.setSpu(spu);
                // 获取商品的详情的url
                String itemUrl = "https://item.jd.com/"+ sku +".html";
                item.setUrl(itemUrl);
                // 图片
                String picUrl = "https:" + skuEles.select("img[data-sku]").first().attr("data-lazy-img");
                picUrl = picUrl.replace("/n9/","/n1/");
                picUrl = picUrl.replace("/n8/","/n1/");
                picUrl = picUrl.replace("/n7/","/n1/");
                picUrl = picUrl.replace("/n6/","/n1/");
                picUrl = picUrl.replace("/n5/","/n1/");
                picUrl = picUrl.replace("/n4/","/n1/");
                picUrl = picUrl.replace("/n3/","/n1/");
                picUrl = picUrl.replace("/n2/","/n1/");
                String picName = this.httpUtils.doGetImg(picUrl);
                item.setPic(picUrl);

                // 获取价格  [{"p":"1799.00","op":"1799.00","cbf":"0","id":"J_100005769525","m":"10000.00"}] 查看xhr请求
                String priceJson = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + sku);
                double price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
                item.setPrice(price);
                // 标题
                String itemInfo = this.httpUtils.doGetHtml(item.getUrl());
                String title = Jsoup.parse(itemInfo).select("div.sku-name").text();

                item.setTitle(title);
                item.setCreated(new Date());
                item.setUpdated(item.getCreated());
                this.itemService.save(item);
            }
        }
    }
}
