package com.mkyong.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.mkyong.bo.HelloWorldBo;
import com.mkyong.bo.impl.Data;

@WebService
public class HelloWorldWS{

	//DI via Spring
	HelloWorldBo helloWorldBo;

	@WebMethod(exclude=true)
	public void setHelloWorldBo(HelloWorldBo helloWorldBo) {
		this.helloWorldBo = helloWorldBo;
	}

	@WebMethod(operationName="getHelloWorld")
	public String getHelloWorld() {
		
		return helloWorldBo.getHelloWorld();
		
	}

	@WebMethod(operationName = "updateData")
	public Data updateData(Data data) {
		return helloWorldBo.updateData(data);
	}
 
}