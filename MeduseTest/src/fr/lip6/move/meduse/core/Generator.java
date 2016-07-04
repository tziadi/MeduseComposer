package fr.lip6.move.meduse.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.lip6.move.meduse.utils.MeduseComposerUtils;

public class Generator {

	private Hashtable<String, Node> childpackages = new Hashtable<String, Node>();
	
	Document document;
	Document documentVariant ;
	Node processNode;
	Element processNodeElement;
	
	Node PCNode;
	
	String idModel = "";
	String nameModel="";
	String idPComponent ;
	String superActiviteDeliveryProcess="";
	String superActivitePhase= "";
	String superActiviteOther="_1ZbXUS2NEeadNdxfTHAycw";
	String idRessourceManager="";
	String idContent="";

	String breakDowns="";
	Hashtable<String,List<KeyProcessFragment>> allDeltas ;
	
	Hashtable<String, Node> allPhases = new Hashtable<String, Node>();
	Hashtable<String, Node> allActivities= new Hashtable<String, Node>();
	Hashtable<String, String> activitiesPhases = new Hashtable<String, String>();
	
String breakDonwsOfDeliveryProcesses = "";
	
	Hashtable<String, Node> processElementOfPhases = new Hashtable<String, Node> ();
	
	
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException{


		MeduseComposerUtils.parseDeltaProcesses("Test");
		MeduseComposerUtils.initialiseProcessFragments("Test");
		List<String> deltas = new ArrayList<>();
		//deltas.add("PD RequirementPhase");
		deltas.add("PD_WriteCodePhase");
		deltas.add("PD_WriteCodeUnitTest");
		deltas.add("PD_IntegrationTestPhase");
		//deltas.add("PD_DefineReleasePhase");
		
		Generator generator = new Generator();
		
		

		generator.generateModelFile(deltas, "Test", "Test","OUTPUTS");


	}
	
	private void generateModelFile(List<String> deltas,
			String deltasFolderPath, String processesFolderPath,
			String variantFolderPath) throws ParserConfigurationException,
			SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		
		MeduseComposerUtils.initialiseProcessFragments(processesFolderPath);

		allDeltas =
				MeduseComposerUtils.parseDeltaProcesses(deltasFolderPath);


		List<String> selectedPC = new ArrayList<String>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Load the input XML document, parse it and return an instance of the
		// Document class.
		System.out.println("PARSING : "+processesFolderPath+"/"+"DP5.xmi");
		
		documentVariant = builder.parse(new File(processesFolderPath+"/"+"DP5.xmi"));

		for (String delta : deltas){
		
			List<KeyProcessFragment> liste = allDeltas.get(delta);
			for (KeyProcessFragment kpf:liste )
			selectedPC.add(kpf.getProcessComponentKey());

		}
		
		
		
		
		
		
		NodeList nodeList = documentVariant.getDocumentElement().getChildNodes();
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			
			
			Node processComponentNode = nodeList.item(i);
			
			System.out.println("SUITE :"+ i+  "   "+processComponentNode.getNodeName());
		
