package com.yangman.crawler.jdcrawler.service;

import com.yangman.crawler.jdcrawler.pojo.Item;

import java.util.List;

public interface ItemService {
    /**
     * 保存商品
     *
     * @param item
     */
    public void save(Item item);

    /**
     * 根据条件查找商品
     *
     * @param item
     * @return
     */
    public List<Item> findAll(Item item);
}
