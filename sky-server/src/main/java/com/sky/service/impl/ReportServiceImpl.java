package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 营业额统计
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return TurnoverReportVO
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //设置时间链
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //获取对应日期的营业额
        List<Double> amounts = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Double amount = orderMapper.getTurnoverByDate(beginTime, endTime);
            amount = amount == null ? 0.0 : amount;
            amounts.add(amount);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(amounts, ","))
                .build();
    }

    /**
     * 用户数量统计
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return UserReportVO
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        //设置时间链
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //获取对应日期的用户数量
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        LocalDateTime preTime = LocalDateTime.of(dateList.get(0).minusDays(1), LocalTime.MAX);
        Integer preUserCount = userMapper.getUserCountByTime(preTime);
        preUserCount = preUserCount == null ? 0 : preUserCount;
        for (LocalDate date : dateList) {
            LocalDateTime time = LocalDateTime.of(date, LocalTime.MAX);
            Integer currentUserCount = userMapper.getUserCountByTime(time);
            currentUserCount = currentUserCount == null ? 0 : currentUserCount;
            totalUserList.add(currentUserCount);
            newUserList.add(currentUserCount - preUserCount);
            preUserCount = currentUserCount;
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    /**
     * 订单数量统计
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return OrderReportVO
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        //设置时间链
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //按日期查询订单数量
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        Integer totalOrderCount = 0;
        Integer validOrderCount = 0;
        double orderCompletionRate = 0.0;
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            Integer currentTotalOrderCount = orderMapper.getCountByTimeAndStatus(map);     //订单总数
            map.put("status", Orders.COMPLETED);
            Integer currentValidOrderCount = orderMapper.getCountByTimeAndStatus(map);     //有效订单数
            orderCountList.add(currentTotalOrderCount);
            validOrderCountList.add(currentValidOrderCount);
            totalOrderCount += currentTotalOrderCount;
            validOrderCount += currentValidOrderCount;
        }
        orderCompletionRate = (double) validOrderCount / (double) totalOrderCount;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 销量top10
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return SalesTop10ReportVO
     */
    @Override
    public SalesTop10ReportVO getSalesTop10Statistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> list = orderMapper.getTop10(beginTime, endTime);
        List<String> names = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();
        for (GoodsSalesDTO goodsSalesDTO : list) {
            names.add(goodsSalesDTO.getName());
            numbers.add(goodsSalesDTO.getNumber());
        }
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(names, ","))
                .numberList(StringUtils.join(numbers, ","))
                .build();
    }

    /**
     * 导出运营数据报表
     *
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        //1.查数据库，获取数据
        LocalDate beginTime = LocalDate.now().minusDays(30);
        LocalDate endTime = LocalDate.now().minusDays(1);

        //获取概览数据
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(beginTime, LocalTime.MIN), LocalDateTime.of(endTime, LocalTime.MAX));

        //2.通过POI将数据写入excel中
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            //基于模板创建一个excel表格
            XSSFWorkbook excel = new XSSFWorkbook(resourceAsStream);

            //获取sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            //填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + beginTime + "至" + endTime);

            //获取第4行
            XSSFRow row4 = sheet.getRow(3);
            row4.getCell(2).setCellValue(businessData.getTurnover());   //填充数据--营业额
            row4.getCell(4).setCellValue(businessData.getOrderCompletionRate());    //填充数据--订单完成率
            row4.getCell(6).setCellValue(businessData.getNewUsers());   //填充数据--新增用户数
            //获取第5行
            XSSFRow row5 = sheet.getRow(4);
            row5.getCell(2).setCellValue(businessData.getValidOrderCount());   //填充数据--有效订单
            row5.getCell(4).setCellValue(businessData.getUnitPrice());    //填充数据--平均客单价

            //输出详细数据
            for(int i=0;i<30;i++){
                LocalDate date=beginTime.plusDays(i);
                //按日期查询数据库
                BusinessDataVO businessDataOfDay = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                //获取第8+i行
                XSSFRow row_i = sheet.getRow(7+i);
                row_i.getCell(1).setCellValue(String.valueOf(date));   //填充数据--日期
                row_i.getCell(2).setCellValue(businessDataOfDay.getTurnover());   //填充数据--营业额
                row_i.getCell(3).setCellValue(businessDataOfDay.getValidOrderCount());   //填充数据--有效订单
                row_i.getCell(4).setCellValue(businessDataOfDay.getOrderCompletionRate());    //填充数据--订单完成率
                row_i.getCell(5).setCellValue(businessDataOfDay.getUnitPrice());    //填充数据--平均客单价
                row_i.getCell(6).setCellValue(businessDataOfDay.getNewUsers());   //填充数据--新增用户数
            }

            //3.通过输出流将excel下载到客户端浏览器中
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            //关闭资源
            out.close();
            excel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