			if (processComponentNode.getNodeName().contains("ProcessComponent")) {
				PCNode = processComponentNode;
				System.out.println("RENTRE :"+i);
				
				Element processComponent = (Element) processComponentNode;

				NodeList processComponentsChild = processComponent.getChildNodes();
				
				
				//STEP1: delete the old child.
				
				String processComponentID = processComponent.getAttribute("xmi:id");
				
				//Node childPackage = findChildPackage(processComponent);
               Node childPackage = processComponentsChild.item(5);
                processNode = processComponentsChild.item(7);
                 processNodeElement = (Element) processNode;
                superActiviteDeliveryProcess =processNodeElement.getAttribute("xmi:id");
                		
                		
               processComponentNode.removeChild(childPackage);
                
                //STEP2: add childPackage from..
                
                
               // System.out.println("STEP2 ===");
                
                //System.out.println("PROCESS === "+processNode.getNodeName());
               
//                for (String key : childpackages.keySet()){
//                	System.out.println("Traitement KEYYYYYY "+key);
//                	
//                	Node childP = childpackages.get(key);
//                	 Node child =  documentVariant.importNode(childP, true);
//                	 processComponentNode.insertBefore(childP, processNode);
//                	 //processComponentNode.appendChild(child);
//                	
//                }
                
                
			
			}
		
		
		}
		for (String pcName: selectedPC){

			//fin directory

			String dirPC = MeduseComposerUtils.processComponentDirectories.get(pcName);

			System.out.println("Handling ===="+dirPC);
			handleFragments2(dirPC, processesFolderPath, deltas); 	
		}
		
		// 
		
		
		processNodeElement.setAttribute("breakdownElements", breakDonwsOfDeliveryProcesses);
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		final Transformer transformer = transformerFactory.newTransformer();
		final DOMSource source = new DOMSource(documentVariant);

		final StreamResult sortie = new StreamResult(new File(variantFolderPath+"/NEWDP5.xml"));
		//final StreamResult result = new StreamResult(System.out);

		//prologue
		transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");			

		//formatage
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		//sortie
		transformer.transform(source, sortie);	
		
		
	}
	
	
	


	private void handleFragments2(String pcName,  String processesFolderPath, List<String> deltas2) 
			throws ParserConfigurationException, SAXException, IOException {
		
		
		
		//parse and read ProcessFragments 	 


				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();

				// Load the input XML document, parse it and return an instance of the
				// Document class.
				System.out.println("PARSING : "+processesFolderPath+"/"+pcName+"/model.xmi");
				
				Document documentFragment = builder.parse(new File(processesFolderPath+"/"+pcName+"/model.xmi"));
				Element childPackge1=null;
				Element childPackge2=null;
				boolean newC=false;

				
				NodeList nodeList = documentFragment.getDocumentElement().getChildNodes();
				
				for (int i = 0; i < nodeList.getLength(); i++) {
				
					Node node = nodeList.item(i);
					
					if (node.getNodeName().contains("ProcessComponent")) {
						Element elem = (Element) node;

						NodeList methodNodes = elem.getChildNodes();
						String processComponentID = elem.getAttribute("xmi:id");
						

						for (int j = 0; j < methodNodes.getLength(); j++) {
						
				       	  Node child = methodNodes.item(j);
				       	   
				       	  if (child.getNodeName().contains("childPackages") ){
				       		   
				       		Element eChild = (Element)child;
				       		   
				       		String keyCP = eChild.getAttribute("xmi:id");  
				       		
				       		   Node childPackage =  documentVariant.importNode(child, false);
				       		   
				       		   NodeList childPackageFils = child.getChildNodes();
				       		   
				       		   for (int k = 0; k < childPackageFils.getLength(); k++) {
				       			   
				       			   Node fils = childPackageFils.item(k);
				       			//   System.out.println("FILS : "+fils.getNodeName());
				       			   
				       			   	if (fils.getNodeName().contains("processElements") ){
				       			   		
				       			   		
										Element pr = (Element) fils;
				       			   		String key = pr.getAttribute("xmi:id");
				       			   		String type = pr.getAttribute("xmi:type");
				       			   		String superActivity= pr.getAttribute("superActivities");
				       			   		
				       			   		
				       			   		if (existProcessFragments(key, deltas2) || existProcessFragments(superActivity, deltas2)) {
				       			   			
				       			   		//System.out.println("PROCECCS ELEMENT ADDED");
				       			   			
				       			   			Node processElement =  documentVariant.importNode(fils, true); 
				       			   			Element process = (Element) processElement;
				       			   		
				       			   			
				       			   		if (pr.getAttribute("xsi:type").contains("Phase") ){

				       			   		    
											process.setAttribute("superActivities", superActiviteDeliveryProcess);
											//breakDowns=breakDowns+" "+ eChild.getAttribute("xmi:id")+" ";
                                           // process.setAttribute("breakdownElements", breakDowns);

                                            allPhases.put(key, childPackage);
                                            superActivitePhase= process.getAttribute("xmi:id") ;
                                            
                                            
											//System.out.println("PHASE BREAKDOWSN :"+breakDowns);
											processElementOfPhases.put(keyCP, processElement);
											breakDonwsOfDeliveryProcesses=breakDonwsOfDeliveryProcesses+ " "+key+ " ";
											


										}	
				       			   			
				       			   		if (pr.getAttribute("xsi:type").contains("Activity") ){



											if (superActivitePhase.equals("")) {


												process.setAttribute("superActivities", superActiviteDeliveryProcess);
												//breakDowns=breakDowns+" "+ eChild.getAttribute("xmi:id")+" ";
												breakDonwsOfDeliveryProcesses=breakDonwsOfDeliveryProcesses+ " "+key+ " ";
												

											}
											else {
												
												process.setAttribute("superActivities", superActivitePhase);
												
												allActivities.put(key, childPackage);
												activitiesPhases.put(key,superActivitePhase);
												
												
												
												
												
												}
											}
				       			   			
				       			   			
				       			   		
				       			   		    
				       			   		
				       			   			childPackage.appendChild(processElement);
				       			   		PCNode.insertBefore(childPackage, processNode);
				       			   			
				       			   			//PCNode.appendChild(childPackage);
				       			   			
				       			   			//
				       			   			//owner.appendChild(childPackage);
				       			   		//childpackages.put(keyCP, childPackage);
				       			   		}
				       			   		
				       			   
				       			   		
				       			   		
				       			   		
				       			   	}
				       			   				
				       			   				
				       			   				
				       			   				
				       			  }
				       		   					
				       		   
				       		   
				       			   	
				       	   }
				       	   
				       	  
				       	   }
							
						}
						
					}
				handleParentChildpackages2();
					
				}
	
	
	private void handleParentChildpackages2(){
		
		
//		System.out.println("PHASES :"+allPhases);
//		System.out.println("ACTIVITIES :"+allActivities);
//		System.out.println("ACTIVITIES_PHASE :"+activitiesPhases);
//		System.out.println("CHAILD PACKAGES :"+childpackages);
		
		List<String> createdAttributes = new ArrayList<String>();
		
		
		
		for(String keyActivities:activitiesPhases.keySet()){
			
			
			String keyPhase = activitiesPhases.get(keyActivities);
			Node ownerActivity=allActivities.get(keyActivities);
			Element ownerActivityElement = (Element)ownerActivity;
			
			
			Node ownerPhase = allPhases.get(keyPhase);
			Element phase = (Element)ownerPhase;
			
			NodeList childPackageFils = ownerPhase.getChildNodes();
			   
			   		   
		    NodeList childOdActivity = ownerActivity.getChildNodes();
		    Node processEAct = childOdActivity.item(0);
		    Element ac = (Element) processEAct;
		    
		    String actKey = ac.getAttribute("xmi:id");
			
			Node processElementNode = childPackageFils.item(0);
			String keyToRemove;
			
			//System.out.println("PHASE NODE :"+phase.getAttribute("name")+" ===>" +processElementNode);
			Element processElement = (Element)processElementNode;
			
			 Node parent = processElement.getParentNode();
			  Element parentElement = (Element) parent;
			  keyToRemove = parentElement.getAttribute("xmi:id");
		      System.out.println ("TO REMOVE :"+keyToRemove);
		      
			//System.out.println("PHASE ELE:"+phase.getAttribute("name")+" ===>" +processElement);
			if (processElement!=null){
				
			 
			if (!createdAttributes.contains(keyPhase)){
				
				processElement.setAttribute("breakdownElements", actKey);
				createdAttributes.add(keyPhase);
			}
			
			else {
				
				String breakD= phase.getAttribute("breakdownElements");
				processElement.setAttribute("breakdownElements", " "+breakD+ " "+actKey);
			}
			}
			
			System.out.println("OWNER PHASE :"+ownerPhase);
			System.out.println("OWNER ACTIVITY :"+ownerActivity);
			
			if (ownerPhase!=null) {
				
				System.out.println("EXIST CHECK :"+allActivities.get(keyToRemove));
				System.out.println("REMOVE ======= "+keyToRemove);
				ownerPhase.appendChild(ownerActivity);
				
				
				//childpackages.remove(keyToRemove);
			}
			
		}
		
	}
		
	
	
	private boolean existProcessFragments(String key, List<String> deltas2) {
		// TODO Auto-generated method stub

		
		
		for (String delta:deltas2){
			//System.out.println("SEARCHING KEY "+key+ " FOR DELTA :"+delta);
			List<KeyProcessFragment> liste = allDeltas.get(delta);
			
			for (KeyProcessFragment kpc: liste)
				    if(kpc.getProcessElementKey().equals(key)) {
				//    	System.out.println("KEY = "+key + " EXIST IN ==> "+delta);
				    	return true;
				    }

			
		}


		return false;
	}


	public void addProcessFragments(String delta){

		// Create childPackges
		//add attributes
		// create process Elemen
	}
}

	

