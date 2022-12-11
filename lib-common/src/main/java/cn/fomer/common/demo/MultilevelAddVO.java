package cn.fomer.common.demo;

import java.io.Serializable;

import lombok.Data;

@Data
//@ApiModel(value = "MultilevelAddVO", description = "类别目录新增对象")
public class MultilevelAddVO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
	//@ApiModelProperty(value = "主键id（新增不需要传）")
	private String id;
	/**
	 * 父节点
	 */
	//@ApiModelProperty(value = "父节点（修改不需要传，新增时一级为0）")
	private String parentId;
	/**
	 * 名称
	 */
	//@ApiModelProperty(value = "名称")	
	private String name;
	/**
	 * 字典id
	 */
	//@ApiModelProperty(value = "字典id（左侧目录id）")
	private String dictId;
	/**
	 * 层级
	 */
	//@ApiModelProperty(value = "层级（没有父级为1，有父级为父级+1）")
	private Integer level;
	/**
	 * 备注
	 */
	//@ApiModelProperty(value = "备注")
	private String remark;
}
