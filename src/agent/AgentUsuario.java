package agent;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import javax.swing.*;
import java.awt.event.*;

import model.Usuario;
import model.Utils;
import model.Recorrido;

public class AgentUsuario extends Agent implements ActionListener{
	
	private JFrame frame = new JFrame();;
	
	private JComboBox<String> origen = new JComboBox<String>();
	private JComboBox<String> destino = new JComboBox<String>();
	private JComboBox<String> hora = new JComboBox<String>();
	
	static String options[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
	
	private JLabel origenLabel = new JLabel("\t\t\tORIGEN");
	private JLabel destinoLabel = new JLabel("\t\t\tDESTINO");
	private JLabel horaLabel = new JLabel("\t\t\tHORA");
	
	private JButton enviar = new JButton("Enviar");;

	
	protected void setup() {
		 
		frame.setTitle(this.getLocalName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 200);
		frame.setResizable(false);
		
		for(int i = 0; i<options.length; i++) {
			origen.addItem(options[i]);
			destino.addItem(options[i]);
			hora.addItem(options[i]);
		}
		
		origen.setBounds(100, 10, 90, 30);
		destino.setBounds(100, 40, 90, 30);
		hora.setBounds(100, 70, 90, 30);
		
		origenLabel.setBounds(0, 10, 100, 30);
		destinoLabel.setBounds(0, 40, 100, 30);
		horaLabel.setBounds(0, 70, 100, 30);
		
		enviar.setBounds(0, 125, 200, 30);
		enviar.addActionListener(this);
		
		frame.add(enviar);
		frame.add(origen);
		frame.add(destino);
		frame.add(hora);
		frame.add(origenLabel);
		frame.add(destinoLabel);
		frame.add(horaLabel);
		
		frame.setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent event) {
		
		if(event.getSource()==enviar) {
			
			int o;
			int d;
			float h;
		
			o = Integer.parseInt(origen.getSelectedItem().toString());
			d = Integer.parseInt(destino.getSelectedItem().toString());
			h = Float.parseFloat(hora.getSelectedItem().toString());
			
			Usuario usuario = new Usuario(o, d, h);
			
			Utils.enviarMensaje(this, "ruta", usuario);
			
			ACLMessage msg=blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			Recorrido recorrido=null;
			try
			{
				recorrido = (Recorrido)msg.getContentObject();
				imprimirRecorrido(recorrido);
				System.out.println("Mensaje Recibido");
				System.out.println(this.getLocalName());
			}
			catch (UnreadableException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void imprimirRecorrido(Recorrido recorridoOptimmo){
		
		String message = "";
		
		message = message.concat("\nRecorrido\n");
		for(int i=0;i<recorridoOptimmo.getParadaRecorrido().size()-1;i++) {
			message=message.concat("Parada: "+recorridoOptimmo.getParadaRecorrido().get(i).getParada()+ " Coger Linea: " + recorridoOptimmo.getParadaRecorrido().get(i).getLlegadaLinea().getLinea() + " Hora: " + recorridoOptimmo.getParadaRecorrido().get(i).getLlegadaLinea().getHora() + "\n");
			
		}
		message = message.concat("Parada: "+recorridoOptimmo.getParadaRecorrido().get(recorridoOptimmo.getParadaRecorrido().size()-1).getParada()+" Llegada: "+recorridoOptimmo.getTiempo());
		System.out.println(message);
		JOptionPane.showMessageDialog(frame, message);
	}
	

}
