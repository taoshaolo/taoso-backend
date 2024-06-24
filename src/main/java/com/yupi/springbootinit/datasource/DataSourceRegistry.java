package com.yupi.springbootinit.datasource;

import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author taoshao
 * @Date 2024/5/24
 */
@Component
public class DataSourceRegistry {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    Map<String,DataSource> typeDataSourceMap;

    @PostConstruct
    public void doInit(){
        typeDataSourceMap = new HashMap(){{
            put(SearchTypeEnum.POST.getValue(),postDataSource);
            put(SearchTypeEnum.USER.getValue(),userDataSource);
            put(SearchTypeEnum.PICTURE.getValue(),pictureDataSource);
        }};

    }



    public DataSource getDataSourceByType(String type){
        if (typeDataSourceMap == null){
            return null;
        }
        return typeDataSourceMap.get(type);
    }
}
