package com.offcn.project.vo.req;

import com.offcn.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ProjectReturnVo extends BaseVo {
    @ApiModelProperty(value = "项目令牌",required = true)
    private String projectToken;

    @ApiModelProperty(value = "回报类型：0-虚拟回报 1-实物回报",required = true)
    private byte type;
    @ApiModelProperty(value = "支持金额",required = true)
    private Integer supportmoney;

    @ApiModelProperty(value = "回报内容",required = true)
    private String content;

    @ApiModelProperty(value = "单笔限购",required = true)
    private Integer signalpurchase;

    @ApiModelProperty(value = "限购数量",required = true)
    private Integer purchase;

    @ApiModelProperty(value = "运费",required = true)
    private Integer freight;

    @ApiModelProperty(value = "发票 0-不开发票 1-开发票",required = true)
    private Byte invoice;

    @ApiModelProperty(value = "回报时间，天数",required = true)
    private Integer rtndate;
}
