package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    private Map<String,Order> orderDB=new HashMap<>();
    private Map<String,DeliveryPartner> partnerDB=new HashMap<>();
    private Map<String, List<String>> partnerOrderDB=new HashMap<>();
    private Map<String,DeliveryPartner> orderPartnerDB=new HashMap<>();

    public void addOrder(Order order)
    {
        String orderId=order.getId();
        orderDB.put(orderId,order);
    }
    public void addPartner(String partnerId){
        DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
       // deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);

        partnerDB.put(partnerId,deliveryPartner);
    }
    public void addOrderPartnerPair(String orderId,String partnerId){

        DeliveryPartner partner=partnerDB.get(partnerId);
        partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
        partnerDB.put(partnerId,partner);


        if(partnerOrderDB.containsKey(partnerId)){
            List<String> list=partnerOrderDB.get(partnerId);
            list.add(orderId);
            partnerOrderDB.put(partnerId,list);
        }else
        {
            List<String> list=new ArrayList<>();
            list.add(orderId);
            partnerOrderDB.put(partnerId,list);
        }

        if(!orderPartnerDB.containsKey(orderId)){
            orderPartnerDB.put(orderId,partner);
        }
    }
    public Order getOrderById(String orderId)
    {
        if(!orderDB.containsKey(orderId)) return null;
        return orderDB.get(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId){
        if(!partnerDB.containsKey(partnerId)) return null;
        return partnerDB.get(partnerId);
    }
    public int getOrderCountByPartnerId(String partnerId){
        if(!partnerOrderDB.containsKey(partnerId)){
            return 0;
        }
          return partnerOrderDB.get(partnerId).size();
    }
    public List<String> getOrdersByPartnerId(String partnerId){
        if(!partnerOrderDB.containsKey(partnerId)) return new ArrayList<>();
        return partnerOrderDB.get(partnerId);
    }
    public List<String> getAllOrders()
    {
        List<String> list=new ArrayList<>();

        for(String s:orderDB.keySet()){
            list.add(s);
        }
        return list;
    }
    public int getCountUnassignedOrders()
    {
       int assigned=0;
       int totalorders=orderDB.size();

       for(List<String> list:partnerOrderDB.values()){
           assigned+=list.size();
       }

       return totalorders-assigned;
    }
    public void deletePartnerById(String partnerId){
         partnerDB.remove(partnerId);
         partnerOrderDB.remove(partnerId);
    }
    public void deleteOrderById(String orderId){
        orderDB.remove(orderId);

        DeliveryPartner partner=orderPartnerDB.get(orderId);
        partner.setNumberOfOrders(partner.getNumberOfOrders()-1);
        List<String> list=partnerOrderDB.get(partner.getId());
        List<String> ans=new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).equals(orderId)){

            }else{
               ans.add(list.get(i));
            }
        }
        partnerOrderDB.put(partner.getId(),ans);
        orderPartnerDB.remove(orderId);


    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
          List<String> list=partnerOrderDB.get(partnerId);
          String orderId=list.get(list.size()-1);
          int deliveryTime=orderDB.get(orderId).getDeliveryTime();
          double time=deliveryTime/60;
          String timeS=Double.toString(time);
          String[] hourMin=timeS.split(".");
          String newTime="";
          newTime.concat(hourMin[0]+":"+hourMin[1]);

          return newTime;
    }
//    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
//
//    }

}
