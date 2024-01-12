package com.xinxian.generator.framework.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class GenQueryFieldVO {

    private String queryField;
    private String queryFieldDesc;

}
