package model;


import java.io.Serializable;

public class Usuario implements Serializable
{
	protected Integer origen;
	protected Integer destino;
	protected float hora;
	
	public Usuario(Integer origen, Integer destino, Float hora)
	{
		this.origen=origen;
		this.destino=destino;
		this.hora=hora;
	}
	
	public Integer getOrigen()
	{
		return origen;
	}
	public void setOrigen(Integer origen)
	{
		this.origen = origen;
	}
	public Integer getDestino()
	{
		return destino;
	}
	public void setDestino(Integer destino)
	{
		this.destino = destino;
	}
	public Float getHora()
	{
		return hora;
	}
	public void setHora(Integer hora)
	{
		this.hora = hora;
	}
	

}
