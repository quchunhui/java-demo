package rexel.com.dview.pojo;

import lombok.Data;

/**
 * @ClassName: DViewVarInfo
 * @Description: DView变量信息
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
@Data
public class DViewVarInfo {

    /**
     * 变量索引
     */
    private int index;
    /**
     * 变量名称
     */
    private String name;
    /**
     * 变量值
     */
    private Object value;
}
