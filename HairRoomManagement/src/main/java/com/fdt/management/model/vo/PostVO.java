package com.fdt.management.model.vo;

import com.fdt.management.model.entity.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子视图
 *
 * @author fdt
 * @TableName product
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostVO extends Post {

    private static final long serialVersionUID = 3949746765855548312L;
    /**
     * 是否已点赞
     */
    private Boolean hasThumb;

}