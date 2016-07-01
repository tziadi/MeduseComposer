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

import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.lip6.move.meduse.utils.MeduseComposerUtils;


public class MeduseProcessVariantGenerator {

	Document document;
	String idModel = "";
	String nameModel="";
	String idPComponent ;
	String superActiviteDeliveryProcess="_1ZbXUS2NEeadNdxfTHAycw";
	String superActivitePhase= "";
	String superActiviteOther="_1ZbXUS2NEeadNdxfTHAycw";
	String idRessourceManager="";
	String idContent="";

	String breakDowns="";
	Hashtable<String,List<KeyProcessFragment>> allDeltas ;
	
	Hashtable<String, Node> allPhases = new Hashtable<String, Node>();
	Hashtable<String, Node> allActivities= new Hashtable<String, Node>();
	Hashtable<String, String> activitiesPhases = new Hashtable<String, String>();
	//Hashtable<Node, List<Node>> chilsPBreakdowns = new Hashtable<Node, List<Node>>();
	//Hashtable<String, String> breakDonwsOfPhases = new Hashtable<String,String> ();
	
	List<String> breakDonwsOfDeliveryProcesses = new ArrayList<String> ();
	
	Hashtable<String, Node> processElementOfPhases = new Hashtable<String, Node> ();
	




	private List<Element> getProcessFragments(String delta){

		List<Element> fragments = new ArrayList<Element>();



		return fragments;

	}


	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException{


		MeduseComposerUtils.parseDeltaProcesses("Test");
		MeduseComposerUtils.initialiseProcessFragments("Test");
		List<String> deltas = new ArrayList<>();
		//deltas.add("PD RequirementPhase");
		deltas.add("PD_WriteCodePhase");
		deltas.add("PD_WriteCodeUnitTest");
		deltas.add("PD_IntegrationTestPhase");
		//deltas.add("PD_DefineReleasePhase");
		
		MeduseProcessVariantGenerator generator = new MeduseProcessVariantGenerator();

		generator.generate(deltas, "Test", "Test","OUTPUTS", "Test");


	}

	public  void generate(List<String> deltas, String deltasFolderPath, String processesFolderPath, 
			String variantFolderPath, String deliveryProcessPluginPath) throws ParserConfigurationException, SAXException, IOException, TransformerException {


		generateContentFile(variantFolderPath);

		generateModelFile(deltas, deltasFolderPath, processesFolderPath,
				variantFolderPath);	
				updatePluginFile(deltas, deltasFolderPath, processesFolderPath,
		variantFolderPath, deliveryProcessPluginPath);
	}

