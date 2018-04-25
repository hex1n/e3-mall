package x.e3mall.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import x.e3mall.common.jedis.JedisClient;
import x.e3mall.common.pojo.DataGridResult;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.utils.JsonUtils;
import x.e3mall.content.service.ContentService;
import x.e3mall.dao.TbContentMapper;
import x.e3mall.pojo.TbContent;
import x.e3mall.pojo.TbContentExample;

import java.util.Date;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/18 15:30
 */

@Service
public class ContentServiceImpl implements ContentService {


    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${Content_List}")
    private String Content_List;

    @Override
    public E3Result save(TbContent tbContent) {

        tbContent.setCreated(new Date());

        tbContentMapper.insertSelective(tbContent);
        //缓存同步,删除缓存中对应的数据
        jedisClient.hdel(Content_List,tbContent.getCategoryId().toString());

        return E3Result.ok();
    }

    @Override
    public DataGridResult ContentList(long categoryId, Integer page, Integer rows) {

        //设置分页信息
        PageHelper.startPage(page, rows);

        //创建条件查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> tbContents = tbContentMapper.selectByExample(example);
        //获取分页信息
        PageInfo<TbContent> pageInfo = new PageInfo<>(tbContents);
        //创建返回结果对象
        DataGridResult dataGridResult = new DataGridResult();
        dataGridResult.setRows(tbContents);
        dataGridResult.setTotal((int) pageInfo.getTotal());

        return dataGridResult;
    }

    @Override
    public List<TbContent> getContentList(long categoryId) {

        //查询缓存
        String json = jedisClient.hget(Content_List, categoryId + "");
        if (StringUtils.isNoneBlank(json)) {
            List<TbContent> tbContents = JsonUtils.jsonToList(json, TbContent.class);

            return tbContents;
        }
        //创建查询条件
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //查询所有分类信息
        List<TbContent> tbContents = tbContentMapper.selectByExampleWithBLOBs(example);
        //将查到的分类放入redis
        try {
            jedisClient.hset(Content_List, categoryId + "", JsonUtils.objectToJson(tbContents));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbContents;
    }
}
