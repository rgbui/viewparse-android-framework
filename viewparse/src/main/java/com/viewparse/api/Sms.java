package com.viewparse.api;

/**
 * 短信
 * sms主要结构：
 *  _id：短信序号，如100
 *  thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
 *  address：发件人地址，即手机号，如+8613811810000
 *  person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
 *  date：日期，long型，如1256539465022，可以对日期显示格式进行设置
 *  protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信
 *  read：是否阅读0未读，1已读
 *  status：短信状态-1接收，0complete,64pending,128failed
 *  type：短信类型1是接收到的，2是已发出
 *  body：短信具体内容
 *  service_center：短信服务中心号码编号，如+8613800755500
 */
public class Sms {
    public int id;
    //发件人地址
    public String address;
    //发件人
    public String person;
    public int date;
    public int read;
    public int status;
    public int type;
    //短信内容
    public String body;
}
