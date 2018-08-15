package com.test.rabbitmq;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SendMsg {
	@Autowired
	private RabbitTemplate directTemplate;
	
	public RabbitTemplate getDirectTemplate() {
		return directTemplate;
	}

	public void setDirectTemplate(RabbitTemplate directTemplate) {
		this.directTemplate = directTemplate;
	}

	public void sendObject(String exg, String key,Object obj)
	{
		byte[] data = toByte(obj);
		Message msg = new Message(data, new MessageProperties());
		directTemplate.send(exg, key, msg);
	}
	
	public byte[] toByte(Object obj)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
}
