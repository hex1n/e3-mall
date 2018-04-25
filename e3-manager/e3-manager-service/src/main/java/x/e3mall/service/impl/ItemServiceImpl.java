package x.e3mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import x.e3mall.common.jedis.JedisClient;
import x.e3mall.common.pojo.DataGridResult;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.utils.IDUtils;
import x.e3mall.common.utils.JsonUtils;
import x.e3mall.dao.TbItemCatMapper;
import x.e3mall.dao.TbItemDescMapper;
import x.e3mall.dao.TbItemMapper;
import x.e3mall.pojo.TbItem;
import x.e3mall.pojo.TbItemDesc;
import x.e3mall.pojo.TbItemExample;
import x.e3mall.service.ItemService;

import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 4/10/2018 3:19 PM
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination topicDestination;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;

    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;

    @Override
    public TbItem getItemById(Long id) {

        //查询缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + id + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        //根据主键查询
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        if (tbItems != null && tbItems.size() > 0) {
            try {
                //把结果添加到缓存
                jedisClient.set(REDIS_ITEM_PRE + ":" + id + ":BASE", JsonUtils.objectToJson(tbItems.get(0)));
                //设置过期时间
                jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ":BASE", ITEM_CACHE_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tbItems.get(0);
        }
        return null;
    }

    @Override
    public DataGridResult getItemList(Integer page, Integer rows) {

        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        //获取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        //创建返回结果对象
        DataGridResult result = new DataGridResult();
        result.setRows(tbItems);
        result.setTotal((int) pageInfo.getTotal());

        return result;
    }

    @Override
    public E3Result addItem(TbItem tbItem, String desc) {

        //设置TbItem的id
        long ItemId = IDUtils.genItemId();
        tbItem.setId(ItemId);
        //设置商品状态: 1:正常  2:下架 0: 删除
        tbItem.setStatus((byte) 1);
        //设置创建时间
        Date date = new Date();
        tbItem.setCreated(date);
        //设置修改时间
        tbItem.setUpdated(date);
        //向商品表插入数据
        tbItemMapper.insert(tbItem);
        //创建TbItemDesc对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        //设置商品描述id
        tbItemDesc.setItemId(ItemId);
        //设置创建时间
        tbItemDesc.setCreated(date);
        //设置修改时间
        tbItemDesc.setUpdated(date);
        //设置描述
        tbItemDesc.setItemDesc(desc);
        //向商品描述表插入数据
        tbItemDescMapper.insert(tbItemDesc);
        //发送一个商品添加消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(ItemId + "");

                return textMessage;
            }
        });

        return E3Result.ok();
    }


    @Override
    public E3Result itemShelves(long id) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        tbItem.setStatus((byte) 2);
        tbItemMapper.updateByPrimaryKey(tbItem);

        return E3Result.ok();
    }

    @Override
    public E3Result itemReshelf(long id) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        tbItem.setStatus((byte) 1);
        tbItemMapper.updateByPrimaryKey(tbItem);
        return E3Result.ok();
    }


    @Override
    public E3Result deleteItems(Long id) {

        tbItemMapper.deleteByPrimaryKey(id);

        return E3Result.ok();
    }

    @Override
    public E3Result itemEdit(TbItem tbItem, String desc) {

        Date date = new Date();
        tbItem.setUpdated(date);
        tbItemMapper.updateByPrimaryKeySelective(tbItem);
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);

        return E3Result.ok();
    }

    @Override
    public E3Result findItemDescById(long id) {
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
        return E3Result.ok(tbItemDesc);
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {

        //查询缓存
        String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
        try {
            if (StringUtils.isNotBlank(json)) {
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        try {
            //把结果添加到缓存
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tbItemDesc;
    }

}
