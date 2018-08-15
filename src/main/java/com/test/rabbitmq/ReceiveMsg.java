package com.test.rabbitmq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.test.bean.S2HInfo;
import com.test.bean.StudentInfo;
import com.test.service.IStudentService;

@Service
public class ReceiveMsg implements ChannelAwareMessageListener{
	@Autowired
	private IStudentService serv;
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		try
		{
			byte[] b = message.getBody();
			Object obj = toObject(b);
			if(obj != null)
			{
				if(obj instanceof StudentInfo)
				{
					StudentInfo si = (StudentInfo)obj;
					System.out.println("Receive Object = "+si);
					System.out.println("Receive hid = "+si.getHid());
					String hid = si.getHid();
					si = serv.saveStu(si);
					Integer sid = si.getId();
					System.out.println("dubbo sid.id="+sid);
					if(hid != null)
					{
						String[] dim = hid.split(",");
						for(String hid2:dim)
						{
							S2HInfo s2h = new S2HInfo();
							s2h.setHid(Integer.parseInt(hid2));
							s2h.setSid(sid);
							serv.saveS2H(s2h);
							
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			long tag = message.getMessageProperties().getDeliveryTag();
			channel.basicAck(tag,false);
		}
	}
	
	public Object toObject(byte[] data)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		try
		{
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
