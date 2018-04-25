package x.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/21 9:59
 */
public class SearchResult implements Serializable {

    private long totalPages;
    private long recourdCount;
    private List<SearchItem> itemList;

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getRecourdCount() {
        return recourdCount;
    }

    public void setRecourdCount(long recourdCount) {
        this.recourdCount = recourdCount;
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }
}
