package cn.itcast.jd.service;

import cn.itcast.jd.pojo.Item;
import java.util.List;

public interface ItemService {

    /**
     * save the products
     *
     * @param item
     */
    public void save(Item item);

    /**
     * search the product
     *
     * @param item
     * @return
     */
    public List<Item> findAll(Item item);

}
