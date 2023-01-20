package cn.fomer.jdbc.clone.service;

import java.util.List;

import cn.fomer.common.entity.ResultVO;
import cn.fomer.jdbc.api.ColumnService;
import cn.fomer.jdbc.api.TableService;

/**
 * 类说明
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-09
 */
public interface ISQLBuilderService {
	ResultVO getTableDDL(TableService table);
	
	String createFieldSQL(ColumnService fieldVO);
}
