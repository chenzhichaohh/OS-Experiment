package com.chenzhichao.www.shiyan2;

/**
 * @author ChenZhichao
 * @mail chenzhichaohh@163.com
 * @create 2020-06-17
 */
public class Resource {
     String resourceName;     //资源名
     Integer resourceSize;       //资源大小
    public Resource(String resourceName, Integer size){
        this.resourceName = resourceName;
        this.resourceSize = size;
    }
}
