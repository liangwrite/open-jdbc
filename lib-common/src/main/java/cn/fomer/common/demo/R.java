package cn.fomer.common.demo;

import lombok.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Optional;

/**
 * 统一API响应结果封装
 *
 * @author Chill
 */
@Getter
@Setter
@ToString
//@ApiModel(description = "返回信息")
@NoArgsConstructor
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	//@ApiModelProperty(value = "状态码", required = true)
	private int code;
	//@ApiModelProperty(value = "是否成功", required = true)
	private boolean success;
	//@ApiModelProperty(value = "承载数据")
	private T data;
	//@ApiModelProperty(value = "返回消息", required = true)
	private String msg;

}
