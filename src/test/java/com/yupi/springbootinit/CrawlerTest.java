package com.yupi.springbootinit;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author taoshao
 * @Date 2024/5/22
 */
@SpringBootTest
public class CrawlerTest {
    @Resource
    private PostService postService;

    @Test
    void testPictureByBaidu() throws IOException {
        String url = "https://www.3gbizhi.com/search/0-0-1-0/动漫";//手机壁纸
//        String url = "https://www.3gbizhi.com/search/0-0-2-0/动漫";//桌面壁纸壁纸

        Document doc = Jsoup.connect(url).get();
//        System.out.println(doc);
        Elements elements = doc.select(".box_black");
        List<Wallpaper> wallpapers = new ArrayList<>();
        for (Element element : elements) {
            //图片
            String href = element.select(".imgw").attr("href");
            //标题
            String text = element.select(".title").text();
//            System.out.println(href);
//            System.out.println(text);
            Wallpaper wallpaper = new Wallpaper();
            wallpaper.setTitle(text);
            wallpaper.setUrl(href);
            wallpapers.add(wallpaper);
        }
        System.out.println(wallpapers);
    }

    @Test
    void testPictureByBing() throws IOException {
        //从必应图片搜索页面中获取图片信息
        int current = 1;
        //目标URL
        String url = "https://cn.bing.com/images/search?q=小黑子&first=" + current;
        //建立连接
        Document doc = Jsoup.connect(url).get();
        //使用 doc.select() 方法,根据 CSS 选择器 .iuscp.isv 获取页面中所有匹配的元素,并存储在 elements 变量中
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            //取图片地址（murl）
            //使用 element.select(".iusc") 选择器获取包含图片地址信息的元素,并取出其 m 属性的值
            String m = element.select(".iusc").get(0).attr("m");
            Map<String,Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
//            System.out.println(murl);
            //取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
//            System.out.println(title);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
        }
        System.out.println(pictures);
    }

    @Test
    void testFetchPassage() {
        //1.获取数据
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"tags\":[],\"reviewStatus\":1}";
        String url = "https://api.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute().body();
        System.out.println(result);
        //2.json 转 对象
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");

        List<Post> postList = new ArrayList<>();

        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));

            String content = tempRecord.getStr("content");
            int maxContentLength = 1000; // 假设数据库 content 字段最大长度为 1000
            if (content.length() > maxContentLength) {
                content = content.substring(0, maxContentLength);
            }
            post.setContent(content);

            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            postList.add(post);
        }
        System.out.println(postList);
        //3.数据入库
        boolean b = postService.saveBatch(postList);//批量保存
        Assertions.assertTrue(b);//断言语句
    }

    private class Wallpaper {
        private String title;
        private String url;

        public Wallpaper() {
        }

        public Wallpaper(String title, String url) {
            this.title = title;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Wallpaper{" +
                    "title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
