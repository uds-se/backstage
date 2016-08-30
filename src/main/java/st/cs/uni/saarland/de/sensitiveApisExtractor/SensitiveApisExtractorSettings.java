package st.cs.uni.saarland.de.sensitiveApisExtractor;

import com.beust.jcommander.Parameter;

/**
 * Created by avdiienko on 09/01/16.
 */
public class SensitiveApisExtractorSettings {
    private static final String APKPATH="-apkPath";
    private static final String ANDROIDJAR="-androidJar";
    private static final String OUTPUT_FOLDER="-outputFolder";

    @Parameter(names = APKPATH, description = "Path to an apk file", required = true)
    public String apkPath;

    @Parameter(names = ANDROIDJAR, description = "Path to an androidJar file", required = true)
    public String androidJar;

    @Parameter(names = OUTPUT_FOLDER, description = "Path to the folder for writing files", required = true)
    public String outputFolder;

}
