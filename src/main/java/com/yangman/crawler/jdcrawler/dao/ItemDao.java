package com.yangman.crawler.jdcrawler.dao;

import com.yangman.crawler.jdcrawler.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: Felix Yang (yangman)
 * @create: 2020-05-18 16:51
 * @description:
 **/

public interface ItemDao extends JpaRepository<Item, Long> { }
