package com.cjp.web.utils;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.internet.InternetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 发送邮件
 * Created by han.wang@ucarinc.com on 2017/7/5.
 */
public final class MailUtil02 {


    /**
     * @param receives   收件人，多个收件人以;隔开
     * @param subject    邮件主题
     * @param msg        邮件内容
     * @param urls       附件路径list:如 http://www.apache.org/images/asf_logo_wide.gif
     * @param attachName 附件名称:带扩展名
     * @throws Exception 发送邮件异常
     * @Description: 发送邮件, 多附件
     * <p>
     * Create by 张小青(xiaoqing.zhang@luckycoffee.com) on 2017-08-11 15:24:42
     * @Version 1.0
     */
    public static void send(String receives, String subject, String msg, List<String> urls, String attachName) throws Exception {
        String domain = "luckincoffee.com";
        String host = "smtp.luckincoffee.com";
        int port = 587;
        String user = "sys_sender";
        String password = "QWER1234!@#$";
        String sender = "sys_sender@luckincoffee.com";
        String UTF8 = "utf-8";
        HtmlEmail htmlEmail = new HtmlEmail();
        htmlEmail.setHostName(host);
        htmlEmail.setSmtpPort(port);
        htmlEmail.setAuthentication(user, password);
        htmlEmail.setFrom(sender);
        htmlEmail.setTo(getSendAddressList(receives));
        htmlEmail.setCharset(UTF8);
        htmlEmail.setSubject(subject);
        htmlEmail.setMsg(msg);

        if (null != urls && urls.size() > 0) {
            EmailAttachment attach;
            for (String url : urls) {
                attach = new EmailAttachment();
                attach.setURL(new URL(url));
                attach.setDisposition(EmailAttachment.ATTACHMENT);
                attach.setName(attachName);
                htmlEmail.attach(attach);
            }
        }
        htmlEmail.send();
    }

    public static void main(String[] args) {

        String reciever = "junping.chi@luckincoffee.com";
        String object = "主题：测试发送02";
        String msg = "消息：测试发送";
        List<String> urls = Arrays.asList("http://www.apache.org/images/asf_logo_wide.gif");
        String attacheName = "apachePic.gif";
        try {
            MailUtil02.send(reciever, object, msg, urls, attacheName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 组装收件人
     *
     * @param receives 收件人
     * @return 真实的收件人地址
     * @throws Exception 拼装邮件地址异常
     */
    private static List<InternetAddress> getSendAddressList(String receives) throws Exception {
        List<InternetAddress> addressList = new ArrayList<InternetAddress>();
        String[] tokens = receives.split(";");
        for (String address : tokens) {
            addressList.add(new InternetAddress(address));
        }
        return addressList;
    }

}
