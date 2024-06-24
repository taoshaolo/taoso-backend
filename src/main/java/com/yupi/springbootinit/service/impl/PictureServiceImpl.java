package com.yupi.springbootinit.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片服务实现类
 */
@Service
public class PictureServiceImpl implements PictureService {

    @Override
    public Page<Picture> searchPicture(String searchText, long pageNum, long pageSize) {
        //从必应图片搜索页面中获取图片信息
        long current = (pageNum - 1) * pageSize;
        //目标URL
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=%s",searchText,current);
        //建立连接
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        //使用 doc.select() 方法,根据 CSS 选择器 .iuscp.isv 获取页面中所有匹配的元素,并存储在 elements 变量中
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            //取图片地址（murl）
            //使用 element.select(".iusc") 选择器获取包含图片地址信息的元素,并取出其 m 属性的值
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
//            System.out.println(murl);
            //取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
//            System.out.println(title);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
            if (pictures.size() >= pageSize){
                break;
            }
        }
        Page<Picture> picturePage = new Page<>(pageNum,pageNum);
        picturePage.setRecords(pictures);
        return picturePage;
    }
}
