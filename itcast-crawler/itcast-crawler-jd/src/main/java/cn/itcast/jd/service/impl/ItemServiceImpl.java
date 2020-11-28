package cn.itcast.jd.service.impl;

import cn.itcast.jd.dao.ItemDao;
import cn.itcast.jd.pojo.Item;
import cn.itcast.jd.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        // determine
        Example<Item> example = Example.of(item);
        // search
        List<Item> list = this.itemDao.findAll(example);
        // return
        return list;
    }
}
