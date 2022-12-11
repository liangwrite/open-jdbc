package cn.fomer.common.office;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * excel的sheet对象
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-11-17
 */
public interface IExcelSheet {

	void write(int row, int col, String message);
	void setColorRed(int row, int col);
	Sheet getSheetPoi();
}
