package com.fdt.common.model.dto.businessSituation;

import com.fdt.common.api.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessSituationQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -969008355709405074L;
    private Long id;
}
