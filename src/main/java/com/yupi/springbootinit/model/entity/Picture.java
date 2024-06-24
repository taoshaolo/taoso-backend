package com.yupi.springbootinit.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片
 *
 * @Author taoshao
 * @Date 2024/5/23
 */


@Data
public class Picture implements Serializable {

    private String title;

    private String url;
    private static final long serialVersionUID = 1L;

}
