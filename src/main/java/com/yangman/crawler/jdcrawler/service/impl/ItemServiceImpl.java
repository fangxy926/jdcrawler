package com.yangman.crawler.jdcrawler.service.impl;

import com.yangman.crawler.jdcrawler.dao.ItemDao;
import com.yangman.crawler.jdcrawler.pojo.Item;
import com.yangman.crawler.jdcrawler.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Felix Yang (yangman)
 * @create: 2020-05-18 16:57
 * @description:
 **/

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;

    @Override
    @Transactional
    public void save(Item item) {
        this.itemDao.save(item);
    }

    @Override
    public List<Item> findAll(Item item) {
        //声明查询条件
        Example<Item> example = Example.of(item);
        // 根据查询条件进行查询数据
        List<Item> list = this.itemDao.findAll(example);
        return list;
    }
}
