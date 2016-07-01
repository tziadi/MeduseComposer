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
		 
		 String breakDowns="";
		 
		 Hashtable<String,List<String>> allDeltas;
		 
	
		 
		
		
		private List<Element> getProcessFragments(String delta){
			
			List<Element> fragments = new ArrayList<Element>();
			
			
			
			return fragments;
			
		}
		
	    public  void generate(List<String> deltas, String deltasFolderPath, String processesFolderPath, 
	    		String variantFolderPath, String deliveryProcessPluginPath) throws ParserConfigurationException, SAXException, IOException {
	        	
	    	
	    	generateModelFile(deltas, deltasFolderPath, processesFolderPath,
					variantFolderPath);	
	    	generateContentFile(variantFolderPath);
	    	//updatePluginFile(deltas, deltasFolderPath, processesFolderPath,
				//	variantFolderPath, deliveryProcessPluginPath);
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
			    contentRoot.setAttribute("xmi:id", idModel);
			    contentRoot.setAttribute("xmi:name", nameModel);
			    contentRoot.setAttribute("guid", nameModel+","+idModel);
			    
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

//		private void updatePluginFile(List<String> deltas,
//				String deltasFolderPath, String processesFolderPath,
//				String variantFolderPath, String deliveryProcessPluginPath) {
//			// TODO Auto-generated method stub
//			
//			
//			//1. open the pluginf file
//			//upade it.
//			
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//
//			// Load the input XML document, parse it and return an instance of the
//			// Document class.
//			Document documentPlugin = builder.parse(new File(deliveryProcessPluginPath+"/plugin.xmi"));
//
//			
//			NodeList nodeList = documentPlugin.getDocumentElement().getChildNodes();
//			for (int i = 0; i < nodeList.getLength(); i++) {
//				Node node = nodeList.item(i);
//				// System.out.println(node.getNodeName());
//
//
//				if (node.getNodeName().contains(":ResourceManager")) {
//					Element elem = (Element) node;
//					
//					//Step1. Create subMangers element
//					
//					final Element subManager = documentPlugin.createElement("subManagers");
//					subManager.setAttribute("xmi:id", idModel);// ID of the ResourceManager Element in model.xmi. 
//					subManager.setAttribute("href", idModel);//uma://ID of ProcessComponent Element in model.xmi#ID of the ResourceManager Element in model.xmi
//					elem.appendChild(subManager);
//				    
//					//Step 2. Create resourceDescriptors
//					
//					final Element resourceDesc = documentPlugin.createElement("resourceDescriptors");
//					resourceDesc.setAttribute("xmi:id", idModel);//ID of the Process Element in model.xmi
//					resourceDesc.setAttribute("id", idModel);//ID of ProcessComponent Element in model.xmi
//					resourceDesc.setAttribute("uri", idModel);//Path + nam/model.xmi
//					elem.appendChild(resourceDesc);
//					
//				
//
//					//TO DO
//					//Step 3. ajouter childPackage.
//					
//					//FIND childPackage with name CustomCategories
//					//ADD a new Element contentElements.
//					
//			    if (node.getNodeName().contains("MethodPlugin")) {
//						Element method = (Element) node;
//					
//					final NodeList methodNodes = method.getChildNodes();
//					for (int j = 0; j < methodNodes.getLength(); j++) {
//
//						{
//							Node test = methodNodes.item(j);
//			
//							
//							
//							final Element contentElement = documentPlugin.createElement("contentElement");
//							contentElement.setAttribute("xmi:type", "org.eclipse.epf.uma.CustomCategory");
//							contentElement.setAttribute("xmi:id", idModel);//ID of the Process Element in model.xmi
//							contentElement.setAttribute("id", idModel);//ID of ProcessComponent Element in model.xmi
//							contentElement.setAttribute("uri", idModel);//Path + nam/model.xmi
//							
//							
//							elem.appendChild(resourceDesc);
//							
//						}
//					}
//					
//				}
//			
//			
		

		private void generateModelFile(List<String> deltas,
				String deltasFolderPath, String processesFolderPath,
				String variantFolderPath) throws ParserConfigurationException,
				SAXException, IOException, TransformerFactoryConfigurationError {
			MeduseComposerUtils.initialiseProcessFragments(processesFolderPath);
	    	
	    	 allDeltas =MeduseComposerUtils.parseDeltaProcesses(deltasFolderPath);
	    	
	    	
	    	ArrayList<String> selectedPC = new ArrayList<String>();
	    	
	    	for (String delta : deltas)
	    		       selectedPC.add(allDeltas.get(delta).get(0));
	    	
	    	
	    	
	    
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
		try {
		    /*
		     * Etape 2 : cr��ation d'un parseur
		     */
		    final DocumentBuilder builder = factory.newDocumentBuilder();
		    		
		    /*
		     * Etape 3 : cr��ation d'un Document
		     */
		    document= builder.newDocument();
			
		   
;		    /*
		     * Etape 4 : cr��ation de l'Element racine
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
		     * Etape 5 : cr��ation de l'Element org.eclipse.epf.uma.resourcemanager.ResourceManager
		     */
			
			idModel="_1a6lEC2NEeadNdxfTHAycwP";
			nameModel = "Variant";
			
		    final Element resourcemanager = document.createElement("org.eclipse.epf.uma.resourcemanager:ResourceManager");
		    resourcemanager.setAttribute("xmi:id", idModel);
		    resourcemanager.setAttribute("guid", "_1a6lEC2NEeadNdxfTHAycw" );
		    resourcemanager.setAttribute("uri", "content.xmi" );
		    
		    final Element resourceDesc = document.createElement("resourceDescriptors");
		    
		    resourceDesc.setAttribute("xmi:id", "_1a6lES2NEeadNdxfTHAycw" );
		    resourceDesc.setAttribute("guid", "-tZpZIt_kZXeilm7d80rx7g" );
		    resourceDesc.setAttribute("uri", "content.xmi" );
		    resourcemanager.appendChild(resourceDesc);
		    racine.appendChild(resourcemanager);
		    
		    final Element processC = document.createElement("org.eclipse.epf.uma.ProcessComponent");
		    idPComponent = "_1ZbXUC2NEeadNdxfTHAycwUUUU";
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
		    
		    final Element childPackageRoot = document.createElement("childPackages");
		    childPackageRoot.setAttribute("xsi:type", "org.eclipse.epf.uma:ProcessPackage" );
		    childPackageRoot.setAttribute("xmi:id", "_G736YC2OEeadNdxfTHAycw" );
		    childPackageRoot.setAttribute("name", "Variant" );
		    childPackageRoot.setAttribute("guid", "_G736YC2OEeadNdxfTHAycw" );
		    
		    //INSERT PROCESS FRAGMENT
		    //1. Create ChildPackage node
		    // 
		    
		    
		  //  System.out.println("FOLDER "+processesFolderPath);
		    for (String pcName: selectedPC){
		    	
		    	//fin directory
		    	
		    	String dirPC = MeduseComposerUtils.processComponentDirectories.get(pcName);
		    	
		    	System.out.println("Handling ===="+dirPC);
		    	 handleFragments(dirPC, childPackageRoot, processesFolderPath); 	
		    }
		   
		    processC.appendChild(childPackageRoot);
		    
		    
		    racine.appendChild(processC);
		    
		    //END handling process Elements

		    final Element process = document.createElement("process");
		    process.setAttribute("xsi:type", "org.eclipse.epf.uma:DeliveryProcess" );
		    
		    process.setAttribute("xmi:id",superActiviteDeliveryProcess  );
		    process.setAttribute("name", "Variant" );
		    process.setAttribute("guid", "_1ZbXUS2NEeadNdxfTHAycw" );
		    
		    process.setAttribute("presentationName", "Mininal Agile Process" );
		    process.setAttribute("breakdownElements", breakDowns );
		    
		    final Element presentation = document.createElement("presentation");
		    
		    presentation.setAttribute("xmi:id", "_1ZbXUS2NEeadNdxfTHAycw" );
		    presentation.setAttribute("href", "uma://-tZpZIt_kZXeilm7d80rx7g#-tZpZIt_kZXeilm7d80rx7g" );
		    process.appendChild(presentation);
		    
		    final Element defaultContext = document.createElement("defaultContext");
		    defaultContext.setAttribute("href", "uma://_hmtlAG96EdupM6itjmYdSQ#_59JiMB3LEeakwMHbkQwdXQ" );
		    process.appendChild(defaultContext);
		    
		    final Element valideContext = document.createElement("valideContext");
		    valideContext.setAttribute("href", "uma://_hmtlAG96EdupM6itjmYdSQ#_59JiMB3LEeakwMHbkQwdXQ" );
		    process.appendChild(valideContext);	
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
		    final StreamResult sortie = new StreamResult(new File(variantFolderPath+"/variant.xml"));
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
	    
	    
	    private void handleFragments(String pcName, Element owner, String processesFolderPath) throws ParserConfigurationException, SAXException, IOException {
			// TODO Auto-generated method stub
	    	

			//parse and read ProcessFragments 	 


			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Load the input XML document, parse it and return an instance of the
			// Document class.
			Document documentVariants = builder.parse(new File(processesFolderPath+"/"+pcName+"/model.xmi"));

			List<String> deltas = new ArrayList<String>();
			NodeList nodeList = documentVariants.getDocumentElement().getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				// System.out.println(node.getNodeName());


				if (node.getNodeName().contains("ProcessComponent")) {
					Element elem = (Element) node;
					
					
					 NodeList methodNodes = elem.getChildNodes();
	                 

				      

					for (int j = 0; j < methodNodes.getLength(); j++) {

						{
							Node n = methodNodes.item(j);
							
							// System.out.println("node "+method );

							if (n.getNodeName().contains("childPackages")) {
								
								

							     Element e = (Element) n;
							     
							    
							     
							      Element childPackge = document.createElement("childPackages");
							     childPackge.setAttribute("xsi:type", "org.eclipse.epf.uma:ProcessPackage" );
							     childPackge.setAttribute("xmi:id", e.getAttribute("xmi:id") );
							     childPackge.setAttribute("name", e.getAttribute("name") );
							     childPackge.setAttribute("guid", e.getAttribute("guid") );
								 
								 final NodeList liste = e.getChildNodes();
								 
								 for (int k = 0; k < liste.getLength(); k++) {
									 Node cp = liste.item(k);
									 
									 if (cp.getNodeName().contains("processElements")){
										 
										 Element pr = (Element) cp;
										 
										// System.out.println("Create Process Elements");
										 final Element process = document.createElement("processElements");
										 process.setAttribute("xsi:type", pr.getAttribute("xsi:type") );
										
										 if (pr.getAttribute("xsi:type").contains("Phase)") ){
											 
											 process.setAttribute("superActivities", superActiviteDeliveryProcess);
											 breakDowns=breakDowns+" "+ e.getAttribute("xmi:id")+" ";
										 
											 superActivitePhase= e.getAttribute("xmi:id") ;
										 }
										 
										 if (pr.getAttribute("xsi:type").contains("Activity)") ){
											 
											 if (superActivitePhase=="") {
												 process.setAttribute("superActivities", superActiviteDeliveryProcess);
											 breakDowns=breakDowns+" "+ e.getAttribute("xmi:id")+" ";
													 
											 }
											 else 
												 process.setAttribute("superActivities", superActivitePhase);
										 }
											 
											
										 
										 
											 
										 process.setAttribute("xmi:id", pr.getAttribute("xmi:id") );
										 process.setAttribute("name", pr.getAttribute("name") );
										 process.setAttribute("guid", pr.getAttribute("guid") );
										 process.setAttribute("presentarionName", pr.getAttribute("presentarionName") );
										 process.setAttribute("isPlanned", pr.getAttribute("isPlanned") );
										 
										 
										
										 								 
										 NodeList elements = pr.getChildNodes();
										 
										 for (int f = 0; f < elements.getLength(); f++) {
											 
											 Node p = elements.item(f);
											
										
											 
											 if( p.getNodeName().contains(("Role"))){
												 
												 //System.out.println("Create Role");
												 Element fils = (Element) p;
												 
												 final Element role = document.createElement("Role");
												 role.setAttribute("href", fils.getAttribute("href") );
												 process.appendChild(role);
												 
											 }
											 if( p.getNodeName().contains(("WorkProduct"))){
												 //System.out.println("Create WorProduct");
												 Element fils = (Element) p;
												 
												 final Element wp = document.createElement("WorkProduct");
												 wp.setAttribute("href", fils.getAttribute("href") );
												 process.appendChild(wp);
												 
											 }	
											 
											 if( p.getNodeName().contains(("Task"))){
												 //System.out.println("Create WorProduct");
												 Element fils = (Element) p;
												 
												 final Element wp = document.createElement("Task");
												 wp.setAttribute("href", fils.getAttribute("href") );
												 process.appendChild(wp);
												 
											 }	
											 
											 if( p.getNodeName().contains(("selectedSteps"))){
												 //System.out.println("Create WorProduct");
												 Element fils = (Element) p;
												 
												 final Element wp = document.createElement("WorkProduct");
												 wp.setAttribute("href", fils.getAttribute("href") );
												 process.appendChild(wp);
												 
											 }	
											 
											 if( p.getNodeName().contains(("methodElementProperty"))){
												 //System.out.println("Create WorProduct");
												 Element fils = (Element) p;
												 
												 final Element wp = document.createElement("methodElementProperty");
												 wp.setAttribute("xmi:id", fils.getAttribute("xmi:id") );
												 wp.setAttribute("name", fils.getAttribute("name") );
												 wp.setAttribute("value", fils.getAttribute("value") );
												 process.appendChild(wp);
												 
											 }		 
											 
											 childPackge.appendChild(process);		 
											
											 
											 
											 
											 
										 }
										 
										
										 
									 }
								 }

								 
								
							owner.appendChild(childPackge);	
							}
						
						}

					}
				}
			}
		
			
		}

		public void addProcessFragments(String delta){
	    	
	    	// Create childPackges
	    	//add attributes
	    	// create process Elemen
	    }
	}


