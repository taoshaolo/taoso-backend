package com.yupi.springbootinit.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取初始帖子列表
 *
 * @Author taoshao
 * @Date 2024/5/22
 */
//取消注释后：每次肩动springboot项目时 会执行一次run方法
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) throws Exception {
        //1.获取数据
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"tags\":[],\"reviewStatus\":1}";
        String url = "https://api.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute().body();
        System.out.println(result);
        //2.json 转 对象
        Map<String,Object> map = JSONUtil.toBean(result,Map.class);
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
        if (b){
            log.info("获取初始化帖子列表成功，条数：{}",postList.size());
        }else {
            log.error("获取初始化帖子列表失败");
        }
    }
}
