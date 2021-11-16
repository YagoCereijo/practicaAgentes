package model;

import java.io.Serializable;

public class Conexion implements Serializable
{
	protected Integer parada;
	protected Integer linea;
	
	public Conexion(Integer parada, Integer linea)
	{
		this.parada=parada;
		this.linea=linea;
	}
	
	public Integer getParada()
	{
		return parada;
	}
	public void setParada(Integer parada)
	{
		this.parada = parada;
	}
	public Integer getLinea()
	{
		return linea;
	}
	public void setLinea(Integer linea)
	{
		this.linea = linea;
	}
	
	
}
