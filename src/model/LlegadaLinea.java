package model;

import java.io.Serializable;

public class LlegadaLinea implements Serializable, Cloneable
{
	protected Float hora;
	protected Integer linea;
	
	public LlegadaLinea(Float hora, Integer linea)
	{
		this.hora=hora;
		this.linea=linea;
	}
	
	public Float getHora()
	{
		return hora;
	}
	public void setHora(Float hora)
	{
		this.hora = hora;
	}
	public Integer getLinea()
	{
		return linea;
	}
	public void setLinea(Integer linea)
	{
		this.linea = linea;
	}
	
	public Object clone()
	{
		LlegadaLinea llegadaLinea=new LlegadaLinea(new Float(hora), new Integer(linea));
		
		return llegadaLinea;
	}
}
