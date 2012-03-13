package org.dongq.analytics.utils;

import org.apache.poi.ss.usermodel.Cell;

public class CellUtils {

	public static String toConvert(Cell cell) {
		String value = "";
		if (cell == null) return value;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			value = String.valueOf((int) cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		default:
			value = "";
			break;
		}

		return value;
	}
}
