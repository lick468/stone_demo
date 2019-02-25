package com.nenu.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nenu.dao.StoneCopyDao;
import com.nenu.dao.StoneDao;
import com.nenu.domain.Stone;
import com.nenu.domain.StoneCopy;
import com.nenu.domain.Stoninproc;
import com.nenu.domain.SupplierStone;
import com.nenu.service.StoneService;
import com.nenu.util.ExcelUtils;

@Service

public class StoneServiceImpl implements StoneService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StoneServiceImpl.class);
	@Autowired
	private StoneDao stoneDao;
	@Autowired
	private StoneCopyDao stoneCopyDao;
	@Override
	public List<Stone> findAllStone() {
		// TODO Auto-generated method stub
		List<Stone> list = new ArrayList<Stone>();
		list = stoneDao.findAllStone();
		LOGGER.info("查询所有Stone:>>" + list.size());
		return list;
	}

	@Override
	public List<StoneCopy> excel2sql(String fileName, MultipartFile mfile, String stone_channelNo) {
		Stone stone = new Stone();
		List<Stone> stoneList = new ArrayList<Stone>();
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
			return readExcelValue(wb, tempFile, stone_channelNo);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<StoneCopy> readExcelValue(Workbook wb, File tempFile, String stone_channelNo) {

		// 查出所有在库的石头编号
				List<StoneCopy> stoneCopyListNo = new ArrayList<StoneCopy>();
				List<Stone> stoneListNo = new ArrayList<Stone>();
				stoneCopyListNo = stoneCopyDao.findAllCopyNo();
				stoneListNo = stoneDao.findAllNo();
				List listNo = new ArrayList<>();
				List listThisNo = new ArrayList<>();
				
				for (int i = 0; i < stoneListNo.size(); i++) {
					if(stoneListNo.get(i).getStone_mainStoneNo().length()>0) {
						listNo.add(stoneListNo.get(i).getStone_mainStoneNo());
					}
					if(stoneListNo.get(i).getStone_substoNo()>0) {
						listNo.add(stoneListNo.get(i).getStone_substoNo());
					}		
				}
				
				for (int i = 0; i < stoneCopyListNo.size(); i++) {
					if(stoneCopyListNo.get(i).getStone_mainStoneNo().length()>0) {
						listNo.add(stoneCopyListNo.get(i).getStone_mainStoneNo());
					}
					if(stoneCopyListNo.get(i).getStone_substoNo() > 0) {
						listNo.add(stoneCopyListNo.get(i).getStone_substoNo());
					}
				}


				// 错误信息接收器
				String errorMsg = "";
				// 得到第一个shell
				Sheet sheet = wb.getSheetAt(0);
				// 得到Excel的行数
				int totalRows = sheet.getPhysicalNumberOfRows();
				// 总列数
				//int totalCells = 0;
				// 得到Excel的列数(前提是有行数)，从第二行算起
				int tit=0;
				for(int i=0;i<30;i++) {//找到标题行
					if(sheet.getRow(i).getPhysicalNumberOfCells()==32) {
						tit = i;
						break;
					}
				}
				Row title = sheet.getRow(tit);//标题行
				Map  map = new HashMap<>();
				//标题映射关系构建
				for (int i = 0; i < title.getPhysicalNumberOfCells(); i++) {
					if(title.getCell(i).getStringCellValue().equals("日期")) {
						map.put(i,"stone_purchdate");
					}
					if(title.getCell(i).getStringCellValue().equals("序号")) {
						map.put(i,"stone_seqno");
					}
					if(title.getCell(i).getStringCellValue().equals("裸石厂")) {
						map.put(i,"stone_loosdofty");
					}
					if(title.getCell(i).getStringCellValue().equals("主石名")) {
						map.put(i,"stone_mainName");
					}
					if(title.getCell(i).getStringCellValue().equals("主石编")) {
						map.put(i,"stone_mainStoneNo");
					}
					if(title.getCell(i).getStringCellValue().equals("规格")) {
						map.put(i,"stone_spec");
					}
					if(title.getCell(i).getStringCellValue().equals("主石数")) {
						map.put(i,"stone_mainNumber");
					}
					if(title.getCell(i).getStringCellValue().equals("主石")) {
						map.put(i,"stone_mainWeight");
					}
					if(title.getCell(i).getStringCellValue().equals("主石价")) {
						map.put(i,"stone_mainPrperct");
					}
					if(title.getCell(i).getStringCellValue().equals("主石额")) {
						map.put(i,"stone_mainPramt");
					}
					if(title.getCell(i).getStringCellValue().equals("副石编")) {
						map.put(i,"stone_substoNo");
					}
					if(title.getCell(i).getStringCellValue().equals("副石名")) {
						map.put(i,"stone_substoName");
					}
					if(title.getCell(i).getStringCellValue().equals("副石")) {
						map.put(i,"stone_substoWgt");
					}
					if(title.getCell(i).getStringCellValue().equals("副石数")) {
						map.put(i,"stone_substoNumber");
					}
					if(title.getCell(i).getStringCellValue().equals("副石价")) {
						map.put(i,"stone_substoPrperct");
					}
					if(title.getCell(i).getStringCellValue().equals("副石额")) {
						map.put(i,"stone_substoPramt");
					}
					if(title.getCell(i).getStringCellValue().equals("形状")) {
						map.put(i,"stone_shape");
					}
					if(title.getCell(i).getStringCellValue().equals("颜色")) {
						map.put(i,"stone_color");
					}
					if(title.getCell(i).getStringCellValue().equals("级别")) {
						map.put(i,"stone_clarity");
					}
					if(title.getCell(i).getStringCellValue().equals("颜色2")) {
						map.put(i,"stone_color2");
					}
					if(title.getCell(i).getStringCellValue().equals("级别2")) {
						map.put(i,"stone_clarity2");
					}
					if(title.getCell(i).getStringCellValue().equals("切工")) {
						map.put(i,"stone_cut");
					}
					if(title.getCell(i).getStringCellValue().equals("荧光")) {
						map.put(i,"stone_fluorescence");
					}
					if(title.getCell(i).getStringCellValue().equals("对称性")) {
						map.put(i,"stone_symmetry");
					}
					if(title.getCell(i).getStringCellValue().equals("抛光")) {
						map.put(i,"stone_polish");
					}
					if(title.getCell(i).getStringCellValue().equals("直径")) {
						map.put(i,"stone_diameter");
					}
					if(title.getCell(i).getStringCellValue().equals("高度")) {
						map.put(i,"stone_height");
					}
					if(title.getCell(i).getStringCellValue().equals("台宽比")) {
						map.put(i,"stone_tablewidth");
					}
					if(title.getCell(i).getStringCellValue().equals("经手人")) {
						map.put(i,"stone_preparer");
					}
					if(title.getCell(i).getStringCellValue().equals("接货人")) {
						map.put(i,"stone_porter");
					}
					if(title.getCell(i).getStringCellValue().equals("备注")) {
						map.put(i,"stone_comments");
					}
					if(title.getCell(i).getStringCellValue().equals("GIA证书号")) {
						map.put(i,"stone_GIA");
					}
					
				}
				
				System.out.println("map.size="+map.size());
				
				List<StoneCopy> stoneList = new ArrayList<StoneCopy>();
				
				// 循环Excel行数,从标题行的下一行开始。标题不入库
				for (int r = tit+1; r < totalRows; r++) {
					//String rowMessage = "";
					Row row = sheet.getRow(r);

					String stone_ID = "";
					Date stone_purchdate=new Date();
					int stone_seqno=0;
					String stone_loosdofty = "";
					String stone_mainName = "";
					String stone_mainStoneNo="";
					String stone_spec = "";
					int stone_mainNumber=0;
					double stone_mainWeight=0;
					double stone_mainPrperct=0;
					double stone_mainPramt=0;
					long stone_substoNo = 0;
					String stone_substoName = "";
					double stone_substoWgt=0;
					int stone_substoNumber=0;
					double stone_substoPrperct=0;
					double stone_substoPramt=0;
					String stone_shape = "";
					String stone_color = "";
					String stone_clarity = "";
					String stone_color2 = "";
					String stone_clarity2 = "";
					String stone_cut = "";
					String stone_fluorescence = "";
					String stone_symmetry = "";
					String stone_polish = "";
					double stone_diameter=0;
					double stone_height=0;
					double stone_tablewidth=0;
					String stone_preparer = "";
					String stone_porter = "";
					String stone_comments = "";
					String stone_GIA="";
					int stone_reserved1 = 0;// 0直接导入的  1修改之后的

					
					stone_ID = UUID.randomUUID().toString().replaceAll("-", "");// UUID
					
					for(int i=0;i<map.size();i++) {
						if("stone_purchdate".equals(map.get(i))) {
							//System.out.println("采购日期");
							if(row.getCell(i)!=null && row.getCell(i).toString().length() > 3) {
								stone_purchdate = row.getCell(i).getDateCellValue(); // 采购日期
							}
							
						} else if("stone_seqno".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_seqno = (int) row.getCell(i).getNumericCellValue();// 序号
							}
							
						} else if("stone_loosdofty".equals(map.get(i))) {
							int cell = row.getCell(i).getCellType(); // Cell类型 0 表示数字 1表示字符串
							if (cell == 1) {
								 stone_loosdofty = row.getCell(i).getStringCellValue();// 裸石厂	
							} else if (cell == 0) {
								int temp;
								temp = (int) row.getCell(i).getNumericCellValue();// 裸石厂
								stone_loosdofty = String.valueOf(temp);
							}
					
						} else if("stone_mainName".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_mainName = row.getCell(i).getStringCellValue();// 主石名
							}
						} else if("stone_mainStoneNo".equals(map.get(i))) {
							//System.out.println("zhushibin");
							if(row.getCell(i)!=null) {
								stone_mainStoneNo = row.getCell(i).getStringCellValue();// 主石编
							}
			
						} else if("stone_spec".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_spec = row.getCell(i).getStringCellValue();// 规格
							}
						} else if("stone_mainNumber".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_mainNumber = (int) row.getCell(i).getNumericCellValue();// 主石数
							}		
						} else if("stone_mainWeight".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_mainWeight = row.getCell(i).getNumericCellValue();// 主石
							}	
						} else if("stone_mainPrperct".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_mainPrperct = row.getCell(i).getNumericCellValue();// 主石价
							}
						} else if("stone_mainPramt".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_mainPramt = row.getCell(i).getNumericCellValue();// 主石额
							}
						} else if("stone_substoNo".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_substoNo = (long)row.getCell(i).getNumericCellValue();// 副石编
							}
						} else if("stone_substoName".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_substoName = row.getCell(i).getStringCellValue();// 副石名
							}	
						} else if("stone_substoWgt".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_substoWgt = row.getCell(i).getNumericCellValue();// 副石
							}
							
							
						} else if("stone_substoNumber".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_substoNumber = (int) row.getCell(i).getNumericCellValue();// 副石数
							}
							
							
						} else if("stone_substoPrperct".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_substoPrperct = row.getCell(i).getNumericCellValue();// 副石价
							}
							
							
						} else if("stone_substoPramt".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_substoPramt = row.getCell(i).getNumericCellValue();// 副石额
							}
							
							
						} else if("stone_shape".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_shape = row.getCell(i).getStringCellValue();// 形状	
							}
							
							
						} else if("stone_color".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_color = row.getCell(i).getStringCellValue();// 颜色
							}
							
							
						} else if("stone_clarity".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_clarity = row.getCell(i).getStringCellValue();// 净度
							}
							
							
						} else if("stone_color2".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_color2 = row.getCell(i).getStringCellValue();// 颜色2
							}
							
							
						} else if("stone_clarity2".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_clarity2 = row.getCell(i).getStringCellValue();// 净度2
							}
							
							
						} else if("stone_cut".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_cut = row.getCell(i).getStringCellValue();// 切工
							}
							
							
						} else if("stone_fluorescence".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_fluorescence = row.getCell(i).getStringCellValue();// 荧光	
							}
							
								
						} else if("stone_symmetry".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_symmetry = row.getCell(i).getStringCellValue();// 对称性
							}
							
							
						} else if("stone_polish".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_polish = row.getCell(i).getStringCellValue();// 抛光
							}
							
							
						} else if("stone_diameter".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_diameter = row.getCell(i).getNumericCellValue();// 直径
							}
							
							
						} else if("stone_height".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_height = row.getCell(i).getNumericCellValue();// 高度
							}
							
							
						} else if("stone_tablewidth".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								stone_tablewidth = row.getCell(i).getNumericCellValue();// 台宽比
							}
							
							
						} else if("stone_preparer".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								int cell = row.getCell(i).getCellType(); // Cell类型 0 表示数字 1表示字符串
								 if (cell == 1) {
									 stone_preparer = row.getCell(i).getStringCellValue();// 经手人	
									} else if (cell == 0) {
										int temp;
										temp = (int) row.getCell(i).getNumericCellValue();// 经手人
										stone_preparer = String.valueOf(temp);
								    }
							}						
						} else if("stone_porter".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								int cell = row.getCell(i).getCellType(); // Cell类型 0 表示数字 1表示字符串
								 if (cell == 1) {
									 stone_porter = row.getCell(i).getStringCellValue();// 接货人	
									} else if (cell == 0) {
										int temp;
										temp = (int) row.getCell(i).getNumericCellValue();//接货人
										stone_porter = String.valueOf(temp);
								    }
							}	
						}else if("stone_comments".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								int cell = row.getCell(i).getCellType(); // Cell类型 0 表示数字 1表示字符串
								 if (cell == 1) {
									 stone_comments = row.getCell(i).getStringCellValue();// 裸石厂	
									} else if (cell == 0) {
										int temp;
										temp = (int) row.getCell(i).getNumericCellValue();// 裸石厂
										stone_comments = String.valueOf(temp);
								    }
							}	
						}else if("stone_GIA".equals(map.get(i))) {
							if(row.getCell(i)!=null) {
								int cell = row.getCell(i).getCellType(); // Cell类型 0 表示数字 1表示字符串
								 if (cell == 1) {
									 stone_GIA = row.getCell(i).getStringCellValue();// GIA证书号	
									} else if (cell == 0) {
										int temp;
										temp = (int) row.getCell(i).getNumericCellValue();// GIA证书号
										stone_GIA = String.valueOf(temp);
								    }
							}	
						}  
					}
					//System.out.println("main:"+stone_mainStoneNo +"-->"+stone_substoNo);
					
					
					//说明此excel表中有与数据库里相同的石编
					if ((listNo.contains(stone_substoNo) && (stone_substoNo>0) ) || (listNo.contains(stone_mainStoneNo)&& (!stone_mainStoneNo.isEmpty()) )) {
						System.out.println("数据库里已有这条数据"+"main:"+stone_mainStoneNo +"-->"+stone_substoNo);
					} else {
						//说明此excel表中有重复石编
						if( (listThisNo.contains(stone_mainStoneNo) && !stone_mainStoneNo.isEmpty()) || (listThisNo.contains(String.valueOf(stone_substoNo)) && stone_substoNo>0 )) {
							System.out.println("Excel表里已有这条数据"+"main:"+stone_mainStoneNo +"-->"+stone_substoNo);
						}else {
							if(!stone_mainStoneNo.isEmpty()) {
								listThisNo.add(stone_mainStoneNo);
							}
							if(stone_substoNo>0 ) {
								listThisNo.add(String.valueOf(stone_substoNo));
							}
			
							stoneList.add(new StoneCopy(stone_ID, stone_purchdate, stone_seqno, stone_loosdofty,
									stone_mainName, stone_mainStoneNo, stone_spec, stone_mainNumber, stone_mainWeight,
									stone_mainPrperct, stone_mainPramt, stone_substoNo, stone_substoName, stone_substoWgt,
									stone_substoNumber, stone_substoPrperct, stone_substoPramt, stone_shape, stone_color,
									stone_clarity, stone_color2, stone_clarity2, stone_cut, stone_fluorescence, stone_symmetry,
									stone_polish, stone_diameter, stone_height, stone_tablewidth, stone_preparer, stone_porter,
									stone_comments, stone_channelNo, stone_reserved1,stone_GIA));
						}
					}
				}

				// 删除上传的临时文件
				if (tempFile.exists()) {
					tempFile.delete();
				}
				//批量插入数据库
				if(stoneList.size()>0) {
					stoneCopyDao.batchInsertStoneCopy(stoneList);
				}
				

				return stoneList;
	}

	
	@Override
	public int insertStoneByExcel(Stone stone) {
		LOGGER.info("插入石头数据" + stone);
		return stoneDao.insertStoneByExcel(stone);
	}
	@Override
	public int batchInsertStoneCopy(List<StoneCopy> list) {
		// TODO Auto-generated method stub
		return stoneCopyDao.batchInsertStoneCopy(list);
	}
	

	@Override
	public List<Stone> findAllNo() {
		List<Stone> list = new ArrayList<Stone>();
		list = stoneDao.findAllNo();
		LOGGER.info("查询数据库里所有石头编号:>>" + list.size());
		return list;
	}

	@Override
	public Stone findStoneByStoneID(String stone_ID) {
		LOGGER.info("通过（stone_ID）查询一个石头信息:>>" + stone_ID);
		return stoneDao.findStoneByStoneID(stone_ID);
	}

	@Override
	public List<Stone> findStoneByMainNo(String stone_mainNo) {
		LOGGER.info("通过（stone_mainNo）查询一个石头信息:>>" + stone_mainNo);
		return stoneDao.findStoneByMainNo(stone_mainNo);
	}

	@Override
	public List<Stone> findStoneBySubNo(long stone_substoNo) {
		LOGGER.info("通过（stone_substoNo）查询一个石头信息:>>" + stone_substoNo);
		return stoneDao.findStoneBySubNo(stone_substoNo);
	}

	@Override
	public List<Stone> findStoneBySupplierName(String stone_supplierName) {
		LOGGER.info("通过（stone_supplierName）查询石头信息:>>" + stone_supplierName);
		return stoneDao.findStoneBySupplierName(stone_supplierName);
	}

	@Override
	public List<Stone> findStoneByPurchdate(Map<String, Object> map) {
		LOGGER.info("通过（stone_purchdate）查询石头信息:>>");
		return stoneDao.findStoneByPurchdate(map);
	}

	@Override
	public int downloadExcel(List<Stone> list,HttpServletResponse response) {
		String sheetName = "石头库表单";
		String titleName = "石头库数据统计表";
		String fileName = "石头库统计表单";
		int columnNumber = 32;
		int[] columnWidth = {  10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
				10, 10, 10, 10, 10, 10, 10, 10, 10, 10 ,10};

		//List<Stone> list = new ArrayList<Stone>();
		//list = stoneDao.findAllStone();
		// System.out.println("list=================="+list.size());
		// System.out.println("list.get(0).toString().length()=================="+list.get(0).toString().length());
		String[][] dataList = new String[list.size()][32];
		// System.out.println("datalist========================"+dataList.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.length; i++) {
			Stone s = (Stone) list.get(i);
			Date date = s.getStone_purchdate();
			dataList[i][0] = sdf.format(date);
			dataList[i][1] = Integer.toString(s.getStone_seqno());
			dataList[i][2] = s.getStone_loosdofty();
			
			if(s.getStone_mainStoneNo().matches(".*[a-zA-Z].*")) {//是主石
				dataList[i][3] = s.getStone_mainName();
				dataList[i][4] = s.getStone_mainStoneNo();
				dataList[i][5] = s.getStone_spec();
				dataList[i][6] = Integer.toString(s.getStone_mainNumber());
				dataList[i][7] = Double.toString(s.getStone_mainWeight());
				dataList[i][8] = Double.toString(s.getStone_mainPrperct());
				dataList[i][9] = Double.toString(s.getStone_mainPramt());
				dataList[i][26] = Double.toString(s.getStone_height());
				dataList[i][27] = Double.toString(s.getStone_tablewidth());
				dataList[i][10] = null;
				dataList[i][11] = null;
				dataList[i][12] = null;
				dataList[i][13] = null;
				dataList[i][14] = null;
				dataList[i][15] = null;
				
			}else {
				dataList[i][3] = null;
				dataList[i][4] = null;
				dataList[i][5] = null;
				dataList[i][6] = null;
				dataList[i][7] = null;
				dataList[i][8] = null;
				dataList[i][9] = null;
				dataList[i][26] = null;
				dataList[i][27] = null;
				dataList[i][10] = String.valueOf(s.getStone_substoNo());
				dataList[i][11] = s.getStone_substoName();
				dataList[i][12] = Double.toString(s.getStone_substoWgt());
				dataList[i][13] = Integer.toString(s.getStone_substoNumber());
				dataList[i][14] = Double.toString(s.getStone_substoPrperct());
				dataList[i][15] = Double.toString(s.getStone_substoPramt());
			}
			
			
			dataList[i][16] = s.getStone_shape();
			dataList[i][17] = s.getStone_color();
			dataList[i][18] = s.getStone_clarity();
			dataList[i][29] = s.getStone_color2();
			dataList[i][20] = s.getStone_clarity2();
			dataList[i][21] = s.getStone_cut();
			dataList[i][22] = s.getStone_fluorescence();
			dataList[i][23] = s.getStone_symmetry();
			dataList[i][24] = s.getStone_polish();
			dataList[i][28] = s.getStone_preparer();
			dataList[i][29] = s.getStone_porter();
			dataList[i][30] = s.getStone_comments();
			dataList[i][31] = s.getStone_GIA();
		}
		String path="D:\\stoneFile";

		String[] columnName = { "日期", "序号", "裸石厂", "主石名", "主石编", "规格", "主石数", "主石", "主石价", "主石额", "副石编", "副石名",
				"副石", "副石数", "副石价", "副石额", "形状", "颜色", "级别", "颜色2", "级别2", "切工", "荧光", "对称性", "抛光", "直径", "高度", "台宽比",
				"经手人", "接货人", "备注" ,"GIA证书号"};

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth,
					columnName, dataList,path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	

	

	@Override
	public List<Stone> findStoneID() {
		// TODO Auto-generated method stub
		return stoneDao.findStoneID();
	}

	@Override
	public Stone findStoneByStoneIDAJAX(String stone_ID) {
		LOGGER.info("查询Stone(通过ajax):>>" + stone_ID);
		return stoneDao.findStoneByStoneIDAJAX(stone_ID);
	}

	@Override
	public List<Stone> findAllStoneNum() {
		LOGGER.info("找出石头库中所有数量多于0的石头");
		return stoneDao.findAllStoneNum();
	}

	@Override
	public int insertStone(StoneCopy stone) {
		LOGGER.info("插入石头");
		return stoneCopyDao.insertStoneCopy(stone);
	}

	@Override
	public int updateMainStoneNumber(Stone stone) {
		// TODO Auto-generated method stub
		LOGGER.info("更新某个主石数量及备注>>"+stone.getStone_mainStoneNo()+":"+stone.getStone_comments());
		return stoneDao.updateMainStoneNumber(stone);
	}

	@Override
	public int updateSubStoneNumber(Stone stone) {
		// TODO Auto-generated method stub
		LOGGER.info("更新某个副石数量及备注>>"+stone.getStone_substoNo()+":"+stone.getStone_comments());
		return stoneDao.updateSubStoneNumber(stone);
	}

	@Override
	public int updateStone(Stone stone) {
		// TODO Auto-generated method stub
		LOGGER.info("更新石头信息>>"+stone.getStone_ID());
		return stoneDao.updateStone(stone);
	}

	@Override
	public List<Stone> findStoneByLoosdofty(String stone_loosdofty) {
		// TODO Auto-generated method stub
		LOGGER.info("根据裸石厂获取石头信息>>"+stone_loosdofty);
		return stoneDao.findStoneByLoosdofty(stone_loosdofty);
	}
	
	@Override
	public List<StoneCopy> findAllStoneCopy() {
		LOGGER.info("找出石头库中所有数量多于0的石头");
		return stoneCopyDao.findAllStone();
	}

	@Override
	public StoneCopy findStoneCopyByStoneIDAJAX(String stone_ID) {
		LOGGER.info("查询Stone(通过ajax):>>" + stone_ID);
		return stoneCopyDao.findStoneByStoneIDAJAX(stone_ID);
		
	}

	@Override
	public int copyToStone() {
		int finish=stoneCopyDao.copyToStone();
		return finish;
	}

	@Override
	public int clearCopy() {
		int clear=stoneCopyDao.clearCopy();
		return clear;
	}

	@Override
	public List<StoneCopy> findAllCopyNo() {
		// TODO Auto-generated method stub
		return stoneCopyDao.findAllCopyNo();
	}

	@Override
	public int updateStoneCopy(StoneCopy stone) {
		// TODO Auto-generated method stub
		LOGGER.info("更新一条临时库石头信息:"+stone);
		return stoneCopyDao.updateStoneCopy(stone);
	}

	@Override
	public int deleteStoneCopy(String stone_ID) {
		// TODO Auto-generated method stub
		LOGGER.info("从临时库中删除一条石头记录:"+stone_ID);
		return stoneCopyDao.deleteStoneCopy(stone_ID);
	}

	@Override
	public int insertStoneByExcel(StoneCopy stone) {
		// TODO Auto-generated method stub
		return stoneCopyDao.insertStoneByExcel(stone);
	}

	@Override
	public int deleteAllData() {
		// TODO Auto-generated method stub
		LOGGER.info("删除临时库中所有数据");
		return stoneCopyDao.deleteAllData();
	}

	@Override
	public int downloadExcelSupplier(List<SupplierStone> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "供应商石头库表单";
		String titleName = "供应商石头库数据统计表";
		String fileName = "供应商石头库统计表单";
		int columnNumber = 13;
		int[] columnWidth = {  10, 10, 10, 10, 10, 10,10,10,10,10,10,10,10};
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String[][] dataList = new String[list.size()][13];
		// System.out.println("datalist========================"+dataList.length);
		
		for (int i = 0; i < list.size(); i++) {
			
				dataList[i][0] =String.valueOf(list.get(i).getId());
				dataList[i][1] =sdf.format(list.get(i).getSupplier_purchdate());
				dataList[i][2] =list.get(i).getSupplier_name();
				dataList[i][3] =String.valueOf(list.get(i).getSupplier_procorNo());
				dataList[i][4] =list.get(i).getSupplier_mainStoneNo();
				dataList[i][5] =String.valueOf(list.get(i).getSupplier_mainNumber()>0?list.get(i).getSupplier_mainNumber():0);
				dataList[i][6] =String.valueOf(list.get(i).getSupplier_mainWeight()>0?list.get(i).getSupplier_mainWeight():0);
				dataList[i][7] =String.valueOf(list.get(i).getSupplier_subStoneNo()>0?list.get(i).getSupplier_subStoneNo():0);
				dataList[i][8] =String.valueOf(list.get(i).getSupplier_subNumber()>0?list.get(i).getSupplier_subNumber():0);
				dataList[i][9] =String.valueOf(list.get(i).getSupplier_subWeight()>0?list.get(i).getSupplier_subWeight():0);
				dataList[i][10] =list.get(i).getSupplier_delyman();
				dataList[i][11] =list.get(i).getSupplier_preparer();
				dataList[i][12] =list.get(i).getSupplier_porter();
			
		
		}
		String path="D:\\stoneFile";

		String[] columnName = { "序号","采购日期","供应商", "订单号", "主石编", "主石数", "主石", "副石编", "副石数", "副石", "送货人", "经手人", "接货人"};

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth,
					columnName, dataList,path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public List<Stone> findAllStoneForPath() {
		// TODO Auto-generated method stub
		return stoneDao.findAllStoneForPath();
	}

	@Override
	public int deleteStoneByStoneIDAJAX(Stone stone) {
		// TODO Auto-generated method stub
		return stoneDao.deleteStoneByStoneIDAJAX(stone);
	}

	@Override
	public List<Stone> findAllStoneNumWithTime(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findAllStoneNumWithTime(params);
	}

	@Override
	public int downloadExcelAllSupplier(List<Stoninproc> list_supplier, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "供应商库存汇总表单";
		String titleName = "供应商库存结余统计表";
		String fileName = "供应商库存结余统计表单";
		int columnNumber = 5;
		int[] columnWidth = {  10, 10, 10, 10, 10};
		
		String[][] dataList = new String[list_supplier.size()][5];
		// System.out.println("datalist========================"+dataList.length);
		DecimalFormat df = new DecimalFormat("0.000");
		for (int i = 0; i < list_supplier.size(); i++) {
			dataList[i][0]=list_supplier.get(i).getStoninproc_supplier();
			dataList[i][1]=String.valueOf(list_supplier.get(i).getMainNumber());
			dataList[i][2]=df.format(list_supplier.get(i).getMainWeight());
			dataList[i][3]=String.valueOf(list_supplier.get(i).getSubNumber());
			dataList[i][4]=df.format(list_supplier.get(i).getSubWeight());
		
		}
		String path="D:\\stoneFile";

		String[] columnName = { "供应商","主石数","主石", "副石数", "副石"};

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth,
					columnName, dataList,path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public List<Stone> findStoneByTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByTableInfo(params);
	}

	@Override
	public List<StoneCopy> findStoneCopyByTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return stoneCopyDao.findStoneCopyByTableInfo(params);
	}

	@Override
	public List<Stone> findStoneByMainNoForManage(String stone_mainNo) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneByMainNoForManage(stone_mainNo);
	}

	@Override
	public List<Stone> findStoneBySubNoForManage(long stone_substoNo) {
		// TODO Auto-generated method stub
		return stoneDao.findStoneBySubNoForManage(stone_substoNo);
	}

	
	
}
