package com.fdt.common.model.vo;

import com.fdt.common.model.entity.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 帖子视图
 *
 * @author fdt
 * @TableName product
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostVO  extends Post implements Serializable {


    private static final long serialVersionUID = -2596201342280769175L;

    /**
     * 是否已点赞
     */
    private Boolean hasThumb;

}