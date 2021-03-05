package com.offcn.order.service.impl;

import com.offcn.dycommon.enums.OrderStatusEnumes;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.po.TOrder;
import com.offcn.order.po.TReturn;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.util.AppDateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {
        TOrder order = new TOrder();
        String memberIdStr = redisTemplate.opsForValue().get(vo.getAccessToken());
        int memberId = Integer.parseInt(memberIdStr);
        order.setMemberid(memberId);
        //2.复制对象  orderInfoSubmitVo---》order
        BeanUtils.copyProperties(vo,order);
        //设置订单号
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        order.setOrdernum(orderNum);
        //支付状态
        order.setStatus(OrderStatusEnumes.UNPAY.getCode()+"");
        order.setInvoice(vo.getInvoice().toString());    //发票状态
        order.setCreatedate(AppDateUtil.getFormatTime()); //创建时间
        //远程调用
        AppResponse<List<TReturn>> response = projectServiceFeign.getReturnList(vo.getProjectid());
        List<TReturn> returnList = response.getData();
        TReturn myReturn=null;
        for (TReturn tReturn : returnList) {
            if(tReturn.getId().intValue() == vo.getReturnid().intValue()){
                myReturn=tReturn;
            }
        }
        //支持金额  回报数量*回报支持金额+运费
        Integer money=order.getRtncount()*myReturn.getSupportmoney()+myReturn.getFreight();
        order.setMoney(money);
        orderMapper.insertSelective(order);
        return order;
    }
}
