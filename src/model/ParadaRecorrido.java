package model;

import java.io.Serializable;

public class ParadaRecorrido implements Serializable, Cloneable
{
	protected Integer parada;
	protected LlegadaLinea llegadaLinea;
	
	public ParadaRecorrido(Integer parada, LlegadaLinea llegadaLinea)
	{
		this.parada=parada;
		this.llegadaLinea=llegadaLinea;
	}
	
	public Integer getParada()
	{
		return parada;
	}
	public void setParada(Integer parada)
	{
		this.parada = parada;
	}
	public LlegadaLinea getLlegadaLinea()
	{
		return llegadaLinea;
	}
	public void setLlegadaLinea(LlegadaLinea llegadaLinea)
	{
		this.llegadaLinea = llegadaLinea;
	}
	
	@Override
	public Object clone()
	{
		ParadaRecorrido paradaRecorrido;
		
		if(llegadaLinea==null)
			paradaRecorrido=new ParadaRecorrido(new Integer(parada), null);
		else
			paradaRecorrido=new ParadaRecorrido(new Integer(parada), (LlegadaLinea)llegadaLinea.clone());
		
		return paradaRecorrido;
	}
}
