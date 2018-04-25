package x.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/12 17:27
 */
public class DataGridResult implements Serializable {

    private Integer total;
    private List<?> rows;

    public DataGridResult(Integer total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public DataGridResult() {
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
