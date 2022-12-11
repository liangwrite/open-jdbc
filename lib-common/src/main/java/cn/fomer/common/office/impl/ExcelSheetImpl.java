package cn.fomer.common.office.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import cn.fomer.common.office.IExcelSheet;

/**
 * 类说明
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-11-17
 */
public class ExcelSheetImpl implements IExcelSheet {
	ExcelImpl excel;
	int sheetNo;
	
	ExcelSheetImpl(ExcelImpl excel, int sheetNo){
		this.excel= excel;
		this.sheetNo= sheetNo;
	}
	
	Cell createCellIfNotExist(int row, int col) {
		Row poiRow = this.createRowIfNotExist(row);
		
		Cell cell= this.getSheetPoi().getRow(row).getCell(col);
		if(cell==null) {
			Cell newCell = poiRow.createCell(col);
			return newCell;
		}
		
		return cell;
	}
	
	
	Row createRowIfNotExist(int row) {
		Row poiRow = this.getSheetPoi().getRow(row);
		if(poiRow==null) {
			Row createRow = this.getSheetPoi().createRow(row);
			return createRow;
		}
		
		return poiRow;
	}
	@Override
	public void write(int row, int col, String message) {
		// TODO Auto-generated method stub
		Cell cell = this.createCellIfNotExist(row, col);
		cell.setCellValue(message);
	}

	@Override
	public void setColorRed(int row, int col) {
		// TODO Auto-generated method stub
		Cell cell = this.createCellIfNotExist(row, col);
		CellStyle style = this.excel.workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.RED.getIndex()); 
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(style);
	}
	@Override
	public Sheet getSheetPoi() {
		// TODO Auto-generated method stub
		return this.excel.poiSheet[this.sheetNo];
	}

}
