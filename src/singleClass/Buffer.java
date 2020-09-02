package singleClass;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Buffer
{
	/**
	 * capacidad del buffer
	 */
	private int capacidad;
	/**
	 * el contenedor
	 */
	private ArrayList<Mensaje>buffer;
	/**
	 * objeto para avisar a los mensajes
	 */
	private Object lleno;
	/**
	 * para avisar a los servidores
	 */
	private Object vacio;
	/**
	 * El constructor del buffer
	 * @param capacidad la capacidad del buffer
	 */
	public Buffer(int capacidad) 
	{
		this.capacidad=capacidad;
		buffer= new ArrayList<Mensaje>();
		lleno=new Object();
		vacio=new Object();
	}
	
	public void inputMessage(Mensaje m) throws InterruptedException
	{
		
		if(buffer.size()==capacidad)
		{
			synchronized (lleno)
			{
				lleno.wait();
			}
		}
		synchronized (this)
		{
			buffer.add(m);
		}
		synchronized (vacio) 
		{
			vacio.notify();
		}
		synchronized(m.pendingAns) 
		{
			m.pendingAns.wait();
		}
	}
	
	public Mensaje getMessage() throws InterruptedException
	{
		if(buffer.size()==0)
		{
			synchronized (vacio)
			{
				vacio.wait();
			}
		}		
		Mensaje m;
		synchronized (this)
		{
			m=buffer.remove(0);
		}
		synchronized (lleno) 
		{
			lleno.notify();
		}
		return m;
	}
}
