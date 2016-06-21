package fr.lip6.move.meduse.core;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

import de.ovgu.featureide.core.builder.ComposerExtensionClass;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.io.manager.ConfigurationManager;
import de.ovgu.featureide.fm.core.io.manager.FileHandler;

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
		List<String> orderedFeatures = orderSelectedFeatures(selectedFeatures);

		// Create imagesMap, the key is the relative path from the feature
		// folder to the image file
		Map<String, List<File>> imagesMap = new LinkedHashMap<String, List<File>>();
		for (int i = 0; i < orderedFeatures.size(); i++) {
			IFolder f = featureProject.getSourceFolder().getFolder(orderedFeatures.get(i));
			File folder = f.getRawLocation().makeAbsolute().toFile();
			List<File> files = MeduseComposerUtils.getAllFiles(folder);
			for (File file : files) {
				if (MeduseComposerUtils.getImageFormat(file.getName()) != null) {
					String relative = folder.toURI().relativize(file.toURI()).getPath();
					List<File> currentList = imagesMap.get(relative);
					if (currentList == null) {
						currentList = new ArrayList<File>();
					}
					currentList.add(file);
					imagesMap.put(relative, currentList);
				}
			}
		}

		// For each image, combine the related image files
		for (Entry<String, List<File>> entry : imagesMap.entrySet()) {
			File output = featureProject.getBuildFolder().getRawLocation().makeAbsolute().toFile();
			File outputImageFile = new File(output, entry.getKey());
			try {
				MeduseComposerUtils.overlapImages(entry.getValue(), outputImageFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
