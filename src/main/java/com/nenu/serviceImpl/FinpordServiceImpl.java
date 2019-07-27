package com.nenu.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.nenu.dao.FinpordCopyDao;
import com.nenu.dao.FinpordDao;
import com.nenu.dao.RoleUserOfInDao;
import com.nenu.dao.StoneDao;
import com.nenu.dao.StoninprocDao;
import com.nenu.domain.Finpord;
import com.nenu.domain.FinpordCopy;
import com.nenu.domain.RoleUserOfIn;
import com.nenu.domain.Stone;
import com.nenu.domain.Stoninproc;
import com.nenu.service.FinpordService;
import com.nenu.service.RoleUserOfInService;
import com.nenu.util.ExcelUtils;

@Service

public class FinpordServiceImpl implements FinpordService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FinpordServiceImpl.class);
	@Autowired
	private FinpordDao finpordDao;
	@Autowired
	private StoneDao stoneDao;
	@Autowired
	private StoninprocDao stoninprocDao;
	@Autowired
	private FinpordCopyDao finpordCopyDao;
	@Autowired
	private RoleUserOfInDao roleUserOfInDao;
	@Override
	public List<Finpord> findAllFinpord() {
		// TODO Auto-generated method stub
		List<Finpord> list = new ArrayList<Finpord>();
		list = finpordDao.findAllFinpord();
		LOGGER.info("查询所有finprod:>>"+list.size());
		return list;
	}
	

	@Override
	public List<FinpordCopy> finprodexcel2sql(String fileName, MultipartFile mfile,int level) {
		List<RoleUserOfIn> ll = roleUserOfInDao.findByRoleUser(level);
		List roleUserOfInList = new ArrayList<>();
		for (int i = 0; i < ll.size(); i++) {
			roleUserOfInList.add(ll.get(i).getRole_type());
		}
		
		
		Finpord finpord = new Finpord();
		List<Finpord> list = new ArrayList<Finpord>();
		File uploadDir = new File("D:\\fileupload");
		// 创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
		if (!uploadDir.exists())
			uploadDir.mkdirs();
		// 新建一个文件
		File tempFile = new File("D:\\fileupload\\finpord" + new Date().getTime() + ".xls");
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
			return readExcelValue(wb, tempFile,roleUserOfInList);
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
	@SuppressWarnings("rawtypes")
	private List<FinpordCopy> readExcelValue(Workbook wb, File tempFile, List roleUserOfInList) {

		// 查出临时库的石头编号
		List<FinpordCopy> finpordCopyListNo = new ArrayList<FinpordCopy>();
		// 查出所有在库的石头编号
		List<Finpord> finpordListNo = new ArrayList<Finpord>();
		finpordCopyListNo = finpordCopyDao.findAllFinpordCopy();
		finpordListNo = finpordDao.findAllFinpord();
		List listNo = new ArrayList<>();
		for (int i = 0; i < finpordListNo.size(); i++) {
			listNo.add(finpordListNo.get(i).getFinpord_mainstono());
		}
		for (int i = 0; i < finpordCopyListNo.size(); i++) {
			listNo.add(finpordCopyListNo.get(i).getFinpord_mainstono());
		}

		// 错误信息接收器
		String errorMsg = "";
		// 得到第二个shell
		Sheet sheet = wb.getSheetAt(0);
		// 得到Excel的行数
		int totalRows = sheet.getPhysicalNumberOfRows();
		// 总列数
		int totalCells = 0;
		int tit=0;
		for(int i=0;i<30;i++) {//找到标题行
			if(sheet.getRow(i).getPhysicalNumberOfCells()==62) {
				tit = i;
				break;
			}
		}
		Row title = sheet.getRow(tit);//标题行
		Map  map = new HashMap<>();
		//标题映射关系构建
		for (int i = 0; i < title.getPhysicalNumberOfCells(); i++) {
			if(title.getCell(i).getStringCellValue().equals("单号")) {
				map.put(i, "finpord_procordNo");
			}else if(title.getCell(i).getStringCellValue().equals("条码")) {
				map.put(i, "finpord_barcode");
			}else if(title.getCell(i).getStringCellValue().equals("名称")) {
				map.put(i, "finpord_name");
			}else if(title.getCell(i).getStringCellValue().equals("供应商")) {
				map.put(i, "finpord_supplier");
			}else if(title.getCell(i).getStringCellValue().equals("款号")) {
				map.put(i, "finpord_styno");
			}else if(title.getCell(i).getStringCellValue().equals("成色")) {
				map.put(i, "finpord_quality");
			}else if(title.getCell(i).getStringCellValue().equals("圈口")) {
				map.put(i, "finpord_ringsz");
			}else if(title.getCell(i).getStringCellValue().equals("倍率")) {
				map.put(i, "finpord_multi");
			}else if(title.getCell(i).getStringCellValue().equals("标价")) {
				map.put(i, "finpord_tagedpr");
			}else if(title.getCell(i).getStringCellValue().equals("金重")) {
				map.put(i, "finpord_goldwgt");
			}else if(title.getCell(i).getStringCellValue().equals("货重")) {
				map.put(i, "finpord_merchwgt");
			}else if(title.getCell(i).getStringCellValue().equals("柜台")) {
				map.put(i, "finpord_counter");
			}else if(title.getCell(i).getStringCellValue().equals("品类")) {
				map.put(i, "finpord_catagory");
			}else if(title.getCell(i).getStringCellValue().equals("金料颜色")) {
				map.put(i, "finpord_goldcolor");
			}else if(title.getCell(i).getStringCellValue().equals("基本工费")) {
				map.put(i, "finpord_baslabcost");
			}else if(title.getCell(i).getStringCellValue().equals("其它费")) {
				map.put(i, "finpord_othrcost");
			}else if(title.getCell(i).getStringCellValue().equals("销售工费")) {
				map.put(i, "finpord_salabcost");
			}else if(title.getCell(i).getStringCellValue().equals("金价")) {
				map.put(i, "finpord_goldpr");
			}else if(title.getCell(i).getStringCellValue().equals("损耗")) {
				map.put(i, "finpord_goldlos");
			}else if(title.getCell(i).getStringCellValue().equals("真实成本")) {
				map.put(i, "finpord_cost");
			}else if(title.getCell(i).getStringCellValue().equals("戒臂样式")) {
				map.put(i, "finpord_armsty");
			}else if(title.getCell(i).getStringCellValue().equals("库房")) {
				map.put(i, "finpord_storname");
			}else if(title.getCell(i).getStringCellValue().equals("企业标准号")) {
				map.put(i, "finpord_entsdno");
			}else if(title.getCell(i).getStringCellValue().equals("备注")) {
				map.put(i, "finpord_comments");
			}else if(title.getCell(i).getStringCellValue().equals("主石编")) {
				map.put(i, "finpord_mainstono");
			}else if(title.getCell(i).getStringCellValue().equals("主石名")) {
				map.put(i, "finpord_mainstoname");
			}else if(title.getCell(i).getStringCellValue().equals("主石")) {
				map.put(i, "finpord_mainstowgt");
			}else if(title.getCell(i).getStringCellValue().equals("主石数")) {
				map.put(i, "finpord_mainstoqty");
			}else if(title.getCell(i).getStringCellValue().equals("主石价")) {
				map.put(i, "finpord_mainstopr");
			}else if(title.getCell(i).getStringCellValue().equals("证书号")) {
				map.put(i, "finpord_cert1");
			}else if(title.getCell(i).getStringCellValue().equals("证书2")) {
				map.put(i, "finpord_cert2");
			}else if(title.getCell(i).getStringCellValue().equals("GIA证书")) {
				map.put(i, "finpord_gia1");
			}else if(title.getCell(i).getStringCellValue().equals("GIA证书2")) {
				map.put(i, "finpord_gia2");
			}else if(title.getCell(i).getStringCellValue().equals("形状")) {
				map.put(i, "finpord_mainstoshape");
			}else if(title.getCell(i).getStringCellValue().equals("系列")) {
				map.put(i, "finpord_series");
			}else if(title.getCell(i).getStringCellValue().equals("切工")) {
				map.put(i, "finpord_cut1");
			}else if(title.getCell(i).getStringCellValue().equals("对称性")) {
				map.put(i, "finpord_symmetry1");
			}else if(title.getCell(i).getStringCellValue().equals("抛光")) {
				map.put(i, "finpord_polish1");
			}else if(title.getCell(i).getStringCellValue().equals("荧光")) {
				map.put(i, "finpord_fluorescence1");
			}else if(title.getCell(i).getStringCellValue().equals("级别")) {
				map.put(i, "finpord_clarity1");
			}else if(title.getCell(i).getStringCellValue().equals("颜色")) {
				map.put(i, "finpord_color1");
			}else if(title.getCell(i).getStringCellValue().equals("切工2")) {
				map.put(i, "finpord_cut2");
			}else if(title.getCell(i).getStringCellValue().equals("对称性2")) {
				map.put(i, "finpord_symmetry2");
			}else if(title.getCell(i).getStringCellValue().equals("抛光2")) {
				map.put(i, "finpord_polish2");
			}else if(title.getCell(i).getStringCellValue().equals("荧光2")) {
				map.put(i, "finpord_fluorescence2");
			}else if(title.getCell(i).getStringCellValue().equals("级别2")) {
				map.put(i, "finpord_clarity2");
			}else if(title.getCell(i).getStringCellValue().equals("颜色2")) {
				map.put(i, "finpord_color2");
			}else if(title.getCell(i).getStringCellValue().equals("副石编")) {
				map.put(i, "finpord_substo1no");
			}else if(title.getCell(i).getStringCellValue().equals("副石名")) {
				map.put(i, "finpord_substo1name");
			}else if(title.getCell(i).getStringCellValue().equals("副石")) {
				map.put(i, "finpord_substo1wgt");
			}else if(title.getCell(i).getStringCellValue().equals("副石数")) {
				map.put(i, "finpord_substo1qty");
			}else if(title.getCell(i).getStringCellValue().equals("副石价")) {
				map.put(i, "finpord_substo1pr");
			}else if(title.getCell(i).getStringCellValue().equals("副石2编")) {
				map.put(i, "finpord_substo2no");
			}else if(title.getCell(i).getStringCellValue().equals("副石2名")) {
				map.put(i, "finpord_substo2name");
			}else if(title.getCell(i).getStringCellValue().equals("副石2重")) {
				map.put(i, "finpord_substo2wgt");
			}else if(title.getCell(i).getStringCellValue().equals("副石2数")) {
				map.put(i, "finpord_substo2qty");
			}else if(title.getCell(i).getStringCellValue().equals("副石2价")) {
				map.put(i, "finpord_substo2pr");
			}else if(title.getCell(i).getStringCellValue().equals("副石3编")) {
				map.put(i, "finpord_substo3no");
			}else if(title.getCell(i).getStringCellValue().equals("副石3名")) {
				map.put(i, "finpord_substo3name");
			}else if(title.getCell(i).getStringCellValue().equals("副石3重")) {
				map.put(i, "finpord_substo3wgt");
			}else if(title.getCell(i).getStringCellValue().equals("副石3数")) {
				map.put(i, "finpord_substo3qty");
			}else if(title.getCell(i).getStringCellValue().equals("副石3价")) {
				map.put(i, "finpord_substo3pr");
			}	
		}
		
		
		List<FinpordCopy> finpordList = new ArrayList<FinpordCopy>();
		
		System.out.println(map.size()+"：大小");

		String br = "<br/>";
		int finpord_seqno=0;//序号
		// 循环Excel行数,从标题行的下一行开始。标题不入库
		for (int r = tit+1; r < totalRows; r++) {
			//String rowMessage = "";
			Row row = sheet.getRow(r);
			System.out.println(row.getPhysicalNumberOfCells());
			if (row == null ) {
				errorMsg += br + "第" + (r + 1) + "行数据有问题，请仔细检查！";
				continue;
			}
			 String finpord_ID;//ID
			 long finpord_procordNo = 0;//单号
			 String finpord_barcode=null;//条码
			 Date finpord_inboundate = new Date();//入库日期
			 String finpord_supplier = null; //供应商
			 String finpord_styno=null;//款号
			 String finpord_quality=null; //成色
			 String finpord_name=null;//名称
			 int finpord_ringsz=0;//圈口尺寸
			 float finpord_multi=0;//倍率
			 double finpord_tagedpr=0;//标价

			 double finpord_goldwgt=0;//金重
			 double finpord_merchwgt=0;//货重
			 String finpord_counter=null;//柜台
			 String finpord_catagory=null;//品类
			 String finpord_goldcolor=null;//金料颜色
			 double finpord_baslabcost=0;//基本工费
			 double finpord_othrcost=0;//其它费用
			 double finpord_salabcost=0;//销售工费
			 double finpord_goldpr=0;//金价
			 double finpord_goldlos=0;//金损耗
			 double finpord_cost=0;//成本
			 String finpord_armsty=null;//戒指壁样式

			 String finpord_storname=null;//库房（门店）
			 String finpord_entsdno=null;//企业标准号
			 String finpord_comments=null;//备注
			 String finpord_mainstono=null;//主石编号
			 String finpord_mainstoname=null;//主石名称
			 double finpord_mainstowgt=0;//主石重量
			 int finpord_mainstoqty=0;//主石数量
			 double finpord_mainstopr=0;//主石价
			 String finpord_cert1=null;//证书1
			 String finpord_cert2=null;//证书2
			 String finpord_gia1=null;//GIA证书1
			 String finpord_gia2=null;//GIA证书1

			 String finpord_mainstoshape=null;//主石形状
			 String finpord_series=null;//系列
			 String finpord_cut1=null;//切工1
			 String finpord_symmetry1=null;//对称性1
			 String finpord_polish1=null;//抛光1
			 String finpord_fluorescence1=null;//荧光1
			 String finpord_clarity1=null;//净度1
			 String finpord_color1=null;//颜色1
			 String finpord_cut2=null;//切工2
			 String finpord_symmetry2=null;//对称性2
			 String finpord_polish2=null;//抛光2
			 String finpord_fluorescence2=null;//荧光2

			 String finpord_clarity2=null;//净度2
			 String finpord_color2=null;//颜色2
			 String finpord_substo1no=null;//副石1编号
			 String finpord_substo1name=null;//副石1名称
			 double finpord_substo1wgt=0;//副石1重量
			 int finpord_substo1qty=0;//副石1数量
			 double finpord_substo1pr=0;//副石1价格
			 String finpord_substo2no=null;//副石2编号
			 String finpord_substo2name=null;//副石2名称
			 double finpord_substo2wgt=0;//副石2重量
			 int finpord_substo2qty=0;//副石2数量
			 double finpord_substo2pr=0;//副石2价格

			 String finpord_substo3no=null;//副石3编号
			 String finpord_substo3name=null;//副石3名称
			 double finpord_substo3wgt=0;//副石3重量
			 int    finpord_substo3qty=0;//副石3数量
			 double finpord_substo3pr=0;//副石3价格
			 int    finpord_reserved1 =0;//备注1
			 String finpord_reserved2=null;//备注2
			
			 finpord_ID = UUID.randomUUID().toString().replaceAll("-", "");// UUID
			 
			 for(int i=0;i<map.size()+1;i++) {//
				 if("finpord_procordNo".equals(map.get(i))) {//单号
	
				} else if("finpord_supplier".equals(map.get(i))) {
						if(row.getCell(i)!=null) {
							 int cell = row.getCell(i).getCellType(); // Cell类型 0 表示数字 1表示字符串
							 if (cell == 1) {
									finpord_supplier = row.getCell(i).getStringCellValue();// 供应商	
								} else if (cell == 0) {
									int temp;
									temp = (int) row.getCell(i).getNumericCellValue();// 供应商
									finpord_supplier = String.valueOf(temp);
							    }
						 } else {
						    	 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
						 }       
			  } else if("finpord_barcode".equals(map.get(i))) {//条码
					if(row.getCell(i)!=null) {
						 int cell = row.getCell(i).getCellType(); // Cell类型 0 表示数字 1表示字符串
						 if (cell == 1) {
							 finpord_barcode = row.getCell(i).getStringCellValue();// 条码	
							} else if (cell == 0) {
								long temp;
								temp = (long) row.getCell(i).getNumericCellValue();// 条码
								finpord_barcode = String.valueOf(temp);
						    }
					 } else {
					    	 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }       
		        }else if("finpord_styno".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_styno = row.getCell(i).getStringCellValue(); // 款号
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_quality".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_quality = row.getCell(i).getStringCellValue(); // 成色
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_name".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_name = row.getCell(i).getStringCellValue(); // 名称
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_ringsz".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_ringsz = (int) row.getCell(i).getNumericCellValue();// 圈口尺寸
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_multi".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_multi = (float) row.getCell(i).getNumericCellValue();// 倍率
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_tagedpr".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_tagedpr = (int)row.getCell(i).getNumericCellValue();// 标价
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_goldwgt".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_goldwgt = row.getCell(i).getNumericCellValue();// 金重
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_merchwgt".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_merchwgt = row.getCell(i).getNumericCellValue();// 货重
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_counter".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_counter = row.getCell(i).getStringCellValue(); // 柜台
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_catagory".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_catagory = row.getCell(i).getStringCellValue(); // 品类
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_mainstono".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_mainstono = row.getCell(i).getStringCellValue();// 主石编号
						 Stoninproc s = stoninprocDao.findStoninprocByStoneNo(finpord_mainstono);
						 if(s!=null ) {
							 finpord_procordNo = s.getStoninproc_procordNo();//订单号
						 }
						 List<Stone> stoneList = stoneDao.findStoneByMainNo(finpord_mainstono);
						 if(stoneList.size() > 0) {
							 finpord_mainstopr = stoneList.get(0).getStone_mainPrperct();//主石价
						 }
						 
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_mainstoname".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_mainstoname =row.getCell(i).getStringCellValue();// 主石名称
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_mainstowgt".equals(map.get(i))) {// 主石重量
					 if(row.getCell(i)!=null) {
						 finpord_mainstowgt = row.getCell(i).getNumericCellValue();
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_mainstoqty".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_mainstoqty = (int) row.getCell(i).getNumericCellValue();// 主石数量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_mainstoqty".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_mainstoqty = (int) row.getCell(i).getNumericCellValue();// 主石数量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_mainstopr".equals(map.get(i))) {// 主石价
					 
			    }else if("finpord_cert1".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_cert1 =row.getCell(i).getStringCellValue();// 证书1
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_cert2".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_cert2 =row.getCell(i).getStringCellValue();// 证书2
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_gia1".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_gia1 =row.getCell(i).getStringCellValue();// GIA证书1
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_gia2".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_gia2 =row.getCell(i).getStringCellValue();// GIA证书2
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_series".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_series =row.getCell(i).getStringCellValue();// 系列
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_cut1".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_cut1 =row.getCell(i).getStringCellValue();// 切工1
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_cut2".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_cut2 =row.getCell(i).getStringCellValue();// 切工2
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_symmetry1".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_symmetry1 =row.getCell(i).getStringCellValue();// 对称性1
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_symmetry2".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_symmetry2 =row.getCell(i).getStringCellValue();// 对称性2
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_polish1".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_polish1 =row.getCell(i).getStringCellValue();// 抛光1
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_polish2".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_polish2 =row.getCell(i).getStringCellValue();// 抛光2
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_fluorescence1".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_fluorescence1 =row.getCell(i).getStringCellValue();// 荧光1
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_fluorescence2".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_fluorescence2 =row.getCell(i).getStringCellValue();// 荧光2
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_clarity1".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_clarity1 =row.getCell(i).getStringCellValue();// 净度1
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_clarity2".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_clarity2 =row.getCell(i).getStringCellValue();// 净度2
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_color1".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_color1 =row.getCell(i).getStringCellValue();// 颜色1
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_color2".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_color2 =row.getCell(i).getStringCellValue();// 颜色2
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_baslabcost".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_baslabcost = row.getCell(i).getNumericCellValue();// 基本工费
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_othrcost".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_othrcost = row.getCell(i).getNumericCellValue();// 其它费用
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_salabcost".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_salabcost = row.getCell(i).getNumericCellValue();// 销售工费
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_goldpr".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_goldpr = row.getCell(i).getNumericCellValue();// 金价
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_goldlos".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_goldlos = row.getCell(i).getNumericCellValue();// 金损耗
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_cost".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_cost = row.getCell(i).getNumericCellValue();// 成本
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo1no".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo1no =row.getCell(i).getStringCellValue();// 副石1编号
						 
						List<Stone> listStone =  stoneDao.findStoneBySubNo(finpord_substo1no);
						if(listStone.size() > 0) {
							finpord_substo1pr = listStone.get(0).getStone_substoPrperct();// 副石1价格
						}
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo2no".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo2no =row.getCell(i).getStringCellValue();// 副石2编号
						 List<Stone> listStone =  stoneDao.findStoneBySubNo(finpord_substo1no);
						 if(listStone.size() > 0) {
							finpord_substo1pr = listStone.get(0).getStone_substoPrperct();// 副石2价格
						 }
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo3no".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo3no =row.getCell(i).getStringCellValue();// 副石3编号
						 List<Stone> listStone =  stoneDao.findStoneBySubNo(finpord_substo1no);
						 if(listStone.size() > 0) {
							finpord_substo1pr = listStone.get(0).getStone_substoPrperct();// 副石3价格
						 }
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo1wgt".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo1wgt =  row.getCell(i).getNumericCellValue();// 副石1重量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo2wgt".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo2wgt =  row.getCell(i).getNumericCellValue();// 副石2重量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo3wgt".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo3wgt =  row.getCell(i).getNumericCellValue();// 副石3重量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo1name".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo1name =  row.getCell(i).getStringCellValue();// 副石1重量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo2name".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo2name =  row.getCell(i).getStringCellValue();// 副石2重量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo3name".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo3name =  row.getCell(i).getStringCellValue();// 副石3重量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo1qty".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo1qty = (int) row.getCell(i).getNumericCellValue();// 副石1数量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo2qty".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo2qty = (int) row.getCell(i).getNumericCellValue();// 副石2数量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo3qty".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_substo3qty = (int) row.getCell(i).getNumericCellValue();// 副石3数量
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_substo1pr".equals(map.get(i))) {
			    	if(row.getCell(i)!=null) {
			    		finpord_substo1pr = row.getCell(i).getNumericCellValue();// 副石1价格
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
					 
			    }else if("finpord_substo2pr".equals(map.get(i))) {
			    	if(row.getCell(i)!=null) {
			    		finpord_substo2pr = row.getCell(i).getNumericCellValue();// 副石1价格
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
					
			    }else if("finpord_substo3pr".equals(map.get(i))) {
			    	if(row.getCell(i)!=null) {
			    		finpord_substo3pr = row.getCell(i).getNumericCellValue();// 副石1价格
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
					 
			    }else if("finpord_armsty".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_armsty = row.getCell(i).getStringCellValue(); // 戒指壁样式
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_storname".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_storname = row.getCell(i).getStringCellValue(); // 库房（门店)
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_entsdno".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_entsdno = row.getCell(i).getStringCellValue(); // 企业标准号
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_comments".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 int cell = row.getCell(i).getCellType(); // Cell类型 0 表示数字 1表示字符串
						 if (cell == 1) {
							 finpord_comments = row.getCell(i).getStringCellValue();// 备注	
							} else if (cell == 0) {
								long temp;
								temp = (long) row.getCell(i).getNumericCellValue();// 备注
								finpord_comments = String.valueOf(temp);
						    }
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_goldcolor".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_goldcolor = row.getCell(i).getStringCellValue(); // 金料颜色
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    }else if("finpord_mainstoshape".equals(map.get(i))) {
					 if(row.getCell(i)!=null) {
						 finpord_mainstoshape =row.getCell(i).getStringCellValue();// 主石形状
					 }else {
						 errorMsg += br + "第  " + (r + 1) + " 行第  "+i+" 列数据有问题，请仔细检查！";
					 }
			    } 
			 }
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
			 String date = sdf.format(new Date()).replaceAll("-", "");//获取现在时间 例如：20180602
			 //finpord_barcode = finpord_supplier + date.substring(2, 6)+(int)((Math.random()*9+1)*10000);
		
			 
			 
			
			System.out.println(listNo.size()+"===========长度");
				
			if (listNo.contains(finpord_mainstono) ) {
				System.out.println("数据库里已有这条数据!");
			} else {
				finpord_seqno++;
				finpordList.add(new FinpordCopy(finpord_ID,finpord_seqno,finpord_procordNo,finpord_barcode,finpord_inboundate,finpord_supplier,finpord_styno,
						finpord_quality,finpord_name,finpord_ringsz,finpord_multi,finpord_tagedpr,finpord_goldwgt,finpord_merchwgt,finpord_counter,
						finpord_catagory,finpord_goldcolor,finpord_baslabcost,finpord_othrcost,finpord_salabcost,finpord_goldpr,finpord_goldlos,finpord_cost,
						finpord_armsty,finpord_storname,finpord_entsdno,finpord_comments,finpord_mainstono,finpord_mainstoname,finpord_mainstowgt,finpord_mainstoqty,
						finpord_mainstopr,finpord_cert1,finpord_cert2,finpord_gia1,finpord_gia2,finpord_mainstoshape,finpord_series,finpord_cut1,finpord_symmetry1,
						finpord_polish1,finpord_fluorescence1,finpord_clarity1,finpord_color1,finpord_cut2,finpord_symmetry2,finpord_polish2,finpord_fluorescence2,
						finpord_clarity2,finpord_color2,finpord_substo1no,finpord_substo1name,finpord_substo1wgt,finpord_substo1qty,finpord_substo1pr,finpord_substo2no,
						finpord_substo2name,finpord_substo2wgt,finpord_substo2qty,finpord_substo2pr,finpord_substo3no,finpord_substo3name,finpord_substo3wgt,finpord_substo3qty,
						finpord_substo3pr,finpord_reserved1,finpord_reserved2));	
			}
		}
		// 删除上传的临时文件
		if (tempFile.exists()) {
			tempFile.delete();
		}
		//批量插入数据库
		if(finpordList.size() > 0) {
			finpordCopyDao.batchInsertFinpordCopy(finpordList);
		}
		

		System.out.println(errorMsg);
		return finpordList;
	}

	@Override
	public int insertFinpordByExcel(FinpordCopy finpord) {
		// TODO Auto-generated method stu
		return finpordCopyDao.insertFinpordByExcel(finpord);
	}
	@Override
	public int batchInsertFinpordCopy(List<FinpordCopy> list) {
		// TODO Auto-generated method stub
		return finpordCopyDao.batchInsertFinpordCopy(list);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int downloadfinprod2sql(List<Finpord> list, int level,HttpServletResponse response) {
		List<RoleUserOfIn> ll = roleUserOfInDao.findByRoleUser(level);
	
		Map  map = new HashMap<>();
		for (int i = 0; i < ll.size(); i++) {
			map.put(i, ll.get(i).getRole_type());
		}
		String sheetName = "成品库表单";
		String titleName = "成品库数据统计表";
		String fileName = "成品库统计表单";

		String[][] dataList = new String[list.size()][map.size()];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
		String[] name = new String[map.size()];
		for (int i = 0; i < dataList.length; i++)
		{	
			
			Finpord s = (Finpord) list.get(i);
			Date date = s.getFinpord_inboundate();
			for(int j=0;j<map.size();j++) {
				if("finpord_procordNo".equals(map.get(j))) {
					dataList[i][j]=Long.toString(s.getFinpord_procordNo()) ;
					name[j]="单号";
				}else if("finpord_barcode".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_barcode();
					name[j]="条码";
				}else if("finpord_styno".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_styno();
					name[j]="款号";
				}else if("finpord_supplier".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_supplier(); 
					name[j]="供应商";
				}else if("finpord_quality".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_quality();
					name[j]="成色";
				}else if("finpord_name".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_name();
					name[j]="名称";
				}else if("finpord_ringsz".equals(map.get(j))) {
					dataList[i][j]=Integer.toString(s.getFinpord_ringsz()); 
					name[j]="圈口";
				}else if("finpord_multi".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_multi());
					name[j]="倍率";
				}else if("finpord_tagedpr".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_tagedpr());
					name[j]="标价";
				}else if("finpord_goldwgt".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_goldwgt());
					name[j]="金重";
				}else if("finpord_merchwgt".equals(map.get(j))) {
					dataList[i][j]= Double.toString(s.getFinpord_merchwgt());
					name[j]="货重";
				}else if("finpord_counter".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_counter();
					name[j]="柜台";
				}else if("finpord_catagory".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_catagory(); 
					name[j]="品类";
				}else if("finpord_mainstono".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_mainstono();
					name[j]="主石编";
				}else if("finpord_mainstoname".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_mainstoname();
					name[j]="主石名";
				}else if("finpord_mainstowgt".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_mainstowgt());
					name[j]="主石";
				}else if("finpord_mainstoqty".equals(map.get(j))) {
					dataList[i][j]=Integer.toString(s.getFinpord_mainstoqty());
					name[j]="主石数";
				}else if("finpord_mainstopr".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_mainstopr());
					name[j]="主石价";
				}else if("finpord_cert1".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_cert1();
					name[j]="证书号";
				}else if("finpord_cert2".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_cert2();
					name[j]="证书2";
				}else if("finpord_gia1".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_gia1();
					name[j]="GIA证书";
				}else if("finpord_gia2".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_gia2();
					name[j]="GIA证书2";
				}else if("finpord_series".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_series();
					name[j]="系列";
				}else if("finpord_cut1".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_cut1();
					name[j]="切工";
				}else if("finpord_cut2".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_cut2();
					name[j]="切工2";
				}else if("finpord_symmetry1".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_symmetry1();
					name[j]="对称性";
				}else if("finpord_symmetry2".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_symmetry2();
					name[j]="对称性2";
				}else if("finpord_polish1".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_polish1();
					name[j]="抛光";
				}else if("finpord_polish2".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_polish2();
					name[j]="抛光2";
				}else if("finpord_fluorescence1".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_fluorescence1();
					name[j]="荧光";
				}else if("finpord_fluorescence2".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_fluorescence2();
					name[j]="荧光2";
				}else if("finpord_clarity1".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_clarity1();
					name[j]="级别";
				}else if("finpord_clarity2".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_clarity2();
					name[j]="级别2";
				}else if("finpord_color1".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_color1();
					name[j]="颜色";
				}else if("finpord_color2".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_color2();
					name[j]="颜色2";
				}else if("finpord_baslabcost".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_baslabcost());
					name[j]="基本工费";
				}else if("finpord_othrcost".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_othrcost());
					name[j]="其它工费";
				}else if("finpord_salabcost".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_salabcost());
					name[j]="销售工费";
				}else if("finpord_goldpr".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_goldlos());
					name[j]="金价";
				}else if("finpord_goldlos".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_goldlos());
					name[j]="损耗";
				}else if("finpord_cost".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_cost());
					name[j]="成本";
				}else if("finpord_substo1no".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_substo1no();
					name[j]="副石编";
				}else if("finpord_substo2no".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_substo2no();
					name[j]="副石1编";
				}else if("finpord_substo3no".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_substo3no();
					name[j]="副石2编";
				}else if("finpord_substo1name".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_substo1name();
					name[j]="副石名";
				}else if("finpord_substo2name".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_substo2name();
					name[j]="副石2名";
				}else if("finpord_substo3name".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_substo3name();
					name[j]="副石3名";
				}else if("finpord_substo1wgt".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_substo1wgt());
					name[j]="副石";
				}else if("finpord_substo2wgt".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_substo2wgt());
					name[j]="副石2重";
				}else if("finpord_substo3wgt".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_substo3wgt());
					name[j]="副石3重";
				}else if("finpord_substo1qty".equals(map.get(j))) {
					dataList[i][j]=Integer.toString(s.getFinpord_substo1qty());
					name[j]="副石数";
				}else if("finpord_substo2qty".equals(map.get(j))) {
					dataList[i][j]=Integer.toString(s.getFinpord_substo2qty());
					name[j]="副石2数";
				}else if("finpord_substo3qty".equals(map.get(j))) {
					dataList[i][j]=Integer.toString(s.getFinpord_substo3qty());
					name[j]="副石3数";
				}else if("finpord_substo1pr".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_substo1pr());
					name[j]="副石价";
				}else if("finpord_substo2pr".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_substo2pr());
					name[j]="副石2价";
				}else if("finpord_substo3pr".equals(map.get(j))) {
					dataList[i][j]=Double.toString(s.getFinpord_substo3pr());
					name[j]="副石3价";
				}else if("finpord_armsty".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_armsty();
					name[j]="戒臂样式";
				}else if("finpord_storname".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_storname();
					name[j]="库房";
				}else if("finpord_comments".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_comments();
					name[j]="备注";
				}else if("finpord_entsdno".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_entsdno();
					name[j]="企业标准号";
				}else if("finpord_goldcolor".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_goldcolor();
					name[j]="金料颜色";
				}else if("finpord_mainstoshape".equals(map.get(j))) {
					dataList[i][j]=s.getFinpord_mainstoshape();
					name[j]="形状";
				}else if("finpord_inboundate".equals(map.get(j))) {
					dataList[i][j]=sdf.format(date);
					name[j]="入库日期";
				}
			
			}
			
		}

		int columnNumber = map.size();
		int[] columnWidth =new int[map.size()];
		for(int i=0;i<columnNumber;i++) {
			columnWidth[i]=20;	
		}
		

		System.out.println(name.length+"=========表头长度");
		System.out.println(map.size()+"=======字段的长度");
		

		String path = "D:\\finprodFile";
		
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth,
					name, dataList,path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return 0;
	}
	
	

	@Override
	public Finpord findFinpordByMainNo(String mainNo) {
		// TODO Auto-generated method stub
		LOGGER.info("根据主石编查找成品信息");
		return finpordDao.findFinpordByMainNo(mainNo);
	}

	@Override
	public Finpord findLastFinpord() {
		// TODO Auto-generated method stub
		LOGGER.info("寻找成品库最后一条记录");
		
		return finpordDao.findLastFinpord();
	}
	@Override
	public FinpordCopy findLastFinpordCopy() {
		// TODO Auto-generated method stub
		LOGGER.info("寻找临时成品库最后一条记录");
		
		return finpordCopyDao.findLastFinpordCopy();
	}

	

	@Override
	public int updateFinpord(Finpord finpord) {
		// TODO Auto-generated method stub
		LOGGER.info("更新一条成品记录");
		return finpordDao.updateFinpord(finpord);
	}

	@Override
	public List<Finpord> findFinpordByProcordNo(long finpord_procordNo) {
		// TODO Auto-generated method stub
		LOGGER.info("根据订单号查询成品记录>>"+finpord_procordNo);
		return finpordDao.findFinpordByProcordNo(finpord_procordNo);
	}

	@Override
	public List<Finpord> findFinpordByInboundate(Map<String, Object> map) {
		// TODO Auto-generated method stub
		LOGGER.info("根据入库日期查询成品记录:"+map.get("start")+">>"+map.get("end"));
		return finpordDao.findFinpordByInboundate(map);
	}

	@Override
	public List<FinpordCopy> findAllFinpordCopy() {
		// TODO Auto-generated method stub
		List<FinpordCopy> list = new ArrayList<FinpordCopy>();
		list = finpordCopyDao.findAllFinpordCopy();
		LOGGER.info("查询所有临时表成品:>>"+list.size());
		return list;
	}



	@Override
	public int copyToFinpord() {
		// TODO Auto-generated method stub
		int finish=finpordCopyDao.copyToFinpord();
		return finish;
	}

	@Override
	public int clearCopy() {
		// TODO Auto-generated method stub
		int clear=finpordCopyDao.clearCopy();
		return clear;
	}


	@Override
	public Finpord findfinpordByfinpordIDAJAX(String finprod_ID) {
		// TODO Auto-generated method stub
		return finpordDao.findfinpordByfinpordIDAJAX(finprod_ID);
	}


	@Override
	public FinpordCopy findfinpordCopyByfinpordIDAJAX(String finprod_ID) {
		// TODO Auto-generated method stub
		return finpordCopyDao.findfinpordCopyByfinpordIDAJAX(finprod_ID);
	}


	@Override
	public int updateFinpordCopy(FinpordCopy finpordCopy) {
		// TODO Auto-generated method stub
		LOGGER.info("更新一条临时表成品记录");
		return finpordCopyDao.updateFinpordCopy(finpordCopy);
	}


	@Override
	public int downloadprofit(String[][] dataList,HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "利润表单";
		String titleName = "利润数据统计表";
		String fileName = "利润统计表单";
		int columnNumber = 12;
		int[] columnWidth = { 20, 20,20, 20, 10, 10,10, 10, 10, 10,10, 10};
		String path="D:\\profitFile";
		String[] columnName = {"订单号", "款号", "条码", "名称", "标价","主石成本","副石成本", "委外加工工费", "其它费", "折扣率","核算", "物料成本"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth,
					columnName, dataList,path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 2;
	}


	@Override
	public int deleteFinpordCopy(String finprod_ID) {
		// TODO Auto-generated method stub
		LOGGER.info("删除一条临时表成品记录"+finprod_ID);
		return finpordCopyDao.deleteFinpordCopy(finprod_ID);
	}


	@Override
	public int deleteAllData() {
		// TODO Auto-generated method stub
		LOGGER.info("删除临时表中全部数据");
		return finpordCopyDao.deleteAllData();
	}


	@Override
	public List<Finpord> findFinpordByBarcodeNo(String finpord_barcodeNo) {
		// TODO Auto-generated method stub
		LOGGER.info("根据条码查询》》"+finpord_barcodeNo);
		return finpordDao.findFinpordByBarcodeNo(finpord_barcodeNo);
	}


	@Override
	public List<Finpord> findFinpordByTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return finpordDao.findFinpordByTableInfo(params);
	}


	@Override
	public List<FinpordCopy> findFinpordCopyByTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return finpordCopyDao.findFinpordCopyByTableInfo(params);
	}

	@Override
	public List<Finpord> findDistinctBarcode() {
		return finpordDao.findDistinctBarcode();
	}

	@Override
	public List<Finpord> findDistinctInBoundate() {
		return finpordDao.findDistinctInBoundate();
	}

	@Override
	public List<Finpord> findDistinctProcordNo() {
		return finpordDao.findDistinctProcordNo();
	}


	@Override
	public int batchInsertFinpord(List<Finpord> list) {
		// TODO Auto-generated method stub
		return finpordDao.batchInsertFinpord(list);
	}


	

				
}
