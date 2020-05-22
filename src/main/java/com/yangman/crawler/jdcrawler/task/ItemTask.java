package com.yangman.crawler.jdcrawler.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yangman.crawler.jdcrawler.pojo.Item;
import com.yangman.crawler.jdcrawler.service.ItemService;
import com.yangman.crawler.jdcrawler.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author: Felix Yang (yangman)
 * @create: 2020-05-18 19:45
 * @description:
 **/

@Component
public class ItemTask {


    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private ItemService itemService;

    private final ObjectMapper MAPPER = new ObjectMapper();


    // 当一个下载任务完成后，间隔多长时间进行下一次的任务，单位毫秒
    @Scheduled(fixedDelay = 100 * 1000)
    public void itemTask() throws Exception {
        String url = "https://search.jd.com/Search?keyword=%E7%AC%94%E8%AE%B0%E6%9C%AC%E7%94%B5%E8%84%91&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&s=54&click=0&page=";

        // 按照页面对手机的搜索结果进行
        for (int i = 1; i < 50; i += 2) {
            String html = httpUtils.doGetHtml(url + i);
            this.parse(html);
        }

        System.out.println("数据抓取完成！");
    }

    /**
     * 解析页面数据
     *
     * @param html
     */
    private void parse(String html) throws JsonProcessingException {
        Document doc = Jsoup.parse(html);

        // 获取商品信息
        Elements elems = doc.select("div#J_goodsList > ul > li");
        for (Element elem : elems) {

            // 获取sku
            long sku = Long.parseLong(elem.attr("data-sku"));

            // 利用sku进行去重
            Item item = new Item();
            item.setSku(sku);
            List<Item> itemList = this.itemService.findAll(item);
            if (itemList.size() > 0) {
                continue;
            }
            // 获取spu
            String itemSpu = elem.attr("data-spu");
            if (!"".equals(itemSpu)) {
                long spu = Long.parseLong(itemSpu);
                item.setSpu(spu);
            }

            // 获取商品的详情的url
            String itemUrl = "https://item.jd.com/" + sku + ".html";
            item.setUrl(itemUrl);

            // 获取商品的图片
            String picSrc  = elem.select("img").first().attr("src");
            if(StringUtils.isEmpty(picSrc)){
                picSrc = elem.select("img").attr("source-data-lazy-img");
            }
            String picUrl = "https:" + picSrc;
            picUrl = picUrl.replace("/n7/", "/n1/");
            String picName = this.httpUtils.doGetImage(picUrl);
            item.setPic(picName);

            // 获取商品价格 https://p.3.cn/prices/mgets?skuIds=J_100013171828
            String priceJson = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + sku);
            double itemPrice = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
            item.setPrice(itemPrice);

            // 获取商品标题
//            String title = info.select(".p-name").text();
            String itemInfo = this.httpUtils.doGetHtml(item.getUrl());
            String title = Jsoup.parse(itemInfo).select("div.sku-name").text();
            item.setTitle(title);

            // 商品的时间
            item.setCreated(new Date());
            item.setUpdated(item.getCreated());

            // 保存到数据库中
            this.itemService.save(item);
        }


    }
}