	private void generateContentFile(String variantFolderPath) {

		final DocumentBuilderFactory factoryContent = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder;
		try {
			builder = factoryContent.newDocumentBuilder();
			Document content= builder.newDocument();
			final Element contentRoot = content.createElement("org.eclipse.epf.uma:DeliveryProcessDescription");
			content.appendChild(contentRoot);			
			contentRoot.setAttribute("xmi:version", "2.0");
			contentRoot.setAttribute("xmlns:xmi", "http://www.omg.org/XMI");
			contentRoot.setAttribute("xmlns:org.eclipse.epf.uma","http://www.eclipse.org/epf/uma/1.0.6/uma.ecore");
			contentRoot.setAttribute("xmlns:epf","http://www.eclipse.org/epf");
			contentRoot.setAttribute("epf:version","1.5.1");
			idContent = "_1ZbXUS2NEeadNdxfTurtt";
			contentRoot.setAttribute("xmi:id", idContent);
			contentRoot.setAttribute("name", nameModel+","+idModel);
			contentRoot.setAttribute("guid", idModel);

			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(content);

			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");			

			//formatage
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			//sortie
			final StreamResult sortie = new StreamResult(new File(variantFolderPath+"/content.xml"));
			transformer.transform(source, sortie);




		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}

				private void updatePluginFile(List<String> deltas,
						String deltasFolderPath, String processesFolderPath,
						String variantFolderPath, String deliveryProcessPluginPath) throws ParserConfigurationException, SAXException, IOException, TransformerException {
					// TODO Auto-generated method stub
					
					
					//1. open the pluginf file
					//upade it.
					
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
		
					// Load the input XML document, parse it and return an instance of the
					// Document class.
					Document documentPlugin = builder.parse(new File(deliveryProcessPluginPath+"/DPplugin.xmi"));
		
					
					NodeList nodeList = documentPlugin.getDocumentElement().getChildNodes();
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						// System.out.println(node.getNodeName());
		
		
						if (node.getNodeName().contains(":ResourceManager")) {
							Element elem = (Element) node;
							
							//Step1. Create subMangers element
							
							String generatedKey = "_1ZbXUS2NEeadNdxfTHAPPP";
							
							final Element subManager = documentPlugin.createElement("subManagers");
							subManager.setAttribute("xmi:id", idModel);// ID of the ResourceManager Element in model.xmi. 
							subManager.setAttribute("href", "uma://"+idPComponent+"#"+idModel);//uma://ID of ProcessComponent Element in model.xmi#ID of the ResourceManager Element in model.xmi
							elem.appendChild(subManager);
						    
						
							//Step 2. Create resourceDescriptors
							
							final Element resourceDesc = documentPlugin.createElement("resourceDescriptors");
							resourceDesc.setAttribute("xmi:id", generatedKey);//ID to be generated
							resourceDesc.setAttribute("id", idPComponent);//ID of ProcessComponent Element in model.xmi
							resourceDesc.setAttribute("uri", "deliveryprocesses/Variant/model.xmi");//Path + nam/model.xmi
							elem.appendChild(resourceDesc);
							
						}
		
							//TO DO
							//Step 3. ajouter childPackage.
							
							//FIND childPackage with name CustomCategories
							//ADD a new Element contentElements.
							
					    if (node.getNodeName().contains("MethodPlugin")) {
								Element method = (Element) node;
							
							final NodeList methodNodes = method.getChildNodes();
							
							for (int j = 0; j < methodNodes.getLength(); j++) {
		
								{
									Node test = methodNodes.item(j);
					                
									if (test.getNodeName().contains("methodPackages")){
										
										Element mp= (Element) test;
										
										if (mp.getAttribute("name").equals("DeliveryProcesses")) {
											
											final Element contentElement = documentPlugin.createElement("childPackages");
											contentElement.setAttribute("xsi:type", "org.eclipse.epf.uma:ProcessComponent");
											
											
											contentElement.setAttribute("xmi:id", idPComponent);//ID of the Process Element in model.xmi
											contentElement.setAttribute("href", "uma://"+idPComponent+"#"+idPComponent);//Path + nam/model.xmi
											
											mp.appendChild(contentElement);
											
										}

									}	
									
								}
									
									
							}		
									
					    }		
					}	
					
					
					final TransformerFactory transformerFactory = TransformerFactory.newInstance();
					final Transformer transformer = transformerFactory.newTransformer();
					final DOMSource source = new DOMSource(documentPlugin);

					final StreamResult sortie = new StreamResult(new File(variantFolderPath+"/PLUG.xml"));
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
					



	private void generateModelFile(List<String> deltas,
			String deltasFolderPath, String processesFolderPath,
			String variantFolderPath) throws ParserConfigurationException,
			SAXException, IOException, TransformerFactoryConfigurationError {
		MeduseComposerUtils.initialiseProcessFragments(processesFolderPath);

		allDeltas =
				MeduseComposerUtils.parseDeltaProcesses(deltasFolderPath);


		List<String> selectedPC = new ArrayList<String>();

		for (String delta : deltas){
		
			List<KeyProcessFragment> liste = allDeltas.get(delta);
			for (KeyProcessFragment kpf:liste )
			selectedPC.add(kpf.getProcessComponentKey());

		}


		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			/*
			 * Etape 2 : cr������ation d'un parseur
			 */
			final DocumentBuilder builder = factory.newDocumentBuilder();

			/*
			 * Etape 3 : cr������ation d'un Document
			 */
			document= builder.newDocument();


			;		    /*
			 * Etape 4 : cr������ation de l'Element racine
			 */
			final Element racine = document.createElement("xml:XMI");
			document.appendChild(racine);			
			racine.setAttribute("xmi:version", "2.0");
			racine.setAttribute("xmlns:xmi", "http://www.omg.org/XMI");
			racine.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
			racine.setAttribute("xmlns:org.eclipse.epf.uma","http://www.eclipse.org/epf/uma/1.0.6/uma.ecore");
			racine.setAttribute("xmlns:org.eclipse.epf.uma.resourcemanager","http:///org/eclipse/epf/uma/resourcemanager.ecore");
			racine.setAttribute("xmlns:epf","http://www.eclipse.org/epf");
			racine.setAttribute("epf:version","1.5.1"); 


			/*
			 * Etape 5 : cr������ation de l'Element org.eclipse.epf.uma.resourcemanager.ResourceManager
			 */

			idModel="_1a6lEC2NEeadNdxfTHAycwP";
			
			nameModel = "Variant";

			final Element resourcemanager = document.createElement("org.eclipse.epf.uma.resourcemanager:ResourceManager");
			resourcemanager.setAttribute("xmi:id", idModel);
			
			
			resourcemanager.setAttribute("guid", idModel);
			resourcemanager.setAttribute("uri", "content.xmi" );

			final Element resourceDesc = document.createElement("resourceDescriptors");

			resourceDesc.setAttribute("xmi:id", "_1a6lES2NEeadNdxfTHAycw" );
			
			resourceDesc.setAttribute("guid", "-tZpZIt_kZXeilm7d80rx7g" );
			resourceDesc.setAttribute("uri", "content.xmi" );
			resourceDesc.setAttribute("id", idContent );
			resourceDesc.setAttribute("xmi:id", "_1a6lES2NEeadNdxfTHAycw" );
			resourcemanager.appendChild(resourceDesc);
			racine.appendChild(resourcemanager);

			final Element processC = document.createElement("org.eclipse.epf.uma.ProcessComponent");
			idPComponent = "_1ZbXUC2NEeadNdxfTHAycw";
			processC.setAttribute("xmi:id", "_1ZbXUC2NEeadNdxfTHAycw" );
		
			processC.setAttribute("name", nameModel );
			processC.setAttribute("guid", "_1ZbXUC2NEeadNdxfTHAycw" );

			final Element methodeEP1 = document.createElement("methodElementProperty");
			methodeEP1.setAttribute("xmi:id", "_1ZbXUi2NEeadNdxfTHAycw" );
			methodeEP1.setAttribute("name", "pkg_loadCheck" );
			methodeEP1.setAttribute("value", "true" );
			processC.appendChild(methodeEP1);


			final Element methodeEP2 = document.createElement("methodElementProperty");
			methodeEP2.setAttribute("xmi:id", "_WOAAoC2OEeadNdxfTHAycw" );
			methodeEP2.setAttribute("name", "me_edited" );
			methodeEP2.setAttribute("value", "true" );
			processC.appendChild(methodeEP2);

			//			 final Element childPackageRoot = document.createElement("childPackages");
			//			 childPackageRoot.setAttribute("xsi:type", "org.eclipse.epf.uma:ProcessPackage" );
			//			 childPackageRoot.setAttribute("xmi:id", "_G736YC2OEeadNdxfTHAycw" );
			//			 childPackageRoot.setAttribute("name", "Variant" );
			//			 childPackageRoot.setAttribute("guid", "_G736YC2OEeadNdxfTHAycw" );

			//INSERT PROCESS FRAGMENT
			//1. Create ChildPackage node
			// 


			//  System.out.println("FOLDER "+processesFolderPath);
			for (String pcName: selectedPC){

				//fin directory

				String dirPC = MeduseComposerUtils.processComponentDirectories.get(pcName);

				System.out.println("Handling ===="+dirPC);
				handleFragments2(dirPC, processC, processesFolderPath, deltas); 	
			}

			// processC.appendChild(childPackageRoot);


			racine.appendChild(processC);

			//END handling process Elements

			final Element process = document.createElement("process");
			process.setAttribute("xsi:type", "org.eclipse.epf.uma:DeliveryProcess" );

			process.setAttribute("xmi:id",superActiviteDeliveryProcess  );
			process.setAttribute("name", "Variant" );
			process.setAttribute("guid", "_1ZbXUS2NEeadNdxfTHAycw" );

			process.setAttribute("presentationName", nameModel );
			
			breakDowns="";
			for(String ph: breakDonwsOfDeliveryProcesses ){
				
				breakDowns= breakDowns+" "+ph+" ";
			}
			process.setAttribute("breakdownElements", breakDowns );

			final Element presentation = document.createElement("presentation");

			presentation.setAttribute("xmi:id", idContent );
			presentation.setAttribute("href", "uma://"+idContent+"#"+idContent );
			process.appendChild(presentation);

			//			 final Element defaultContext = document.createElement("defaultContext");
			//			 defaultContext.setAttribute("href", "uma://_hmtlAG96EdupM6itjmYdSQ#_59JiMB3LEeakwMHbkQwdXQ" );
			//			 process.appendChild(defaultContext);
			//
			//			 final Element valideContext = document.createElement("valideContext");
			//			 valideContext.setAttribute("href", "uma://_hmtlAG96EdupM6itjmYdSQ#_59JiMB3LEeakwMHbkQwdXQ" );
			//			 process.appendChild(valideContext);	

			processC.appendChild(process);
			/*
			 * Etape 8 : affichage
			 */
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(document);

			String srep="./Results/";
			File out_rep=new File(srep);
			out_rep.mkdirs();
			final StreamResult sortie = new StreamResult(new File(variantFolderPath+"/model.xml"));
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
		catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		catch (TransformerException e) {
			e.printStackTrace();
		}
	}


	private void handleFragments2(String pcName, Element owner, String processesFolderPath, List<String> deltas2) 
			throws ParserConfigurationException, SAXException, IOException {
		
		
		
		//parse and read ProcessFragments 	 


				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();

				// Load the input XML document, parse it and return an instance of the
				// Document class.
				System.out.println("PARSING : "+processesFolderPath+"/"+pcName+"/model.xmi");
				
				Document documentVariants = builder.parse(new File(processesFolderPath+"/"+pcName+"/model.xmi"));
				Element childPackge1=null;
				Element childPackge2=null;
				boolean newC=false;

				
				NodeList nodeList = documentVariants.getDocumentElement().getChildNodes();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					// System.out.println(node.getNodeName());


					if (node.getNodeName().contains("ProcessComponent")) {
						Element elem = (Element) node;

						NodeList methodNodes = elem.getChildNodes();
						String processComponentID = elem.getAttribute("xmi:id");
						

						for (int j = 0; j < methodNodes.getLength(); j++) {
						
				       	   Node child = methodNodes.item(j);
				       	   
				       	   
				       	  
				       	   if (child.getNodeName().contains("childPackages") ){
				       		   
				       		Element eChild = (Element)child;
				       		   
				       		String keyCP = eChild.getAttribute("xmi:id");  
				       		
				       		   Node childPackage =  document.importNode(child, false);
				       		   
				       		   NodeList childPackageFils = child.getChildNodes();
				       		   
				       		   for (int k = 0; k < childPackageFils.getLength(); k++) {
				       			   
				       			   Node fils = childPackageFils.item(k);
				       			//   System.out.println("FILS : "+fils.getNodeName());
				       			   
				       			   	if (fils.getNodeName().contains("processElements") ){
				       			   		
				       			   		
										Element pr = (Element) fils;
				       			   		String key = pr.getAttribute("xmi:id");
				       			   		String type = pr.getAttribute("xmi:type");
				       			   		String superActivity= pr.getAttribute("superActivities");
				       			   		
				       			   		
				       			   		if (existProcessFragments(key, deltas2)) {
				       			   			
				       			   		//System.out.println("PROCECCS ELEMENT ADDED");
				       			   			
				       			   			Node processElement =  document.importNode(fils, true); 
				       			   			Element process = (Element) processElement;
				       			   		
				       			   			
				       			   		if (pr.getAttribute("xsi:type").contains("Phase") ){

				       			   		    
											process.setAttribute("superActivities", superActiviteDeliveryProcess);
											//breakDowns=breakDowns+" "+ eChild.getAttribute("xmi:id")+" ";
                                           // process.setAttribute("breakdownElements", breakDowns);

                                            allPhases.put(key, childPackage);
                                            superActivitePhase= process.getAttribute("xmi:id") ;
											//System.out.println("PHASE BREAKDOWSN :"+breakDowns);
											processElementOfPhases.put(keyCP, processElement);
											breakDonwsOfDeliveryProcesses.add(keyCP);
											


										}	
				       			   			
				       			   		if (pr.getAttribute("xsi:type").contains("Activity") ){



											if (superActivitePhase.equals("")) {


												process.setAttribute("superActivities", superActiviteDeliveryProcess);
												//breakDowns=breakDowns+" "+ eChild.getAttribute("xmi:id")+" ";
												breakDonwsOfDeliveryProcesses.add(key);
												

											}
											else {
												
												process.setAttribute("superActivities", superActivitePhase);
												
												allActivities.put(key, childPackage);
												activitiesPhases.put(key,superActivitePhase);
												
												
												
												
												
												}
											}
				       			   			
				       			   			
				       			   		
				       			   		    
				       			   		
				       			   			childPackage.appendChild(processElement);
				       			   			
				       			   			//
				       			   			owner.appendChild(childPackage);
			       			   		
				       			   		}
				       			   		
				       			   	if (existProcessFragments(superActivity, deltas2)) {
			       			   			
				       			   		//System.out.println("PROCECCS ELEMENT ADDED");
				       			   			
				       			   			Node processElement =  document.importNode(fils, true); 
	       			   				
				       			   		Element process = (Element) processElement;
				       			   		
			       			   			
				       			   		if (pr.getAttribute("xsi:type").contains("Phase") ){


											process.setAttribute("superActivities", superActiviteDeliveryProcess);
											//breakDowns=breakDowns+" "+ eChild.getAttribute("xmi:id")+" ";
                                            //process.setAttribute("breakdownElements", breakDowns);

                                            allPhases.put(key, child);
                                            
											superActivitePhase= process.getAttribute("xmi:id") ;
											
											
											processElementOfPhases.put(keyCP, processElement);
											breakDonwsOfDeliveryProcesses.add(keyCP);
											
											


										}	
				       			   			
				       			   		if (pr.getAttribute("xsi:type").contains("Activity") ){



											if (superActivitePhase.equals("")) {


												process.setAttribute("superActivities", superActiviteDeliveryProcess);
												//breakDowns=breakDowns+" "+ eChild.getAttribute("xmi:id")+" ";
												
												

											}
											else {
												
												process.setAttribute("superActivities", superActivitePhase);
												allActivities.put(key, child);
												activitiesPhases.put(key,superActivity);
												
												
												
												
												}
											}
				       			   			
				       			   			childPackage.appendChild(processElement);
				       			   			
				       			   			owner.appendChild(childPackage);
			       			   		
				       			   		}
				       			   		
				       			   		
				       			   		
				       			   	}
				       			   				
				       			   				
				       			   				
				       			   				
				       			  }
				       		   					
				       		   
				       		   
				       			   	
				       	   }
				       	   
				       	  
				       	   }
							
						}
						
					}
				handleParentChildpackages2();
					
				}



	

	
//	private void handleParentChildPackage(){
//		
//		
//		//System.out.println("PHASES :"+allPhases);
//		//System.out.println("ACTIVITIES :"+allActivities);
//		///System.out.println("ACTIVITIES_PHASE :"+activitiesPhases);
//		
//		
//		for(String keyActivities:activitiesPhases.keySet()){
//			
//			
//			String keyPhase = activitiesPhases.get(keyActivities);
//			Node ownerActivity=allActivities.get(keyActivities);
//			Node ownerPhase = allPhases.get(keyPhase);
//			
//			//System.out.println("OWNER PHASE :"+ownerPhase);
//			//System.out.println("OWNER ACTIVITY :"+ownerActivity);
//			
//			if (ownerPhase!=null) ownerPhase.appendChild(ownerActivity);
//			
//		}
//		
//	}
	
private void handleParentChildpackages(){
		
		
		//System.out.println("PHASES :"+allPhases);
		//System.out.println("ACTIVITIES :"+allActivities);
		///System.out.println("ACTIVITIES_PHASE :"+activitiesPhases);
		
		List<String> createdAttributes = new ArrayList<String>();
		
		System.out.println("PROCECCCCCCCCCCCCCCCCCC : "+processElementOfPhases);
		
		for(String keyActivities:activitiesPhases.keySet()){
			
			
			String keyPhase = activitiesPhases.get(keyActivities);
			Node ownerActivity=allActivities.get(keyActivities);
			Node ownerPhase = allPhases.get(keyPhase);
			Element phase = (Element)ownerPhase;
			
			
			
			Node processElementNode = processElementOfPhases.get(keyPhase);
			
			System.out.println("PHASE NODE :"+phase.getAttribute("name")+" ===>" +processElementNode);
			Element processElement = (Element)processElementNode;
			System.out.println("PHASE ELE:"+phase.getAttribute("name")+" ===>" +processElement);
			if (processElement!=null){
			if (!createdAttributes.contains(keyPhase)){
				
				processElement.setAttribute("breakdownElements", keyActivities);
				createdAttributes.add(keyPhase);
			}
			
			else {
				
				String breakD= phase.getAttribute("breakdownElements");
				processElement.setAttribute("breakdownElements", " "+breakD+ " "+keyActivities);
			}
			}
			
			//System.out.println("OWNER PHASE :"+ownerPhase);
			//System.out.println("OWNER ACTIVITY :"+ownerActivity);
			
			if (ownerPhase!=null) ownerPhase.appendChild(ownerActivity);
			
		}
		
	}
	


private void handleParentChildpackages2(){
	
	
	//System.out.println("PHASES :"+allPhases);
	//System.out.println("ACTIVITIES :"+allActivities);
	///System.out.println("ACTIVITIES_PHASE :"+activitiesPhases);
	
	List<String> createdAttributes = new ArrayList<String>();
	
	
	
	for(String keyActivities:activitiesPhases.keySet()){
		
		
		String keyPhase = activitiesPhases.get(keyActivities);
		Node ownerActivity=allActivities.get(keyActivities);
		Node ownerPhase = allPhases.get(keyPhase);
		Element phase = (Element)ownerPhase;
		
		NodeList childPackageFils = ownerPhase.getChildNodes();
		   
		   		   
	    NodeList childOdActivity = ownerActivity.getChildNodes();
	    Node processEAct = childOdActivity.item(0);
	    Element ac = (Element) processEAct;
	    
	    String actKey = ac.getAttribute("xmi:id");
		
		Node processElementNode = childPackageFils.item(0);
		
		
		System.out.println("PHASE NODE :"+phase.getAttribute("name")+" ===>" +processElementNode);
		Element processElement = (Element)processElementNode;
		System.out.println("PHASE ELE:"+phase.getAttribute("name")+" ===>" +processElement);
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
		
		//System.out.println("OWNER PHASE :"+ownerPhase);
		//System.out.println("OWNER ACTIVITY :"+ownerActivity);
		
		if (ownerPhase!=null) ownerPhase.appendChild(ownerActivity);
		
	}
	
}
	
