package com.xinxian.generator.framework.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName GenQueryField
 * @Description 前端查询
 * @Author lmy
 * @Date 2023/5/9 08:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenUniqueField {

    private List<String> uniqueFields;
    private Integer checkUniqueType;

}
