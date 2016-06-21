package fr.lip6.move.meduse.utils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MeduseComposerUtils {


	public static Hashtable<String,List<String>> deltasFragments = new Hashtable<String, List<String>>();

    public static Hashtable<String, String> processComponentNames = new Hashtable<String, String>();
	

    public static Hashtable<String, String> processComponentDirectories = new Hashtable<String, String>();

	public static List<String> readProcessFragmentDirectories(String processesFolderPath){
		
		List<String> result = new ArrayList<String>(); 
		File file = new File(processesFolderPath);
        File[] files = file.listFiles();
      
        	 if (files != null) {
                 for (int i = 0; i < files.length; i++) {
                     if (files[i].isDirectory() == true) {
                    	 
                    	 System.out.println("Find process :"+files[i].getName());
                    	 
                    	 result.add(files[i].getName());
                     }
                }
        	 }
        	 return result;
	}
	
	
	public static void initialiseProcessFragments(String processesFolderPath ) throws ParserConfigurationException, SAXException, IOException{
		
		List<String> dirs = readProcessFragmentDirectories(processesFolderPath);
		
		
		
		for (String d: dirs){
			
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Load the input XML document, parse it and return an instance of the
			// Document class.
			Document document = builder.parse(new File(processesFolderPath+"/"+d+"/model.xmi"));

			List<String> deltas = new ArrayList<String>();
			NodeList nodeList = document.getDocumentElement().getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				
				if (node.getNodeName().contains("ProcessComponent")) {
					
					Element elem = (Element) node;
					String key = elem.getAttribute("xmi:id");
					
					String name = elem.getAttribute("name");
					
					processComponentNames.put(key, name);
					processComponentDirectories.put(key, d);
			
		       }
			}
			
		}
		
		for (String key:processComponentNames.keySet()){
			
			
			
			//System.out.println(key + " ==> "+processComponentNames.get(key));
		}
		
	}
	
	

	public static Hashtable<String,List<String>> parseDelaProcesses(String deltasFolderPath) throws ParserConfigurationException,
	SAXException, IOException {

		Hashtable<String,List<String>> deltasFragments = new Hashtable<String, List<String>>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Load the input XML document, parse it and return an instance of the
		// Document class.
		Document document = builder.parse(new File(deltasFolderPath+"/plugin.xmi"));

		List<String> deltas = new ArrayList<String>();
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			// System.out.println(node.getNodeName());


			if (node.getNodeName().contains("MethodPlugin")) {
				Element elem = (Element) node;
				final NodeList methodNodes = elem.getChildNodes();


				// System.out.println(elem);

				for (int j = 0; j < methodNodes.getLength(); j++) {

					{
						Node method = methodNodes.item(j);



						if (method.getNodeName().contains("methodPackages")) {

							// System.out.println(method.getNodeName());

							Element meth = (Element) method;

							final NodeList childes = meth.getChildNodes();

							for (int k = 0; k < childes.getLength(); k++) {
								Node child = childes.item(k);
								//System.out.println(child.getNodeName());

								if (child.getNodeName().contains("childPackages")) {

									Element chElement = (Element) child;

									NodeList childs2List= chElement.getChildNodes();

									for (int h = 0; h < childs2List.getLength(); h++) {
										Node child2 = childs2List.item(h);
										// System.out.println(child2.getNodeName());
										if (child2.getNodeName().contains("childPackages")) {


											Element content = (Element) child2;

											NodeList contents= content.getChildNodes();

											for (int f = 0; f < contents.getLength(); f++) {

												Node cont = contents.item(f);

												if (cont.getNodeName().contains("contentElements")) {


													String name = cont.getAttributes().getNamedItem("presentationName").getNodeValue();


													if (name.contains("PD")){

														Element c = (Element) cont;
														NodeList atts= c.getChildNodes();
														for (int e = 0; e < atts.getLength(); e++) {

															Node a = atts.item(e);

															if (a.getNodeName().contains("categorizedElements")) {

																String refs = a.getAttributes().getNamedItem("href").getNodeValue();
																//System.out.println(refs.substring(6, 29));
																//System.out.println(refs.substring(30));
																List<String> cle = new ArrayList<String>();
																cle.add(refs.substring(6, 29));
																cle.add(refs.substring(30));
																deltasFragments.put(name, cle);

															}

														}

													}

												}
											}

										}
									}

								}
							}


						}
					}

				}
			}
		}



		for (String delta : deltasFragments.keySet())
			System.out.println(delta + " " +deltasFragments.get(delta));
		return deltasFragments;
	}


	public static void parseProcessFragments(String pComponent) throws ParserConfigurationException,
	SAXException, IOException {


		//parse and read ProcessFragments 	 


		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Load the input XML document, parse it and return an instance of the
		// Document class.
		Document document = builder.parse(new File("ProcessFragments/"+pComponent+"/model.xmi"));

		List<String> deltas = new ArrayList<String>();
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			
			Node node = nodeList.item(i);
			


			if (node.getNodeName().contains("ProcessComponent")) {
				Element elem = (Element) node;
				final NodeList methodNodes = elem.getChildNodes();
                 

				for (int j = 0; j < methodNodes.getLength(); j++) {

					{
						Node method = methodNodes.item(j);

						// System.out.println("node "+method );

						if (method.getNodeName().contains("childPackages")) {

						 

							Element e = (Element) method;
							final NodeList liste = e.getChildNodes();
							  
							
							
						}
					}

				}
			}
		}
	}


}
