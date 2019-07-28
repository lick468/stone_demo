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

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nenu.dao.StoneAnalysisDao;
import com.nenu.domain.Plan;
import com.nenu.domain.StoneAnalysis;
import com.nenu.service.StoneAnalysisService;
import com.nenu.util.ExcelUtils;

@Service

public class StoneAnalysisServiceImpl implements StoneAnalysisService {

	@Autowired
	private StoneAnalysisDao stoneDao;

	@Override
	public List<StoneAnalysis> findAllStone() {
		// TODO Auto-generated method stub
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>();
		list = stoneDao.findAllStone();
		return list;
	}

	@Override
	public List<StoneAnalysis> findStoneBySupplier(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneBySupplier(params);
	}

	@Override
	public List<StoneAnalysis> findStoneByProduct(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneBySupplier(params);
	}

	public List<StoneAnalysis> findSell() {
		// TODO Auto-generated method stub
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>();
		list = stoneDao.findSell();
		return list;
	}

	@Override
	public List<StoneAnalysis> findStoneByArea(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByArea(params);
	}

	@Override
	public List<StoneAnalysis> findRoom() {
		// TODO Auto-generated method stub
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>();
		list = stoneDao.findRoom();
		return list;
	}

	@Override
	public List<StoneAnalysis> findArea() {
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>();
		list = stoneDao.findArea();
		return list;
	}

	@Override
	public List<StoneAnalysis> findCounter() {
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>();
		list = stoneDao.findCounter();
		return list;
	}

	@Override
	public List<StoneAnalysis> findRoomSell() {
		// TODO Auto-generated method stub
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>();
		list = stoneDao.findRoomSell();
		return list;
	}

	@Override
	public List<StoneAnalysis> findAreaSell() {
		// TODO Auto-generated method stub
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>();
		list = stoneDao.findAreaSell();
		return list;
	}

	@Override
	public List<StoneAnalysis> findCounterSell() {
		// TODO Auto-generated method stub
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>();
		list = stoneDao.findCounterSell();
		return list;
	}

	@Override
	public List<StoneAnalysis> findStoneSellNumber(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneSellNumber(params);
	}

	@Override
	public List<StoneAnalysis> findStoneBySupplierFor711(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneBySupplierFor711(params);
	}

	@Override
	public List<StoneAnalysis> findStoneBySupplierForProductSum(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneBySupplierForProductSum(params);
	}

	@Override
	public List<StoneAnalysis> findStoneBySupplierForProductSettlementpriceSum(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneBySupplierForProductSettlementpriceSum(params);
	}

	@Override
	public List<StoneAnalysis> findProductBySupplierAndCounter(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findProductBySupplierAndCounter(params);
	}

	@Override
	public List<StoneAnalysis> findStoneByCounter(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByCounter(params);
	}

	@Override
	public List<StoneAnalysis> findAreaAccountByProductAndTime(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findAreaAccountByProductAndTime(params);
	}

	@Override
	public List<StoneAnalysis> findRoomAccountByProductAndTime(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findRoomAccountByProductAndTime(params);
	}

	@Override
	public List<StoneAnalysis> findCounterAccountByProductAndTime(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findCounterAccountByProductAndTime(params);
	}

	

