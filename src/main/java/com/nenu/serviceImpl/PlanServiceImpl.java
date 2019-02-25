package com.nenu.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.nenu.dao.PlanDao;
import com.nenu.domain.Plan;
import com.nenu.domain.StoneAnalysis;
import com.nenu.service.PlanService;
import com.nenu.util.ExcelUtils;
@Service

public class PlanServiceImpl implements PlanService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlanServiceImpl.class);
	@Autowired
	private PlanDao planDao;
	@Override
	public List<Plan> planexcel2sql(String fileName, MultipartFile mfile) {
		// TODO Auto-generated method stub
		File uploadDir = new File("D:\\fileupload");
		// 创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
		if (!uploadDir.exists())
			uploadDir.mkdirs();
		// 新建一个文件
		File tempFile = new File("D:\\fileupload\\" + new Date().getTime() + ".xls");
		// 初始化输入流
		InputStream is = null;

		try {
			// 将上传的文件写入新建的文件中
			mfile.transferTo(tempFile);

			// 根据新建的文件实例化输入流
			is = new FileInputStream(tempFile);

			// 根据版本选择创建Workbook的方式
			Workbook wb = null;
			// 根据文件名判断文件是2003版本还是2007版本
			if (ExcelUtils.isExcel2007(fileName)) {
				wb = new XSSFWorkbook(is);
			} else {
				wb = new HSSFWorkbook(is);
			}
			// 根据excel里面的内容读取知识库信息
			System.out.println("这里");
			return readExcelValue(wb, tempFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
			}
		}

		return null;
	}
	
	//新的解析xls工具
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Plan> readExcelValue(Workbook wb, File tempFile) throws ParseException {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<Plan> planAllList = planDao.findAllPlan();
			
			// 错误信息接收器
			String errorMsg = "";
			// 得到第一个shell
			Sheet sheet = wb.getSheetAt(0);
			// 得到Excel的行数
			
			int totalRows = sheet.getPhysicalNumberOfRows();
			System.out.println("总共"+totalRows+"行数据");
			// 总列数
			// int totalCells = 0;
			// 得到Excel的列数(前提是有行数)，从第二行算起
			int tit = 0;
			for (int i = 0; i < 31; i++) {// 找到标题行
				if (sheet.getRow(i).getPhysicalNumberOfCells() == 6) {
					tit = i;
					break;
				}
			}
			Row title = sheet.getRow(tit);// 标题行
			Map map = new HashMap<>();
			// 标题映射关系构建
			for (int i = 0; i < title.getPhysicalNumberOfCells(); i++) {
				if (title.getCell(i).getStringCellValue().equals("起始日期")) {
					map.put(i, "plan_start");
				}
				if (title.getCell(i).getStringCellValue().equals("结束日期")) {
					map.put(i, "plan_end");
				}
				if (title.getCell(i).getStringCellValue().equals("库房")) {
					map.put(i, "plan_room");
				}
				if (title.getCell(i).getStringCellValue().equals("统计对象")) {
					map.put(i, "plan_belong");
				}
				if (title.getCell(i).getStringCellValue().equals("计划任务")) {
					map.put(i, "plan_num");
				}
				if (title.getCell(i).getStringCellValue().equals("指标")) {
					map.put(i, "plan_index");
				}

			}

			List<Plan> planList = new ArrayList<Plan>();
			// 循环Excel行数,从标题行的下一行开始。标题不入库
			for (int r = tit + 1; r < totalRows; r++) {
				Date plan_start=new Date();
				Date plan_end=new Date();
				String plan_room = null;
				String plan_belong = null;
				float plan_num = 0;
				String plan_index = null;
				
				Row row = sheet.getRow(r);
				for(int i=0;i<map.size();i++) {
					if("plan_start".equals(map.get(i))) {
						if (getCellValue(row.getCell(i)).length()>0) {
							plan_start =  sdf.parse(getCellValue(row.getCell(i)));//起始日期	
						}
					}else if("plan_end".equals(map.get(i))) {
						if (getCellValue(row.getCell(i)).length()>0) {
							plan_end =  sdf.parse(getCellValue(row.getCell(i)));//结束日期	
						}
					}else if("plan_room".equals(map.get(i))) {
						if (getCellValue(row.getCell(i)).length()>0) {
							plan_room =  getCellValue(row.getCell(i));//库房
						}
					}else if("plan_belong".equals(map.get(i))) {
						if (getCellValue(row.getCell(i)).length()>0) {
							plan_belong =  getCellValue(row.getCell(i));//统计对象	
						}
					}else if("plan_num".equals(map.get(i))) {
						if (getCellValue(row.getCell(i)).length()>0) {
							plan_num =Float.parseFloat(  getCellValue(row.getCell(i)));//计划任务	
						}
					}else if("plan_index".equals(map.get(i))) {
						if (getCellValue(row.getCell(i)).length()>0) {
							plan_index =  getCellValue(row.getCell(i));//指标	
						}
					}
				}
			
				planList.add(new Plan(plan_start,plan_end,plan_room,plan_belong,plan_num,plan_index));
			}
			
			//删除生成的临时文件
			 if (tempFile.exists()) { 
				 tempFile.delete(); 	 
			 }
			 //剔除相同的
			 for (int i = 0; i < planList.size(); i++) {
				Plan plan = planDao.findPlanByPlan(planList.get(i));
				if(plan!=null) {
					planList.remove(i);
				}
			}
			//批量插入数据库
			 if(planList.size() > 0) {
				 planDao.batchInsertPlan(planList);
			 }
			
			return planList;
		}
	
	//获取单元格的值  
    private String getCellValue(Cell cell) {  
        String cellValue = "";  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       
        if (cell != null) {  
            //判断单元格数据的类型，不同类型调用不同的方法  
            switch (cell.getCellType()) {  
                //数值类型  
                case Cell.CELL_TYPE_NUMERIC:  
                    //进一步判断 ，单元格格式是日期格式   
                	
                    if (DateUtil.isCellDateFormatted(cell)) {  
                        //cellValue = formatter.formatCellValue(cell);
                        cellValue  = sdf.format(cell.getDateCellValue());
                       
                    } else {  
                        //数值  
                        double value = cell.getNumericCellValue();  
                        int intValue = (int) value;  
                        cellValue = value - intValue == 0 ? String.valueOf(intValue) : String.valueOf(value);  
                    }  
                    break;  
                case Cell.CELL_TYPE_STRING:  
                    cellValue = cell.getStringCellValue();  
                    break;  
                case Cell.CELL_TYPE_BOOLEAN:  
                    cellValue = String.valueOf(cell.getBooleanCellValue());  
                    break;  
                    //判断单元格是公式格式，需要做一种特殊处理来得到相应的值  
                case Cell.CELL_TYPE_FORMULA:{  
                    try{  
                        cellValue = String.valueOf(cell.getNumericCellValue());  
                    }catch(IllegalStateException e){  
                        cellValue = String.valueOf(cell.getRichStringCellValue());  
                    }       
                }  
                    break;  
                case Cell.CELL_TYPE_BLANK:  
                    cellValue = "";  
                    break;  
                case Cell.CELL_TYPE_ERROR:  
                    cellValue = "";  
                    break;  
                default:  
                    cellValue = cell.toString().trim();  
                    break;  
            }  
        }  
        return cellValue.trim();  
    }  
	
	
	@Override
	public int insertPlanByExcel(Plan plan) {
		// TODO Auto-generated method stub
		LOGGER.info("插入一条计划任务");
		return planDao.insertPlanByExcel(plan);
	}
	@Override
	public int batchInsertPlan(List<Plan> planList) {
		// TODO Auto-generated method stub
		LOGGER.info("批量插入计划任务");
		return planDao.batchInsertPlan(planList);
	}
	@Override
	public List<Plan> findPlanByTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		LOGGER.info("查找计划任务");
		return planDao.findPlanByTableInfo(params);
	}
	@Override
	public int deleteOneData(int id) {
		// TODO Auto-generated method stub
		LOGGER.info("根据ID删除一个计划任务");
		return planDao.deleteOneData(id);
	}
	@Override
	public int deleteAllData() {
		// TODO Auto-generated method stub
		LOGGER.info("删除所有计划任务");
		return planDao.deleteAllData();
	}
	@Override
	public List<Plan> findPlanByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		LOGGER.info("根据条件查找");
		return planDao.findPlanByParams(params);
	}

	@Override
	public Plan findPlanByPlan(Plan plan) {
		// TODO Auto-generated method stub
		LOGGER.info("查重");
		return planDao.findPlanByPlan(plan);
	}

	@Override
	public List<Plan> findAllPlan() {
		// TODO Auto-generated method stub
		LOGGER.info("查找所有计划销量");
		return planDao.findAllPlan();
	}

	

	
}
