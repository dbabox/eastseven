package com.justinmobile.tsm.endpoint.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.stereotype.Service;

import com.justinmobile.tsm.endpoint.webservice.dto.Status;

@WebService(serviceName = "SmsWebService", targetNamespace = NameSpace.CM, portName = "MobileWebServiceHttpPort")
@Service("smsService")
public interface SmsWebService {

	@WebMethod(operationName = "Register")
	@WebResult(name = "Status", targetNamespace = NameSpace.CM)
	public Status register(
			@WebParam(name = "Content", targetNamespace = NameSpace.CM) String content,
			@WebParam(name = "MSISDN", targetNamespace = NameSpace.CM) String mobileNo
			);
}