	private void handleFragments(String pcName, Element owner, String processesFolderPath, List<String> deltas2) 
			throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub


		//parse and read ProcessFragments 	 


		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Load the input XML document, parse it and return an instance of the
		// Document class.
		System.out.println("PARSING : "+processesFolderPath+"/"+pcName+"/model.xmi");
		
		Document documentVariants = builder.parse(new File(processesFolderPath+"/"+pcName+"/model.xmi"));
		Element childPackge1=null;
		Element childPackge2=null;
		boolean newC=false;

		
		NodeList nodeList = documentVariants.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			// System.out.println(node.getNodeName());


			if (node.getNodeName().contains("ProcessComponent")) {
				Element elem = (Element) node;


				NodeList methodNodes = elem.getChildNodes();
				String processComponentID = elem.getAttribute("xmi:id");
				//idPComponent=processComponentID;



				for (int j = 0; j < methodNodes.getLength(); j++) {


					Node n = methodNodes.item(j);

					// System.out.println("node "+method );

					if (n.getNodeName().contains("childPackages")) {



						Element e = (Element) n;


						final NodeList liste = e.getChildNodes();

						for (int k = 0; k < liste.getLength(); k++) {
							Node cp = liste.item(k);

							if (cp.getNodeName().contains("processElements") ){
							

								Element pr = (Element) cp;
								
								

								//System.out.println("Create Process Elements");
								String key = pr.getAttribute("xmi:id");
								
								
								if (existProcessFragments(key, deltas2)) {

									//System.out.println("PHASE :"+pr.getAttribute("name")+ " TYPE "+ pr.getAttribute("xsi:type"));
									System.out.println("PROCESS ELEMENT  :"+pr.getAttribute("name")+ " TYPE "+ pr.getAttribute("xsi:type"));

									final Element process = document.createElement("processElements");
									process.setAttribute("xsi:type", pr.getAttribute("xsi:type") );

									if (pr.getAttribute("xsi:type").contains("Phase") ){


										process.setAttribute("superActivities", superActiviteDeliveryProcess);
										breakDowns=breakDowns+" "+ e.getAttribute("xmi:id")+" ";



										superActivitePhase= e.getAttribute("xmi:id") ;
										System.out.println("PHASE BREAKDOWSN :"+breakDowns);


									}

									if (pr.getAttribute("xsi:type").contains("Activity") ){



										if (superActivitePhase.equals("")) {


											process.setAttribute("superActivities", superActiviteDeliveryProcess);
											breakDowns=breakDowns+" "+ e.getAttribute("xmi:id")+" ";
											
											

										}
										else {
											
											process.setAttribute("superActivities", superActivitePhase);
									}
										}
											
									
									
									





									process.setAttribute("xmi:id", pr.getAttribute("xmi:id") );
									process.setAttribute("name", pr.getAttribute("name") );
									process.setAttribute("guid", pr.getAttribute("guid") );
									process.setAttribute("presentationName", pr.getAttribute("presentationName") );
									process.setAttribute("isPlanned", pr.getAttribute("isPlanned") );




									NodeList elements = pr.getChildNodes();

									for (int f = 0; f < elements.getLength(); f++) {

										Node p = elements.item(f);

										System.out.println("PPPPPPPPPPPPPPPPP "+p.getNodeName());
										
										

										if( p.getNodeName().contains(("Role"))){

											System.out.println("Create Role");
											Element fils = (Element) p;

											final Element role = document.createElement("Role");
											role.setAttribute("href", fils.getAttribute("href") );
											process.appendChild(role);

										}
										if( p.getNodeName().contains(("WorkProduct"))){
											System.out.println("Create WorProduct");
											Element fils = (Element) p;

											final Element wp = document.createElement("WorkProduct");
											wp.setAttribute("href", fils.getAttribute("href") );
											process.appendChild(wp);

										}	

										if( p.getNodeName().contains(("Task"))){
											System.out.println("Create WorProduct");
											Element fils = (Element) p;

											final Element wp = document.createElement("Task");
											wp.setAttribute("href", fils.getAttribute("href") );
											process.appendChild(wp);

										}	

										if( p.getNodeName().contains(("selectedSteps"))){
											System.out.println("Create Selected STEP");
											Element fils = (Element) p;

											final Element wp = document.createElement("selectedStep");
											wp.setAttribute("href", fils.getAttribute("href") );
											process.appendChild(wp);

										}	

										if( p.getNodeName().contains(("methodElementProperty"))){
											System.out.println("Create methodElementProperty");
											Element fils = (Element) p;

											final Element wp = document.createElement("methodElementProperty");
											wp.setAttribute("xmi:id", fils.getAttribute("xmi:id") );
											wp.setAttribute("name", fils.getAttribute("name") );
											wp.setAttribute("value", fils.getAttribute("value") );
											process.appendChild(wp);

										}		 


									}

									childPackge1 = document.createElement("childPackages");
									childPackge1.setAttribute("xsi:type", "org.eclipse.epf.uma:ProcessPackage" );
									childPackge1.setAttribute("xmi:id", e.getAttribute("xmi:id") );
									childPackge1.setAttribute("name", e.getAttribute("name") );
									childPackge1.setAttribute("guid", e.getAttribute("guid") );
									childPackge1.appendChild(process);	






								}


								


							}


							//ELSE : case of child packages that contains other child packages


							else {
								childPackge2=null;
							    newC=false;
                  

								if (cp.getNodeName().contains("childPackages") ){
									
									

									Element childPackage = (Element) cp;
									NodeList childList = childPackage.getChildNodes();

									for (int m = 0; m < childList.getLength(); m++) {

										Node p = childList.item(m);


										if (p.getNodeName().contains("processElements")) {

											Element pr2 = (Element)p;

											String key = pr2.getAttribute("xmi:id");
											if (existProcessFragments(key, deltas2)) {

												System.out.println("PHASE :"+pr2.getAttribute("name")+ " TYPE "+ pr2.getAttribute("xsi:type"));


												final Element process2 = document.createElement("processElements");
												process2.setAttribute("xsi:type", pr2.getAttribute("xsi:type") );

												if (pr2.getAttribute("xsi:type").contains("Phase") ){


													process2.setAttribute("superActivities", superActiviteDeliveryProcess);
													breakDowns=breakDowns+" "+ e.getAttribute("xmi:id")+" ";



													superActivitePhase= e.getAttribute("xmi:id") ;
													System.out.println("PHASE BREAKDOWSN :"+breakDowns);


												}

												if (pr2.getAttribute("xsi:type").contains("Activity)") ){



													if (superActivitePhase.equals("")) {


														process2.setAttribute("superActivities", superActiviteDeliveryProcess);
														breakDowns=breakDowns+" "+ e.getAttribute("xmi:id")+" ";

													}
													else 
														process2.setAttribute("superActivities", superActivitePhase);
												}





												process2.setAttribute("xmi:id", pr2.getAttribute("xmi:id") );
												process2.setAttribute("name", pr2.getAttribute("name") );
												process2.setAttribute("guid", pr2.getAttribute("guid") );
												process2.setAttribute("presentationName", pr2.getAttribute("presentationName") );
												process2.setAttribute("isPlanned", pr2.getAttribute("isPlanned") );




												NodeList elements = pr2.getChildNodes();

												for (int f = 0; f < elements.getLength(); f++) {

													Node pp = elements.item(f);



													if( pp.getNodeName().contains(("Role"))){

														System.out.println("Create Role");
														Element fils = (Element) pp;

														final Element role = document.createElement("Role");
														role.setAttribute("href", fils.getAttribute("href") );
														process2.appendChild(role);

													}
													if( pp.getNodeName().contains(("WorkProduct"))){
														System.out.println("Create WorProduct");
														Element fils = (Element) pp;

														final Element wp = document.createElement("WorkProduct");
														wp.setAttribute("href", fils.getAttribute("href") );
														process2.appendChild(wp);

													}	

													if( pp.getNodeName().contains(("Task"))){
														System.out.println("Create WorProduct");
														Element fils = (Element) pp;

														final Element wp = document.createElement("Task");
														wp.setAttribute("href", fils.getAttribute("href") );
														process2.appendChild(wp);

													}	

													if( pp.getNodeName().contains(("selectedSteps"))){
														System.out.println("Create WorProduct");
														Element fils = (Element) pp;

														final Element wp = document.createElement("selectedSteps");
														wp.setAttribute("href", fils.getAttribute("href") );
														process2.appendChild(wp);

													}	

													if( pp.getNodeName().contains(("methodElementProperty"))){
														//System.out.println("Create WorProduct");
														Element fils = (Element) pp;

														final Element wp = document.createElement("methodElementProperty");
														wp.setAttribute("xmi:id", fils.getAttribute("xmi:id") );
														wp.setAttribute("name", fils.getAttribute("name") );
														wp.setAttribute("value", fils.getAttribute("value") );
														process2.appendChild(wp);

													}		 


												}

												childPackge2 = document.createElement("childPackages");
												
												
												newC=true;
												childPackge2.setAttribute("xsi:type", "org.eclipse.epf.uma:ProcessPackage" );
												childPackge2.setAttribute("xmi:id", childPackage.getAttribute("xmi:id") );
												childPackge2.setAttribute("name", childPackage.getAttribute("name") );
												childPackge2.setAttribute("guid", childPackage.getAttribute("guid") );
												childPackge2.appendChild(process2);	

											}

										}
									}
								}
								
								//child2..

							}

							

						}
						
						
					}
					
					
				}
				
				if (childPackge1!=null) {
					
					
					if (newC) {
						System.out.println("CREATE CHILDDDDD");
						childPackge1.appendChild(childPackge2);	
					}
					owner.appendChild(childPackge1);	

				}

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


