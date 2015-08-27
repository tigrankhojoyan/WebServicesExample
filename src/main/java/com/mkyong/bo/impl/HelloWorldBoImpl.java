package com.mkyong.bo.impl;

import com.mkyong.bo.HelloWorldBo;

public class HelloWorldBoImpl implements HelloWorldBo{

	public String getHelloWorld(){
		return "JAX-WS + Spring!";
	}

	@Override
	public Data updateData(Data data) {
		data.setIntData(data.getIntData() + 10);
		data.setStringData("The '" + data.getStringData() + "' data has been given.");
		return data;
	}

}