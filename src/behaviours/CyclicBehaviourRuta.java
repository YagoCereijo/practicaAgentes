package behaviours;

import java.io.IOException;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import model.*;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * Tiene informaci�n de las l�neas y la hora de inicio de cada recorrido.
 * @author Fran
 *
 */
public class CyclicBehaviourRuta extends CyclicBehaviour
{
	/**
	 * Clave parada, hora de llegada/salida y la linea
	 */
	protected Hashtable<Integer, Vector<LlegadaLinea>> hashtableParadas;
	
	/**
	 * Clave parada origen, parada destino y linea
	 */
	protected Hashtable<Integer, Vector<Conexion>> hashtableGrafo;
	protected Recorrido recorridoOptimmo;
	protected Float tiempos[][];
	
	public CyclicBehaviourRuta(Agent agent)
	{
		super(agent);
		
		hashtableParadas = new Hashtable<Integer, Vector<LlegadaLinea>>();
		hashtableGrafo = new Hashtable<Integer, Vector<Conexion>>();
		tiempos = new Float[14][14];
		
	}
	
	@Override
	public void action()
	{
        ACLMessage msg=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        try
		{
        	
    		//mandar mensajes a todas las l�neas
        	Utils.enviarMensaje(this.myAgent, "linea", null);
			
        	//esperar que me lleguen los tiempos de cada l�nea, se filtra por l�nea
        	//para evitar recibir dos l�neas 1 si hay dos comportmientos preguntando de modo
        	//concurrente
        	ACLMessage msgLineaA=this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("ontologia1")));
        	ACLMessage msgLineaB=this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("ontologia2")));
        
        	//construir el mapa de paradas con los tiempos de llegadas a cada parada
    		//calculo tablas de llegada a cada parada
        	Linea linea=(Linea)msgLineaA.getContentObject();
    		agregarhorarioLinea(linea.getHorarioParada());
    		cargarMatrizTiempos(linea.getParada(), linea.getTiempo());
    		crearGrafo(linea.getParada(), linea.getLinea());
    		
    		linea=(Linea)msgLineaB.getContentObject();
    		agregarhorarioLinea(linea.getHorarioParada());
    		cargarMatrizTiempos(linea.getParada(), linea.getTiempo());
    		crearGrafo(linea.getParada(), linea.getLinea());

    		imprimirGrafo();
    		imprimirTablasHorarias();
        	
        	
    		//calcular el recorrido m�s corto
    		Usuario usuario=(Usuario)msg.getContentObject();
    		Recorrido recorrido=new Recorrido();
    		recorrido.setTiempo(usuario.getHora());
    		recorrido.getParadaRecorrido().add(new ParadaRecorrido(usuario.getOrigen(), null));
    		calcularRecorridoMinimo(recorrido, usuario.getDestino());

    		
    		
    		//devolver el resultado al usuario
           	ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
       		aclMessage.addReceiver(msg.getSender());
            aclMessage.setOntology("ontologia");
            //el lenguaje que se define para el servicio
            aclMessage.setLanguage(new SLCodec().getName());
            //el mensaje se transmita en XML
            aclMessage.setEnvelope(new Envelope());
			//cambio la codificacion de la carta
			aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
            //aclMessage.getEnvelope().setAclRepresentation(FIPANames.ACLCodec.XML); 
    		aclMessage.setContentObject((Serializable)recorridoOptimmo);
    		this.myAgent.send(aclMessage);  
   		
    		recorridoOptimmo=null;
			
		}
		catch (UnreadableException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	public static void main(String args[])
	{
		CyclicBehaviourRuta cyclicBehaviourRuta=new CyclicBehaviourRuta(null);
		cyclicBehaviourRuta.pruebaSistema();
	}
	
	public void pruebaSistema()
	{
		CyclicBehaviourLinea cyclicBehaviourLinea1=new CyclicBehaviourLinea1(null);
		CyclicBehaviourLinea cyclicBehaviourLinea2=new CyclicBehaviourLinea2(null);
		
		//calculo tablas de llegada a cada parada
		agregarhorarioLinea(cyclicBehaviourLinea2.getHorarioParada());
		cargarMatrizTiempos(cyclicBehaviourLinea2.getParada(), cyclicBehaviourLinea2.getTiempo());
		crearGrafo(cyclicBehaviourLinea2.getParada(), 2);
		agregarhorarioLinea(cyclicBehaviourLinea1.getHorarioParada());
		cargarMatrizTiempos(cyclicBehaviourLinea1.getParada(), cyclicBehaviourLinea1.getTiempo());
		crearGrafo(cyclicBehaviourLinea1.getParada(), 1);
		
		imprimirGrafo();
		imprimirTablasHorarias();
		
		//calculo la ruta m�s corta
		Recorrido recorrido=new Recorrido();
		recorrido.setTiempo(9.0f);
		recorrido.getParadaRecorrido().add(new ParadaRecorrido(1, null));
		calcularRecorridoMinimo(recorrido, 10);
		//calcularRecorridoMinimo(recorrido, 3);
		
		imprimirRecorrido();
	}
	
	//validada
	public void cargarMatrizTiempos(Integer parada[], Float tiempo[])
	{
		for(int i=1;i<parada.length;i++)
			tiempos[parada[i-1]][parada[i]]=tiempo[i-1];
	}
	
	//validada
	public void crearGrafo(Integer parada[], Integer linea)
	{
		for(int i=0;i<parada.length-1;i++)
			if(hashtableGrafo.get(parada[i])==null)
			{
				Vector<Conexion> vectorConexiones=new Vector<Conexion>();
				vectorConexiones.add(new Conexion(parada[i+1], linea));
				hashtableGrafo.put(parada[i], vectorConexiones);
			}
			else
			{
				Vector<Conexion> vectorConexiones=hashtableGrafo.get(parada[i]);
				vectorConexiones.add(new Conexion(parada[i+1], linea));
				hashtableGrafo.put(parada[i], vectorConexiones);
			}	
	}
	
	/**
	 * Construye el grafo de paradas
	 * @param hashtableParadas
	 */
	public void agregarhorarioLinea(Hashtable<Integer, Vector<LlegadaLinea>> hashtableParadas)
	{
		Enumeration<Integer> claves=hashtableParadas.keys();
		Integer clave;
		
		while(claves.hasMoreElements())
		{
			clave=claves.nextElement();
			
			if(this.hashtableParadas.get(clave)==null)
				this.hashtableParadas.put(clave, hashtableParadas.get(clave));
			else
				this.hashtableParadas.get(clave).addAll(hashtableParadas.get(clave));
		}
	}
	
	/**
	 * Calcula recursivamente el recorrido m�s corto, no es ninguna implementaci�n eficiente, es la m�s
	 * sencilla que hab�a para los datos que se ten�an
	 * @param recorrido Se pasar� una variable creada y se almacenar� el recorrido parcial que se va probando.Inicialmente 
	 * contendr� la parada de inicio y la hora a la que llega la persona a esa parada
	 */
	protected void calcularRecorridoMinimo(Recorrido recorrido, Integer paradaFinal)
	{
		Integer origenParada=recorrido.getParadaRecorrido().get(recorrido.getParadaRecorrido().size()-1).getParada();
		Vector<Conexion> vectorParadas=hashtableGrafo.get(origenParada);
		
		//si he llegado a la parada de destino paro la recursividad y guardo la soluci�n si es m�s eficiente
		if(origenParada.compareTo(paradaFinal)==0)
		{
			if(recorridoOptimmo==null || recorridoOptimmo.getTiempo()>recorrido.getTiempo())
			{
				if(recorridoOptimmo==null)
				{
					recorridoOptimmo=new Recorrido();
					recorridoOptimmo.setParadaRecorrido(new Vector<ParadaRecorrido>());
				}
				else
					recorridoOptimmo.getParadaRecorrido().removeAllElements();
				
				for(int i=0;i<recorrido.getParadaRecorrido().size();i++)
				{
					recorridoOptimmo.getParadaRecorrido().add((ParadaRecorrido)recorrido.getParadaRecorrido().get(i).clone());
				}
				
				recorridoOptimmo.setTiempo(recorrido.getTiempo());
				
			}
			
			return;
		}

		//ya no hay m�s conexiones
		if(vectorParadas==null)
			return;

		
		//recorro todos los buses que salen desde esa parada
		Vector<LlegadaLinea> vectorLlegadaLinea=hashtableParadas.get(origenParada);
		for(int i=0;i<vectorLlegadaLinea.size();i++)
		{
			if(recorrido.getTiempo()<=vectorLlegadaLinea.get(i).getHora())
			{
				recorrido.getParadaRecorrido().get(recorrido.getParadaRecorrido().size()-1).setLlegadaLinea(vectorLlegadaLinea.get(i));
				
				for(int j=0;j<vectorParadas.size();j++)
				{

					//tengo que coger del vector solo aquellas paradas de destino que 
					//realmente vaya a linea que haya cogido
					if(vectorParadas.get(j).getLinea()!=vectorLlegadaLinea.get(i).getLinea())
						continue;
						
					Integer destinoParada=vectorParadas.get(j).getParada();
					Float tiempoAnterior;
					
					recorrido.getParadaRecorrido().add(new ParadaRecorrido(destinoParada, null));
					tiempoAnterior=recorrido.getTiempo();
					recorrido.setTiempo(vectorLlegadaLinea.get(i).getHora()+tiempos[origenParada][destinoParada]);
					
					calcularRecorridoMinimo(recorrido, paradaFinal);
					recorrido.getParadaRecorrido().remove(recorrido.getParadaRecorrido().size()-1);
					recorrido.setTiempo(tiempoAnterior);
					
				}
			}
		}
	}
	
	
	public void imprimirGrafo()
	{
		Enumeration<Integer> claves=hashtableGrafo.keys();
		Integer clave;
		
		System.out.println("\nGrafo Paradas\n");
		while(claves.hasMoreElements())
		{
			clave=claves.nextElement();
			System.out.print("\n"+clave+" ");
			for(int i=0;i<hashtableGrafo.get(clave).size();i++)
				System.out.print(hashtableGrafo.get(clave).get(i)+" ");
		}

	}
	
	public void imprimirTablasHorarias()
	{
		Enumeration<Integer> claves=hashtableParadas.keys();
		Integer clave;
		Vector<LlegadaLinea> vectorLlegadaLinea;
		
		while(claves.hasMoreElements())
		{
			clave=claves.nextElement();
			System.out.println("\n\nParada "+clave);
			vectorLlegadaLinea=hashtableParadas.get(clave);
			for(int i=0;i<vectorLlegadaLinea.size();i++)
			{
				System.out.println(vectorLlegadaLinea.get(i).getLinea()+" "+vectorLlegadaLinea.get(i).getHora());
			}
			
		}
	}
	
	public void imprimirRecorrido()
	{
		System.out.println("\nRecorrido\n");
		for(int i=0;i<recorridoOptimmo.getParadaRecorrido().size()-1;i++)
			System.out.println("Parada: "+recorridoOptimmo.getParadaRecorrido().get(i).getParada()+" Coger Linea: "+recorridoOptimmo.getParadaRecorrido().get(i).getLlegadaLinea().getLinea()+" Hora: "+recorridoOptimmo.getParadaRecorrido().get(i).getLlegadaLinea().getHora());
		System.out.println("Parada: "+recorridoOptimmo.getParadaRecorrido().get(recorridoOptimmo.getParadaRecorrido().size()-1).getParada()+" Llegada: "+recorridoOptimmo.getTiempo());
		
	}

}