	@Override
	public List<StoneAnalysis> findExchangeByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findExchangeByParams(params);
	}

	@Override
	public List<StoneAnalysis> excel2sql(String fileName, MultipartFile mfile) {
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
	List<StoneAnalysis> readExcelValue(Workbook wb, File tempFile) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
			if (sheet.getRow(i).getPhysicalNumberOfCells() == 42) {
				tit = i;
				break;
			}
		}
		Row title = sheet.getRow(tit);// 标题行
		Map map = new HashMap<>();
		// 标题映射关系构建
		for (int i = 0; i < title.getPhysicalNumberOfCells(); i++) {
			if (title.getCell(i).getStringCellValue().equals("序号")) {
				map.put(i, "sid");
			}
			if (title.getCell(i).getStringCellValue().equals("来源")) {
				map.put(i, "source");
			}
			if (title.getCell(i).getStringCellValue().equals("库房")) {
				map.put(i, "room");
			}
			if (title.getCell(i).getStringCellValue().equals("区域")) {
				map.put(i, "area");
			}
			if (title.getCell(i).getStringCellValue().equals("单号")) {
				map.put(i, "orderNo");
			}
			if (title.getCell(i).getStringCellValue().equals("条码")) {
				map.put(i, "barcode");
			}
			if (title.getCell(i).getStringCellValue().equals("成色")) {
				map.put(i, "quality");
			}
			if (title.getCell(i).getStringCellValue().equals("主石名")) {
				map.put(i, "centerstonename");
			}
			if (title.getCell(i).getStringCellValue().equals("名称")) {
				map.put(i, "product");
			}
			if (title.getCell(i).getStringCellValue().equals("结算价")) {
				map.put(i, "settlementprice");
			}
			if (title.getCell(i).getStringCellValue().equals("金重")) {
				map.put(i, "goldweight");
			}
			if (title.getCell(i).getStringCellValue().equals("货重")) {
				map.put(i, "goodweight");
			}
			if (title.getCell(i).getStringCellValue().equals("主石")) {
				map.put(i, "centerstone");
			}
			if (title.getCell(i).getStringCellValue().equals("金价")) {
				map.put(i, "goldprice");
			}
			if (title.getCell(i).getStringCellValue().equals("标价")) {
				map.put(i, "listprice");
			}
			if (title.getCell(i).getStringCellValue().equals("折扣")) {
				map.put(i, "discount");
			}
			if (title.getCell(i).getStringCellValue().equals("销售工费")) {
				map.put(i, "wage");
			}
			if (title.getCell(i).getStringCellValue().equals("柜台")) {
				map.put(i, "counter");
			}
			if (title.getCell(i).getStringCellValue().equals("兑换金重")) {
				map.put(i, "exchangegoldweight");
			}
			if (title.getCell(i).getStringCellValue().equals("折足金重")) {
				map.put(i, "deprecitiongoldweight");
			}
			if (title.getCell(i).getStringCellValue().equals("兑换金额")) {
				map.put(i, "exchangemoney");
			}
			if (title.getCell(i).getStringCellValue().equals("品类")) {
				map.put(i, "sort");
			}
			if (title.getCell(i).getStringCellValue().equals("主石数")) {
				map.put(i, "centerstoneNo");
			}
			if (title.getCell(i).getStringCellValue().equals("数量")) {
				map.put(i, "count");
			}
			if (title.getCell(i).getStringCellValue().equals("日期")) {
				map.put(i, "date");
			}
			if (title.getCell(i).getStringCellValue().equals("副石数")) {
				map.put(i, "sidestoneNo");
			}
			if (title.getCell(i).getStringCellValue().equals("副石重")) {
				map.put(i, "sidestoneweight");
			}
			if (title.getCell(i).getStringCellValue().equals("证书号")) {
				map.put(i, "certificateNo");
			}
			if (title.getCell(i).getStringCellValue().equals("销售人")) {
				map.put(i, "salesman");
			}
			if (title.getCell(i).getStringCellValue().equals("颜色")) {
				map.put(i, "color");
			}
			if (title.getCell(i).getStringCellValue().equals("级别")) {
				map.put(i, "level");
			}
			if (title.getCell(i).getStringCellValue().equals("款号")) {
				map.put(i, "priceNo");
			}
			if (title.getCell(i).getStringCellValue().equals("本次积分")) {
				map.put(i, "bouns");
			}
			if (title.getCell(i).getStringCellValue().equals("切工")) {
				map.put(i, "cut");
			}
			if (title.getCell(i).getStringCellValue().equals("卡号")) {
				map.put(i, "cardNo");
			}
			if (title.getCell(i).getStringCellValue().equals("时间")) {
				map.put(i, "time");
			}
			if (title.getCell(i).getStringCellValue().equals("成本")) {
				map.put(i, "cost");
			}
			if (title.getCell(i).getStringCellValue().equals("圈口")) {
				map.put(i, "circle");
			}
			if (title.getCell(i).getStringCellValue().equals("供应商")) {
				map.put(i, "supplier");
			}
			if (title.getCell(i).getStringCellValue().equals("系列")) {
				map.put(i, "series");
			}
			if (title.getCell(i).getStringCellValue().equals("戒臂样式")) {
				map.put(i, "style");
			}
			if (title.getCell(i).getStringCellValue().equals("备注")) {
				map.put(i, "remark");
			}

		}

		

		List<StoneAnalysis> stoneList = new ArrayList<StoneAnalysis>();

		String br = "<br/>";
		int sid = 0;
		String source = null;
		String room = null;
		int bouns = 0;
		String cut = null;
		String cardNo = null;
		String time = null;
		float cost = 0;
		String circle = null;
		String supplier = null;
		String series = null;
		String style = null;
		String remark = null;
		String area = null;
		String orderNo = null;
		String barcode = null;
		String quality = null;
		String centerstonename = null;
        float settlementprice = 0;
		float goldweight = 0;
		float goodweight = 0;
		float centerstone = 0;
		float goldprice = 0;
		float listprice = 0;
		float discount = 0;
		float wage = 0;
		String product = null;
		String counter = null;
		float exchangegoldweight = 0;
		float deprecitiongoldweight = 0;
		float exchangemoney = 0;
		String sort = null;
		int centerstoneNo = 0;
		int count = 0;
		Date date = null;
		int sidestoneNo = 0;
		float sidestoneweight = 0;
		String certificateNo = null;
		String salesman = null;
		String color = null;
		String level = null;
		String priceNo = null;
		// 循环Excel行数,从标题行的下一行开始。标题不入库
				for (int r = tit + 1; r < totalRows; r++) {
					
					Row row = sheet.getRow(r);
					for(int i=0;i<map.size();i++) {
						if("sid".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								sid = Integer.parseInt( getCellValue(row.getCell(i)));//序号
							}
						}else if("source".equals(map.get(i))) {
							source =  getCellValue(row.getCell(i));//来源
						}else if("room".equals(map.get(i))) {
							room =  getCellValue(row.getCell(i));//库房
						}else if("area".equals(map.get(i))) {
							area =  getCellValue(row.getCell(i));//区域
						}else if("orderNo".equals(map.get(i))) {
							orderNo =  getCellValue(row.getCell(i));//单号
						}else if("barcode".equals(map.get(i))) {
							barcode =  getCellValue(row.getCell(i));//条码
						}else if("quality".equals(map.get(i))) {
							quality =  getCellValue(row.getCell(i));//成色
						}else if("centerstonename".equals(map.get(i))) {
							centerstonename =  getCellValue(row.getCell(i));//主石名
						}else if("product".equals(map.get(i))) {
							product =  getCellValue(row.getCell(i));//名称
						}else if("settlementprice".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								settlementprice = Float.parseFloat(getCellValue(row.getCell(i)));//结算价
							}
						}else if("goldweight".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								goldweight =Float.parseFloat(  getCellValue(row.getCell(i)));//金重
							}
						}else if("goodweight".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								goodweight = Float.parseFloat( getCellValue(row.getCell(i)));//货重
							}
						}else if("centerstone".equals(map.get(i))) {
							if(getCellValue(row.getCell(i)).length()>0) {
								centerstone = (float) row.getCell(i).getNumericCellValue();//主石
							}
						}else if("goldprice".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								goldprice = Float.parseFloat( getCellValue(row.getCell(i)));//金价
							}
						}else if("listprice".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								listprice = Float.parseFloat( getCellValue(row.getCell(i)));//标价
							}
						}else if("discount".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								discount = Float.parseFloat( getCellValue(row.getCell(i)));//折扣
							}
						}else if("wage".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								wage = Float.parseFloat( getCellValue(row.getCell(i)));//销售工费
							}
						}else if("counter".equals(map.get(i))) {
							counter =  getCellValue(row.getCell(i));//柜台
						}else if("exchangegoldweight".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								exchangegoldweight =  Float.parseFloat(getCellValue(row.getCell(i)));//兑换金重
							}
						}else if("deprecitiongoldweight".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								deprecitiongoldweight =  Float.parseFloat(getCellValue(row.getCell(i)));//折足金重
							}
						}else if("exchangemoney".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								exchangemoney = Float.parseFloat( getCellValue(row.getCell(i)));//兑换金额
							}
						}else if("sort".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								sort =  getCellValue(row.getCell(i));//品类
							}
						}else if("centerstoneNo".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								centerstoneNo =  Integer.parseInt(getCellValue(row.getCell(i)));//主石数
							}
						}else if("count".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								count =  Integer.parseInt(getCellValue(row.getCell(i)));//数量
							}
						}else if("date".equals(map.get(i))) {
							date =  sdf.parse(getCellValue(row.getCell(i)));//日期
						}else if("sidestoneNo".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								sidestoneNo = Integer.parseInt( getCellValue(row.getCell(i)));//副石
							}
						}else if("sidestoneweight".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								sidestoneweight = Float.parseFloat( getCellValue(row.getCell(i)));//副石重
							}
						}else if("certificateNo".equals(map.get(i))) {
							certificateNo =  getCellValue(row.getCell(i));//证书号
						}else if("salesman".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()<20) {
								salesman =  getCellValue(row.getCell(i));//销售人
							}
						}else if("color".equals(map.get(i))) {
							color =  getCellValue(row.getCell(i));//颜色
						}else if("level".equals(map.get(i))) {
							level =  getCellValue(row.getCell(i));//级别
						}else if("priceNo".equals(map.get(i))) {
							priceNo =  getCellValue(row.getCell(i));//款号
						}else if("bouns".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								bouns =  Integer.parseInt(getCellValue(row.getCell(i)));//本次积分
							}
						}else if("cut".equals(map.get(i))) {
							cut =  getCellValue(row.getCell(i));//切工
						}else if("cardNo".equals(map.get(i))) {
							cardNo =  getCellValue(row.getCell(i));//卡号
						}else if("time".equals(map.get(i))) {
							time =  getCellValue(row.getCell(i));//时间
						}else if("cost".equals(map.get(i))) {
							if( getCellValue(row.getCell(i)).length()>0) {
								cost = Float.parseFloat( getCellValue(row.getCell(i)));//成本
							}
						}else if("circle".equals(map.get(i))) {
							circle =  getCellValue(row.getCell(i));//圈口
						}else if("supplier".equals(map.get(i))) {
							supplier =  getCellValue(row.getCell(i));//供应商
						}else if("series".equals(map.get(i))) {
							series =  getCellValue(row.getCell(i));//系列
						}else if("style".equals(map.get(i))) {
							style =  getCellValue(row.getCell(i));//戒臂样式
						}else if("remark".equals(map.get(i))) {
							remark =  getCellValue(row.getCell(i));//备注
						}
					}
			System.out.println("sid="+sid+",source="+source+",room="+room+",orderNo="+orderNo+",salesman="+salesman+",supplier="+supplier);

			
			// System.out.println("这里少时诵诗书所所所所");
			stoneList.add(new StoneAnalysis(sid, source, room, bouns, cut, cardNo, time, cost, circle, supplier, series, style, remark, area, orderNo, barcode, quality, centerstonename, product, settlementprice, goldweight, goodweight, centerstone, goldprice, listprice, discount, wage, counter, exchangegoldweight, deprecitiongoldweight, exchangemoney, sort, centerstoneNo, count, date, sidestoneNo, sidestoneweight, certificateNo, salesman, color, level, priceNo));

		}
		
		  // 删除上传的临时文件 
		  if (tempFile.exists()) { 
			  tempFile.delete(); 
		   }
		 
		// Long start = System.currentTimeMillis();
		//for (int j = 0; j < stoneList.size(); j++) {
		//	stoneDao.insertStone(stoneList.get(j));
		//}
		//System.out.println("耗时 ： "+(System.currentTimeMillis() - start));
		//Long startnew = System.currentTimeMillis();
		  if(stoneList.size() > 0) {
			  stoneDao.batchInsertStone(stoneList);
		  }
		
		//System.out.println("耗时 ： "+(System.currentTimeMillis() - startnew));

		return stoneList;
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
	public int insertStone(StoneAnalysis stone) {
		// TODO Auto-generated method stub
		return stoneDao.insertStone(stone);
	}
	@Override
	public int batchInsertStone(List<StoneAnalysis> list) {
		// TODO Auto-generated method stub
		return stoneDao.batchInsertStone(list);
	}
	@Override
	public List<StoneAnalysis> findCompareDataSetByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findCompareDataSetByParams(params);
	}

	@Override
	public List<StoneAnalysis> findTwoYearsDataSetForCompare(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findTwoYearsDataSetForCompare(params);
	}

	@Override
	public int downloadExcelForIndex10(List<StoneAnalysis> list) {
		// TODO Auto-generated method stub

		String sheetName = "销售数据表单";
		String titleName = "销售数据统计表";
		String fileName = "销售数据统计表单";
		int columnNumber = 6;
		int[] columnWidth = { 15, 15, 15, 15, 15, 15 };
		String[][] dataList = new String[list.size()][6];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getRoom();
			dataList[i][2] = s.getCounter();
			dataList[i][3] = s.getProduct();
			dataList[i][4] = String.valueOf(s.getSettlementprice());
			dataList[i][5] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "销售区域", "门店", "柜台", "名称", "售价", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;

	}

	@Override
	public int downloadExcelForIndex3(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "兑换销售数据表单";
		String titleName = "兑换销售数据统计表";
		String fileName = "兑换销售数据统计表单";
		int columnNumber = 10;
		int[] columnWidth = { 15, 15, 15, 15, 15, 15,15,15,15,15 };
		String[][] dataList = new String[list.size()][10];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getSupplier();
			dataList[i][1] = s.getCounter();
			dataList[i][2] = s.getProduct();
			dataList[i][3] = s.getSource();
			dataList[i][4] = String.valueOf(s.getExchangegoldweight());
			dataList[i][5] = String.valueOf(s.getSettlementprice());
			dataList[i][6] = String.valueOf(s.getListprice());
			dataList[i][7] = String.valueOf(s.getGoldweight());
			dataList[i][8] = String.valueOf(s.getCenterstone());
			dataList[i][9] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "供应商", "柜台", "名称", "来源", "兑换金重","结算价", "标价", "金重", "主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;

	}

	@Override
	public List<StoneAnalysis> findStoneByAreaAndTimeForSettlementpriceSum(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByAreaAndTimeForSettlementpriceSum(params);
	}

	@Override
	public List<StoneAnalysis> findStoneByAreaAndTimeForNumberSum(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByAreaAndTimeForNumberSum(params);
	}

	@Override
	public List<StoneAnalysis> findStoneByAreaAndTimeForNumberSumDemo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByAreaAndTimeForNumberSumDemo(params);
	}

	@Override
	public int downloadExcelForProductNum(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "供应商品销售数据表单";
		String titleName = "供应商品销售数据统计表";
		String fileName = "供应商品销售数据统计表单";
		int columnNumber = 9;
		int[] columnWidth = { 15, 15, 15, 15,15, 15, 15, 15,15 };
		String[][] dataList = new String[list.size()][9];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getSupplier();
			dataList[i][1] = s.getCounter();
			dataList[i][2] = s.getProduct();
			dataList[i][3] = s.getQuality();
			dataList[i][4] = String.valueOf(s.getSettlementprice());
			dataList[i][5] = String.valueOf(s.getListprice());
			dataList[i][6] = String.valueOf(s.getGoldweight());
			dataList[i][7] = String.valueOf(s.getCenterstone());
			dataList[i][8] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "供应商", "柜台", "名称", "成色", "结算价", "标价", "金重", "主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;

	}

	@Override
	public int downloadExcelForIndex(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "供应商销售数据表单";
		String titleName = "供应商销售数据统计表";
		String fileName = "供应商销售数据统计表单";
		int columnNumber = 8;
		int[] columnWidth = { 15, 15, 15, 15,15, 15, 15, 15 };
		String[][] dataList = new String[list.size()][8];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getSupplier();
			dataList[i][1] = s.getCounter();
			dataList[i][2] = s.getProduct();
			dataList[i][3] = String.valueOf(s.getSettlementprice());
			dataList[i][4] = String.valueOf(s.getListprice());
			dataList[i][5] = String.valueOf(s.getGoldweight());
			dataList[i][6] = String.valueOf(s.getCenterstone());
			dataList[i][7] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "供应商", "柜台", "名称", "结算价", "标价", "金重", "主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadExcelFor743(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "销售结构数据表单";
		String titleName = "销售结构数据表单";
		String fileName = "销售结构数据表单";
		int columnNumber = 8;
		int[] columnWidth = { 15, 15, 15, 15, 15,15,15,15 };
		String[][] dataList = new String[list.size()][8];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);
			dataList[i][0] = s.getCounter();
			dataList[i][1] = s.getSupplier();
			dataList[i][2] = s.getProduct();
			dataList[i][3] = String.valueOf(s.getSettlementprice());
			dataList[i][4] = String.valueOf(s.getListprice());
			dataList[i][5] = String.valueOf(s.getGoldweight());
			dataList[i][6] = String.valueOf(s.getCenterstone());
			dataList[i][7] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "柜台","供应商", "名称", "结算价", "标价", "金重", "主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadExcelFor744(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "区域经营分析数据表单";
		String titleName = "区域经营分析数据表单";
		String fileName = "区域经营分析数据表单";
		int columnNumber = 11;
		int[] columnWidth = { 15, 15, 15, 15, 15,15, 15, 15, 15, 15,15 };
		String[][] dataList = new String[list.size()][11];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);
			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getSupplier();
			dataList[i][2] = s.getRoom();
			dataList[i][3] = s.getCounter();
			dataList[i][4] = s.getProduct();
			dataList[i][5] = s.getQuality();
			dataList[i][6] = String.valueOf(s.getListprice());
			dataList[i][7] = String.valueOf(s.getSettlementprice());
			dataList[i][8] = String.valueOf(s.getGoldweight());
			dataList[i][9] = String.valueOf(s.getCenterstone());
			dataList[i][10] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "地区","供应商", "门店", "柜台", "名称", "成色", "标价", "结算价", "金重", "主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}


	
	@Override
	public List<StoneAnalysis> findStoneByCounterAndTimeOfIndex9(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByCounterAndTimeOfIndex9(params);
	}

	@Override
	public List<StoneAnalysis> findStoneByProductAndTimeOfIndex9(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByProductAndTimeOfIndex9(params);
	}

	@Override
	public List<StoneAnalysis> findSupplierForIndex11(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findSupplierForIndex11(params);
	}

	@Override
	public List<StoneAnalysis> findOneSupplierForIndex11(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findOneSupplierForIndex11(params);
	}

	@Override
	public List<StoneAnalysis> findProductOfIndex811(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findProductOfIndex811(params);
	}

	@Override
	public List<StoneAnalysis> findProductOfIndex812(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findProductOfIndex812(params);
	}

	@Override
	public List<StoneAnalysis> findProductOfIndex813(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findProductOfIndex813(params);
	}

	@Override
	public List<StoneAnalysis> findProductOfIndex814(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findProductOfIndex814(params);
	}

	@Override
	public List<StoneAnalysis> findDateForIndex8888(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findDateForIndex8888(params);
	}

	@Override
	public int downloadExcelForIndex811(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "周销售数据表单";
		String titleName = "周销售数据统计表";
		String fileName = "周销售数据统计表单";
		int columnNumber = 9;
		int[] columnWidth = { 15, 15, 15, 15, 15, 15,15,15,15 };
		String[][] dataList = new String[list.size()][9];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getRoom();
			dataList[i][2] = s.getCounter();
			dataList[i][3] = s.getProduct();
			dataList[i][4] = String.valueOf(s.getSettlementprice());
			dataList[i][5] = String.valueOf(s.getListprice());
			dataList[i][6] = String.valueOf(s.getGoldprice());
			dataList[i][7] = String.valueOf(s.getCenterstone());
			dataList[i][8] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "销售区域", "门店", "柜台", "名称", "结算价","标价","金重","主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int downloadExcelForIndex812(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "月销售数据表单";
		String titleName = "月销售数据统计表";
		String fileName = "月销售数据统计表单";
		int columnNumber = 9;
		int[] columnWidth = { 15, 15, 15, 15, 15, 15,15,15,15 };
		String[][] dataList = new String[list.size()][9];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getRoom();
			dataList[i][2] = s.getCounter();
			dataList[i][3] = s.getProduct();
			dataList[i][4] = String.valueOf(s.getSettlementprice());
			dataList[i][5] = String.valueOf(s.getListprice());
			dataList[i][6] = String.valueOf(s.getGoldprice());
			dataList[i][7] = String.valueOf(s.getCenterstone());
			dataList[i][8] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "销售区域", "门店", "柜台", "名称", "结算价","标价","金重","主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int downloadExcelForIndex813(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "季度销售数据表单";
		String titleName = "季度销售数据统计表";
		String fileName = "季度销售数据统计表单";
		int columnNumber = 9;
		int[] columnWidth = { 15, 15, 15, 15, 15, 15,15,15,15 };
		String[][] dataList = new String[list.size()][9];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getRoom();
			dataList[i][2] = s.getCounter();
			dataList[i][3] = s.getProduct();
			dataList[i][4] = String.valueOf(s.getSettlementprice());
			dataList[i][5] = String.valueOf(s.getListprice());
			dataList[i][6] = String.valueOf(s.getGoldprice());
			dataList[i][7] = String.valueOf(s.getCenterstone());
			dataList[i][8] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "销售区域", "门店", "柜台", "名称", "结算价","标价","金重","主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int downloadExcelForIndex814(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "年销售数据表单";
		String titleName = "年销售数据统计表";
		String fileName = "年销售数据统计表单";
		int columnNumber = 9;
		int[] columnWidth = { 15, 15, 15, 15, 15, 15,15,15,15 };
		String[][] dataList = new String[list.size()][9];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getRoom();
			dataList[i][2] = s.getCounter();
			dataList[i][3] = s.getProduct();
			dataList[i][4] = String.valueOf(s.getSettlementprice());
			dataList[i][5] = String.valueOf(s.getListprice());
			dataList[i][6] = String.valueOf(s.getGoldprice());
			dataList[i][7] = String.valueOf(s.getCenterstone());
			dataList[i][8] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "销售区域", "门店", "柜台", "名称", "结算价","标价","金重","主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public List<StoneAnalysis> findCompareDateCounterOfIndex822(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findCompareDateCounterOfIndex822(params);
	}

	@Override
	public List<StoneAnalysis> findCompareDateProductOfIndex822(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findCompareDateProductOfIndex822(params);
	}

	@Override
	public List<StoneAnalysis> findCompareDateAreaOfIndex822(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findCompareDateAreaOfIndex822(params);
	}

	@Override
	public int downloadExcelForIndex821(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "月销售数据同比分析表单";
		String titleName = "月销售数据统计同比分析表";
		String fileName = "月销售数据统计同比分析表单";
		int columnNumber = 4;
		int[] columnWidth = { 15, 15, 15, 15 };
		String[][] dataList = new String[list.size()][4];
		 System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getIndex8Attributes();
			dataList[i][1] = s.getIndex8Select();
			dataList[i][2] = s.getIndex8Compare();
			dataList[i][3] = s.getIndex8Diff();
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "属性", "月份额", "去年同期额", "环比增长率" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int downloadExcelForIndex822(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "年销售数据同比分析表单";
		String titleName = "年销售数据统计同比分析表";
		String fileName = "年销售数据统计同比分析表单";
		int columnNumber = 4;
		int[] columnWidth = { 15, 15, 15, 15 };
		String[][] dataList = new String[list.size()][4];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getIndex8Attributes();
			dataList[i][1] = s.getIndex8Select();
			dataList[i][2] = s.getIndex8Compare();
			dataList[i][3] = s.getIndex8Diff();
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "属性", "年份额", "去年同期额", "同比增长率" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int downloadExcelForIndex831(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "月销售数据环比分析表单";
		String titleName = "月销售数据统计环比分析表";
		String fileName = "月销售数据统计环比分析表单";
		int columnNumber = 4;
		int[] columnWidth = { 15, 15, 15, 15 };
		String[][] dataList = new String[list.size()][4];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getIndex8Attributes();
			dataList[i][1] = s.getIndex8Select();
			dataList[i][2] = s.getIndex8Compare();
			dataList[i][3] = s.getIndex8Diff();
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "属性", "月份额", "上月同期额", "环比增长率" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int downloadExcelForIndex832(List<StoneAnalysis> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "年销售数据环比分析表单";
		String titleName = "年销售数据统计环比分析表";
		String fileName = "年销售数据统计环比分析表单";
		int columnNumber = 4;
		int[] columnWidth = { 15, 15, 15, 15 };
		String[][] dataList = new String[list.size()][4];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);

			dataList[i][0] = s.getIndex8Attributes();
			dataList[i][1] = s.getIndex8Select();
			dataList[i][2] = s.getIndex8Compare();
			dataList[i][3] = s.getIndex8Diff();
		}
		String path = "D:\\analysisFile";

		String[] columnName = { "属性", "年份额", "去年同期额", "环比增长率" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
	@Override
	public List<StoneAnalysis> findProductByTime(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findProductByTime(params);
	}

	

	@Override
	public int deleteOneData(int id) {
		// TODO Auto-generated method stub
		return stoneDao.deleteOneData(id);
	}

	@Override
	public int deleteAllData() {
		// TODO Auto-generated method stub
		return stoneDao.deleteAllData();
	}

	@Override
	public List<StoneAnalysis> findCompareDateRoomOfIndex822(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findCompareDateRoomOfIndex822(params);
	}

	@Override
	public List<StoneAnalysis> findStoneFor744(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneFor744(params);
	}

	@Override
	public List<StoneAnalysis> findStoneByCounterFor743(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByCounterFor743(params);
	}

	@Override
	public List<StoneAnalysis> findAreaFor742(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findAreaFor742(params);
	}

	@Override
	public List<StoneAnalysis> findRoomFor742(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findRoomFor742(params);
	}

	@Override
	public List<StoneAnalysis> findCounterFor742(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findCounterFor742(params);
	}

	@Override
	public List<StoneAnalysis> findExchangeByParamsFor725(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findExchangeByParamsFor725(params);
	}

	@Override
	public List<StoneAnalysis> findStoneByTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByTableInfo(params);
	}

	@Override
	public List<StoneAnalysis> findStoneBySource(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneBySource(params);
	}

	@Override
	public List<StoneAnalysis> findStoneForSource(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneForSource(params);
	}

	@Override
	public int downloadExcelForIndexSource(List<StoneAnalysis> list, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "成色销售数据表单";
		String titleName = "成色销售数据统计表";
		String fileName = "成色销售数据统计表单";
		int columnNumber = 12;
		int[] columnWidth = { 15, 15, 15, 15,15, 15, 15, 15,15, 15, 15,15 };
		String[][] dataList = new String[list.size()][12];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);
			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getSupplier();
			dataList[i][2] = s.getRoom();
			dataList[i][3] = s.getCounter();
			dataList[i][4] = s.getProduct();
			dataList[i][5] = s.getSource();
			dataList[i][6] = s.getQuality();
			dataList[i][7] = String.valueOf(s.getSettlementprice());
			dataList[i][8] = String.valueOf(s.getListprice());
			dataList[i][9] = String.valueOf(s.getGoldweight());
			dataList[i][10] = String.valueOf(s.getCenterstone());
			dataList[i][11] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = {"地区", "供应商","门店", "柜台", "名称","来源","成色", "结算价", "标价", "金重", "主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	
	@Override
	public List<StoneAnalysis> findStoneForIndex724MainStone(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneForIndex724MainStone(params);
	}

	@Override
	public List<StoneAnalysis> findStoneForIndex724ListPrice(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneForIndex724ListPrice(params);
	}

	@Override
	public List<StoneAnalysis> findStoneForIndex724SettlePrice(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneForIndex724SettlePrice(params);
	}

	@Override
	public List<StoneAnalysis> findStoneForIndex724GoldWeight(Map<String, Object> params) {
		return stoneDao.findStoneForIndex724GoldWeight(params);
	}

	@Override
	public List<StoneAnalysis> findStoneFor722And723(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneFor722And723(params);
	}

	@Override
	public List<StoneAnalysis> findStoneFor722(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneFor722(params);
	}

	@Override
	public List<StoneAnalysis> findStoneFor723(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneFor723(params);
	}

	@Override
	public int downloadExcelForIndex722(List<StoneAnalysis> list, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "款式销售数据表单";
		String titleName = "款式销售数据统计表";
		String fileName = "款式销售数据统计表单";
		int columnNumber = 11;
		int[] columnWidth = { 15, 15, 15, 15,15, 15, 15, 15,15, 15, 15 };
		String[][] dataList = new String[list.size()][11];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);
			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getSupplier();
			dataList[i][2] = s.getRoom();
			dataList[i][3] = s.getCounter();
			dataList[i][4] = s.getProduct();
			dataList[i][5] = s.getPriceNo();
			dataList[i][6] = String.valueOf(s.getSettlementprice());
			dataList[i][7] = String.valueOf(s.getListprice());
			dataList[i][8] = String.valueOf(s.getGoldweight());
			dataList[i][9] = String.valueOf(s.getCenterstone());
			dataList[i][10] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = {"地区", "供应商","门店", "柜台", "名称","款号", "结算价", "标价", "金重", "主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadExcelForIndex723(List<StoneAnalysis> list, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "镶嵌方式销售数据表单";
		String titleName = "镶嵌方式销售数据统计表";
		String fileName = "镶嵌方式销售数据统计表单";
		int columnNumber = 11;
		int[] columnWidth = { 15, 15, 15, 15,15, 15, 15, 15,15, 15, 15 };
		String[][] dataList = new String[list.size()][11];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);
			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getSupplier();
			dataList[i][2] = s.getRoom();
			dataList[i][3] = s.getCounter();
			dataList[i][4] = s.getProduct();
			dataList[i][5] = s.getStyle();
			dataList[i][6] = String.valueOf(s.getSettlementprice());
			dataList[i][7] = String.valueOf(s.getListprice());
			dataList[i][8] = String.valueOf(s.getGoldweight());
			dataList[i][9] = String.valueOf(s.getCenterstone());
			dataList[i][10] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = {"地区", "供应商","门店", "柜台", "名称","镶嵌方式", "结算价", "标价", "金重", "主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadExcelForIndex724(List<StoneAnalysis> list, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "区间销售数据表单";
		String titleName = "区间销售数据统计表";
		String fileName = "区间销售数据统计表单";
		int columnNumber = 11;
		int[] columnWidth = { 15, 15, 15, 15,15, 15, 15, 15,15, 15, 15 };
		String[][] dataList = new String[list.size()][11];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);
			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getSupplier();
			dataList[i][2] = s.getRoom();
			dataList[i][3] = s.getCounter();
			dataList[i][4] = s.getProduct();
			dataList[i][5] = s.getSource();
			dataList[i][6] = String.valueOf(s.getSettlementprice());
			dataList[i][7] = String.valueOf(s.getListprice());
			dataList[i][8] = String.valueOf(s.getGoldweight());
			dataList[i][9] = String.valueOf(s.getCenterstone());
			dataList[i][10] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = {"地区", "供应商","门店", "柜台", "名称","来源", "结算价", "标价", "金重", "主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<StoneAnalysis> findStoneFor726(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneFor726(params);
	}

	@Override
	public List<StoneAnalysis> findStoneForIndex726(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneForIndex726(params);
	}

	@Override
	public int downloadExcelForIndex726(List<StoneAnalysis> list, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "圈口销售数据表单";
		String titleName = "圈口销售数据统计表";
		String fileName = "圈口销售数据统计表单";
		int columnNumber = 11;
		int[] columnWidth = { 15, 15, 15, 15,15, 15, 15, 15,15, 15, 15 };
		String[][] dataList = new String[list.size()][11];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			StoneAnalysis s = (StoneAnalysis) list.get(i);
			dataList[i][0] = s.getArea();
			dataList[i][1] = s.getSupplier();
			dataList[i][2] = s.getRoom();
			dataList[i][3] = s.getCounter();
			dataList[i][4] = s.getProduct();
			dataList[i][5] = s.getCircle();
			dataList[i][6] = String.valueOf(s.getSettlementprice());
			dataList[i][7] = String.valueOf(s.getListprice());
			dataList[i][8] = String.valueOf(s.getGoldweight());
			dataList[i][9] = String.valueOf(s.getCenterstone());
			dataList[i][10] = sdf.format(s.getDate());
		}
		String path = "D:\\analysisFile";

		String[] columnName = {"地区", "供应商","门店", "柜台", "名称","圈口", "结算价", "标价", "金重", "主石", "日期" };

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<StoneAnalysis> findStoneForIndex741(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneForIndex741(params);
	}
	@Override
	public int downloadExcelForIndex741(List<Plan> list, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "计划销售数据统计分析表单";
		String titleName = "计划销售数据统计表";
		String fileName = "计划销售数据统计表单";
		int columnNumber = 8;
		int[] columnWidth = { 15, 15, 15, 15,15, 15, 15, 15 };
		String[][] dataList = new String[list.size()][8];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			Plan s = (Plan) list.get(i);
			dataList[i][0] = sdf.format(s.getPlan_start());
			dataList[i][1] = sdf.format(s.getPlan_end());
			dataList[i][2] = s.getPlan_room();
			dataList[i][3] = s.getPlan_belong();
			dataList[i][4] = String.valueOf(s.getPlan_num());
			dataList[i][5] = s.getPlan_index();
			dataList[i][6] = String.valueOf(s.getPlan_do());
			dataList[i][7] = String.valueOf(s.getPlan_diff());
			
		}
		String path = "D:\\analysisFile";

		String[] columnName = {"起始时间", "结束时间","库房", "统计对象", "计划任务", "指标","完成情况", "差额"};

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelFor711(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "供应商单品分析表单";
		String titleName = "供应商单品统计表";
		String fileName = "供应商单品统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelFor712(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "供应商品群分析表单";
		String titleName = "供应商品群统计表";
		String fileName = "供应商品群统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForSource(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "来源分析表单";
		String titleName = "来源统计表";
		String fileName = "来源统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelFor722(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "款式分析表单";
		String titleName = "款式统计表";
		String fileName = "款式统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelFor723(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "镶嵌方式分析表单";
		String titleName = "镶嵌方式统计表";
		String fileName = "镶嵌方式统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelFor724(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "区间分析表单";
		String titleName = "区间统计表";
		String fileName = "区间统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelFor726(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "圈口统计分析表单";
		String titleName = "圈口统计表";
		String fileName = "圈口统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForIndex7(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "系列商品贡献度分析表单";
		String titleName = "系列商品贡献度统计表";
		String fileName = "系列商品贡献度统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelFor743(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "同品不同类别分析表单";
		String titleName = "同品不同类别统计表";
		String fileName = "同品不同类别统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelFor744(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "区域经营分析表单";
		String titleName = "区域经营统计表";
		String fileName = "区域经营统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForS75(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "管理分析表单";
		String titleName = "管理统计表";
		String fileName = "管理统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelFor7311(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "年走势分析表单";
		String titleName = "年走势统计表";
		String fileName = "年走势统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int downloadGraphExcelFor7312(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "月走势分析表单";
		String titleName = "月走势统计表";
		String fileName = "月走势统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 2;
		int[] columnWidth = { 15, 15 };
		String[][] dataList = new String[glist.length/2][2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%2==1) {
				int j = (i+1)/2-1;
				dataList[j][0] =glist[i-1] ;
				dataList[j][1] =glist[i] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"X轴", "Y轴"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForIndex3(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "兑换分析表单";
		String titleName = "兑换统计表";
		String fileName = "兑换统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 3;
		int[] columnWidth = { 15, 15,15 };
		String[][] dataList = new String[glist.length/3][3];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%3==2) {
				int j = (i+1)/3-1;
				dataList[j][0] =glist[i-2] ;
				dataList[j][1] =glist[i-1] ;
				dataList[j][2] =glist[i-0] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"Y轴", "X轴1", "X轴2"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForIndex11(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "供应商销售分析表单";
		String titleName = "供应商销售统计表";
		String fileName = "供应商销售统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 3;
		int[] columnWidth = { 15, 15,15 };
		String[][] dataList = new String[glist.length/3][3];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%3==2) {
				int j = (i+1)/3-1;
				dataList[j][0] =glist[i-2] ;
				dataList[j][1] =glist[i-1] ;
				dataList[j][2] =glist[i-0] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"Y轴", "X轴1", "X轴2"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForS752(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "销售结构分析表单";
		String titleName = "销售结构统计表";
		String fileName = "销售结构统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 4;
		int[] columnWidth = { 15, 15,15,15 };
		String[][] dataList = new String[glist.length/4][4];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%4==3) {
				int j = (i+1)/4-1;
				dataList[j][0] =glist[i-3] ;
				dataList[j][1] =glist[i-1] ;
				dataList[j][2] =glist[i-2] ;
				dataList[j][3] =glist[i-0] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"上轴", "数目", "下轴","数目"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForIndex811(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "周销售分析表单";
		String titleName = "周销售统计表";
		String fileName = "周销售统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 3;
		int[] columnWidth = { 15, 15,15 };
		String[][] dataList = new String[glist.length/3][3];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%3==2) {
				int j = (i+1)/3-1;
				dataList[j][0] =glist[i-2] ;
				dataList[j][1] =glist[i-1] ;
				dataList[j][2] =glist[i-0] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"时间", "分类","数目"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForIndex812(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "月销售分析表单";
		String titleName = "月销售统计表";
		String fileName = "月销售统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 3;
		int[] columnWidth = { 15, 15,15 };
		String[][] dataList = new String[glist.length/3][3];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%3==2) {
				int j = (i+1)/3-1;
				dataList[j][0] =glist[i-2] ;
				dataList[j][1] =glist[i-1] ;
				dataList[j][2] =glist[i-0] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"时间", "分类","数目"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForIndex813(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "季度销售分析表单";
		String titleName = "季度销售统计表";
		String fileName = "季度销售统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 3;
		int[] columnWidth = { 15, 15,15 };
		String[][] dataList = new String[glist.length/3][3];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%3==2) {
				int j = (i+1)/3-1;
				dataList[j][0] =glist[i-2] ;
				dataList[j][1] =glist[i-1] ;
				dataList[j][2] =glist[i-0] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"时间", "分类","数目"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForIndex814(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "年销售分析表单";
		String titleName = "年销售统计表";
		String fileName = "年销售统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 3;
		int[] columnWidth = { 15, 15,15 };
		String[][] dataList = new String[glist.length/3][3];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%3==2) {
				int j = (i+1)/3-1;
				dataList[j][0] =glist[i-2] ;
				dataList[j][1] =glist[i-1] ;
				dataList[j][2] =glist[i-0] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"时间", "分类","数目"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int downloadGraphExcelForIndex5(String conGraph, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "销售趋势分析表单";
		String titleName = "销售趋势统计表";
		String fileName = "销售趋势统计表单";
		String[] glist = conGraph.split("&");
		int columnNumber = 3;
		int[] columnWidth = { 15, 15,15 };
		String[][] dataList = new String[glist.length/3][3];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%3==2) {
				int j = (i+1)/3-1;
				dataList[j][0] =glist[i-2] ;
				dataList[j][1] =glist[i-1] ;
				dataList[j][2] =glist[i-0] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"时间", "分类","数目"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<StoneAnalysis> findDistinctArea() {
		return stoneDao.findDistinctArea();
	}

	@Override
	public List<StoneAnalysis> findDistinctProduct() {
		return stoneDao.findDistinctProduct();
	}

	@Override
	public List<StoneAnalysis> findDistinctRoom() {
		return stoneDao.findDistinctRoom();
	}

	@Override
	public List<StoneAnalysis> findDistinctSupplier() {
		return stoneDao.findDistinctSupplier();
	}

	@Override
	public List<StoneAnalysis> findDistinctPriceNo() {
		return stoneDao.findDistinctPriceNo();
	}

	@Override
	public List<StoneAnalysis> findDistinctSeries() {
		return stoneDao.findDistinctSeries();
	}

	@Override
	public List<StoneAnalysis> findDistinctQuality() {
		return stoneDao.findDistinctQuality();
	}

	@Override
	public List<StoneAnalysis> findDistinctCounter() {
		return stoneDao.findDistinctCounter();
	}
	@Override
	public List<StoneAnalysis> findDistinctCircle() {
		return stoneDao.findDistinctCircle();
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByProduct(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByProduct(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByArea(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByArea(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByCounter(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByCounter(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByRoom(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByRoom(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackBySupplier(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackBySupplier(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackBySource(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackBySource(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByPriceNo(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByPriceNo(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackBySeries(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackBySeries(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByDate(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByDate(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByDateAndProduct(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByDateAndProduct(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByDateAndArea(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByDateAndArea(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByDateAndCounter(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByDateAndCounter(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByProductAndSource(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByProductAndSource(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByCenterstone(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByCenterstone(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackBySettlementprice(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackBySettlementprice(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByGoldweight(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByGoldweight(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByListprice(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByListprice(params);
	}

    @Override
    public List<StoneAnalysis> findSourceEqualsBackByCircle(Map<String, Object> params) {
        return stoneDao.findSourceEqualsBackByCircle(params);
    }

	@Override
	public List<StoneAnalysis> findSourceEqualsBackByQuality(Map<String, Object> params) {
		return stoneDao.findSourceEqualsBackByQuality(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByQuality(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByQuality(params);
	}

	@Override
    public List<StoneAnalysis> findSourceEqualsSaleByCircle(Map<String, Object> params) {
        return stoneDao.findSourceEqualsSaleByCircle(params);
    }

    @Override
	public List<StoneAnalysis> findSourceEqualsSaleByProduct(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByProduct(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByArea(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByArea(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByCounter(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByCounter(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByRoom(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByRoom(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleBySupplier(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleBySupplier(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleBySource(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleBySource(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByPriceNo(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByPriceNo(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleBySeries(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleBySeries(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByDate(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByDate(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByDateAndProduct(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByDateAndProduct(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByDateAndArea(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByDateAndArea(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByDateAndCounter(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByDateAndCounter(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByProductAndSource(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByProductAndSource(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByCenterstone(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByCenterstone(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleBySettlementprice(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleBySettlementprice(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByGoldweight(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByGoldweight(params);
	}

	@Override
	public List<StoneAnalysis> findSourceEqualsSaleByListprice(Map<String, Object> params) {
		return stoneDao.findSourceEqualsSaleByListprice(params);
	}

	@Override
	public List<StoneAnalysis> findStoneByParams(Map<String, Object> params) {
		return stoneDao.findStoneByParams(params);
	}

	@Override
	public List<StoneAnalysis> findDistinctSource() {
		return stoneDao.findDistinctSource();
	}

}
