
package com.kunyan;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "MailUtilWeb", targetNamespace = "http://zxc/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface MailUtilWeb {


    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     */
    @WebMethod
    @RequestWrapper(localName = "sendmail", targetNamespace = "http://zxc/", className = "com.kunyan.SendMail")
    @ResponseWrapper(localName = "sendmailResponse", targetNamespace = "http://zxc/", className = "com.kunyan.SendMailResponse")
    public void sendmail(
            @WebParam(name = "arg0", targetNamespace = "")
            String arg0,
            @WebParam(name = "arg1", targetNamespace = "")
            String arg1,
            @WebParam(name = "arg2", targetNamespace = "")
            String arg2);

    class MailMain {

        /**
     * 	 * @param args
     * 	 	 */
        public static void main(String[] args) {

            if (args.length < 3) {
                System.out.println("Usage: [topic] [content] [mail]");
                System.exit(1);
          }

            MailUtilWebService service = new MailUtilWebService();
            MailUtilWeb helloProxy = service.getMailUtilWebPort();

            helloProxy.sendmail(args[0], args[1], args[2]);
            System.out.println("send success !!");

                 }

        }
}
