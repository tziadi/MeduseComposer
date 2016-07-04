package fr.lip6.move.meduse.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.xml.sax.SAXException;

import de.ovgu.featureide.core.builder.ComposerExtensionClass;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.io.manager.ConfigurationManager;
import de.ovgu.featureide.fm.core.io.manager.FileHandler;
import fr.lip6.move.meduse.utils.MeduseComposerUtils;
import fr.lip6.move.meduse.utils.MeduseProcessVariantGenerator;

/**
 * Meduse composer
 * 
 * 
 * @author Tewfik Ziadi
 *
 */
public class MeduseComposer extends ComposerExtensionClass {

	@Override
	public void performFullBuild(IFile config) {

		// Get the selected features and order them
		List<String> selectedFeatures = getSelectedNonAbstractFeatures(config);
		List<String> selecteddelta = getSelectedDeltas(selectedFeatures);

		Generator generator = new Generator();
		
		IFolder deltasF = featureProject.getProject().getFolder("DeltaProcesses");
		File deltasFolder = deltasF.getRawLocation().makeAbsolute().toFile();
		String deltasFolderPath = deltasFolder.getAbsolutePath();
		
		IFolder processesF = featureProject.getProject().getFolder("ProcessFragments");
		File processesFolder = processesF.getRawLocation().makeAbsolute().toFile();
		String processesFolderPath = processesFolder.getAbsolutePath();
		
		IFolder variantF = featureProject.getProject().getFolder("DerivedProduct");
		File variantFolder = variantF.getRawLocation().makeAbsolute().toFile();
		String variantFolderPath = variantFolder.getAbsolutePath();
		
		IFolder pluginF = featureProject.getProject().getFolder("DerivedProduct");
		File pluginFolder = pluginF.getRawLocation().makeAbsolute().toFile();
		String pluginFolderPath = deltasFolder.getAbsolutePath();
		
		if (deltasFolderPath!=null && processesFolderPath !=null){
			
			try {
				generator.generate(selecteddelta, deltasFolderPath,processesFolderPath, variantFolderPath, pluginFolderPath  );
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		
		MeduseComposerUtils.parseDeltaProcesses("Test");
		MeduseComposerUtils.initialiseProcessFragments("Test");
		List<String> deltas = new ArrayList<>();
		//deltas.add("PD RequirementPhase");
		deltas.add("PD_WriteCodePhase");
		deltas.add("PD_WriteCodeUnitTest");
		deltas.add("PD_IntegrationTestPhase");
		//deltas.add("PD_DefineReleasePhase");
		
		Generator generator = new Generator();
		
		
		
		}

		
	/**
	 * Get selected non abstract features
	 * 
	 * @param config
	 * @return features
	 */
	protected List<String> getSelectedNonAbstractFeatures(IFile config) {
		List<String> selectedFeatures = new ArrayList<String>();
		final Configuration configuration = new Configuration(featureProject.getFeatureModel());
		FileHandler.load(Paths.get(config.getLocationURI()), configuration, ConfigurationManager.getFormat(config.getName()));
		for (IFeature f : configuration.getSelectedFeatures()) {
			if (!f.getStructure().isAbstract()) {
				selectedFeatures.add(f.getName());
			}
		}
		return selectedFeatures;
	}

	/**
	 * Order the features in case of user-defined ordering
	 * 
	 * @param selectedFeatures
	 * @return ordered features
	 */
	protected List<String> orderSelectedFeatures(List<String> selectedFeatures) {
		// Order them if needed
		Collection<String> featureOrderList = featureProject.getFeatureModel().getFeatureOrderList();
		if (featureOrderList != null && !featureOrderList.isEmpty()) {
			List<String> orderedFeatures = new ArrayList<String>();
			for (String feature : featureOrderList) {
				if (selectedFeatures.contains(feature)) {
					orderedFeatures.add(feature);
				}
			}
			return orderedFeatures;
		}
		return selectedFeatures;
	}

	
	/**
	 * extract the selected delta
	 * 
	 * @param selectedFeatures
	 * @return ordered features
	 */
	protected List<String> getSelectedDeltas(List<String> selectedFeatures) {
		// Order them if needed
		List<String> deltas = new ArrayList<String>();
		for(String feature : selectedFeatures){
			
			if (feature.contains("PD"))
				deltas.add(feature);
		}
		
		return deltas;
		
	}
}
