package com.diting.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.diting.util.Utils.isEmpty;

/**
 * Created by liufei on 2016/8/29.
 */
@SuppressWarnings("ALL")
public class ExcelUtil {

    /**
     * 读取excel文件内容，
     *
     * @param fileName
     * @param
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> readExcel(String fileName) {
        //准备返回值列表
        List<Map> valueList = new ArrayList<>();

        String ExtensionName = getExtensionName(fileName);
        File tmpfile = new File(fileName);
        try {
            if (ExtensionName.equalsIgnoreCase("xls")) {
                valueList = readExcel2003(fileName);
            } else if (ExtensionName.equalsIgnoreCase("xlsx")) {
                valueList = readExcel2007(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //删除缓存文件
        tmpfile.delete();
        return valueList;
    }

    /**
     * 文件操作 获取文件扩展名
     *
     * @param filename 文件名称包含扩展名
     * @return
     * @Author: sunny
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * poi针对2003和2007有不同的处理类，必须要判断excel版本,然后采用不同的类
     * ，针对2003是HSSF开头的泪，针对2007是XSSF开头的类
     * 读取2007-2013格式
     *
     * @param filePath 文件路径
     * @return
     * @throws java.io.IOException
     */
    @SuppressWarnings({"rawtypes", "resource"})
    public static List<Map> readExcel2003(String filePath) throws IOException {
        //返回结果集
        List<Map> valueList = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            HSSFWorkbook wookbook = new HSSFWorkbook(fis);  // 创建对Excel工作簿文件的引用
            HSSFSheet sheet = wookbook.getSheetAt(0);   // 在Excel文档中，第一张工作表的缺省索引是0
            int rows = sheet.getPhysicalNumberOfRows(); // 获取到Excel文件中的所有行数­
            Map<Integer, String> keys = new HashMap<>();
            int cells = 0;
            // 遍历行­（第1行  表头） 准备Map里的key  并判断表头是否为question anser两个字段
            HSSFRow firstRow = sheet.getRow(0);
            if (firstRow != null) {
                // 获取到Excel文件中的所有的列
                cells = firstRow.getPhysicalNumberOfCells();
                if (cells >= 1) {
                    // 遍历列
                    for (int j = 0; j < cells; j++) {
                        // 获取到列的值­
                        try {
                            HSSFCell cell = firstRow.getCell((short) j);
                            String cellValue = getCellValue(cell);
                            keys.put(j, cellValue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //TODO 返回错误信息 格式不对
                }
            }
            // 遍历行­（从第二行开始）
            for (int i = 1; i < rows; i++) {
                // 读取左上端单元格(从第二行开始)
                HSSFRow row = sheet.getRow(i);
                // 行不为空
                if (row != null) {
                    //准备当前行 所储存值的map
                    Map<String, Object> val = new HashMap<>();

                    boolean isValidRow = false;

                    // 遍历列
                    for (int j = 0; j < cells; j++) {
                        // 获取到列的值­
                        try {
                            HSSFCell cell = row.getCell((short) j);
                            String cellValue = isEmpty(getCellValue(cell))?null:getCellValue(cell).replace(" ", "");
                            val.put(keys.get(j), cellValue);
                            if (!isValidRow && cellValue != null && cellValue.trim().length() > 0) {
                                isValidRow = true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //第I行所有的列数据读取完毕，放入valuelist
                    if (isValidRow) {
                        valueList.add(val);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert fis != null;
            fis.close();
        }
        return valueList;
    }

    /**
     * poi针对2003和2007有不同的处理类，必须要判断excel版本,然后采用不同的类
     * ，针对2003是HSSF开头的泪，针对2007是XSSF开头的类
     * 读取97-2003格式
     *
     * @param filePath 文件路径
     * @throws java.io.IOException
     */
    @SuppressWarnings({"rawtypes", "resource"})
    public static List<Map> readExcel2007(String filePath) throws IOException {
        List<Map> valueList = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            XSSFWorkbook xwb = new XSSFWorkbook(fis);   // 构造 XSSFWorkbook 对象，strPath 传入文件路径
            XSSFSheet sheet = xwb.getSheetAt(0);            // 读取第一章表格内容
            // 定义 row、cell
            XSSFRow row;
            // 循环输出表格中的第一行内容   表头
            Map<Integer, String> keys = new HashMap<>();
            row = sheet.getRow(0);
            if (row != null) {
                //System.out.println("j = row.getFirstCellNum()::"+row.getFirstCellNum());
//                if (row.getPhysicalNumberOfCells() == KnowledgeUtil.CUS_DATA_COLUMNSIZE) {
                    for (int j = row.getFirstCellNum(); j <= row.getPhysicalNumberOfCells(); j++) {
                        // 通过 row.getCell(j).toString() 获取单元格内容，
                        if (row.getCell(j) != null) {
                            if (!row.getCell(j).toString().isEmpty()) {
                                keys.put(j, row.getCell(j).toString());
                            }
                        } else {
                            keys.put(j, "K-R1C" + j + "E");
                        }
                    }
//                } else {
//                    //TODO 返回错误信息  格式不对
//                }
            }
            // 循环输出表格中的从第二行开始内容
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                System.out.println("i=" + i);
                if (row != null) {
                    boolean isValidRow = false;
                    Map<String, Object> val = new HashMap<>();
                    if (row.getFirstCellNum() < 0) {
                        break;
                    }
                    for (int j = row.getFirstCellNum(); j <= row.getPhysicalNumberOfCells(); j++) {
                        XSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            String cellValue = null;
                            if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    cellValue = new DataFormatter().formatRawCellContents(cell.getNumericCellValue(), 0, "yyyy-MM-dd HH:mm:ss");
                                } else {
                                    cellValue = String.valueOf(cell.getNumericCellValue());
                                }
                            } else {
                                cellValue = cell.toString();
                            }
                            if (cellValue != null && cellValue.trim().length() <= 0) {
                                cellValue = null;
                            }
                            if (cellValue != null && cellValue.trim().length() > 0) {
                                val.put(keys.get(j), cellValue.replace(" ", ""));
                                if (!isValidRow) {
                                    isValidRow = true;
                                }
                            }
                        }
                    }
                    // 第I行所有的列数据读取完毕，放入valuelist
                    if (isValidRow) {
                        valueList.add(val);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert fis != null;
            fis.close();
        }

        return valueList;
    }

    private static String getCellValue(HSSFCell cell) {
        DecimalFormat df = new DecimalFormat("#");
        String cellValue = null;
        if (cell == null)
            return null;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cellValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                    break;
                }
                cellValue = df.format(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                cellValue = null;
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                cellValue = String.valueOf(cell.getErrorCellValue());
                break;
        }
        if (cellValue != null && cellValue.trim().length() <= 0) {
            cellValue = null;
        }
        return cellValue;
    }
}
