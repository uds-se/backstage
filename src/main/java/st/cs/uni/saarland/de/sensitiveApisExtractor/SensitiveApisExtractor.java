package st.cs.uni.saarland.de.sensitiveApisExtractor;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import soot.PackManager;
import soot.Transform;
import st.cs.uni.saarland.de.testApps.TestApp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avdiienko on 27/01/16.
 */
public class SensitiveApisExtractor {
    public static void main(String[] args) throws IOException {
        SensitiveApisExtractorSettings settings = new SensitiveApisExtractorSettings();
        JCommander jc = new JCommander(settings);

        try {
            jc.parse(args);
        }
        catch (ParameterException e){
            System.err.println(e.getMessage());
            jc.usage();
            System.exit(1);
        }
        TestApp.initializeSootForUiAnalysis(settings.apkPath, settings.androidJar, false, false);

        List<String> sensitiveApis = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("res/sensitiveapis.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splittedLine = line.split(">");
                if(splittedLine.length == 2){
                    sensitiveApis.add(String.format("%s>", splittedLine[0]));
                }
            }
        }
        List<String> results = new ArrayList<>();
        SensitiveApisBodyTransformer transformer = new SensitiveApisBodyTransformer(sensitiveApis, results);
        PackManager.v().getPack("jtp").add(new Transform("jtp.SensitiveApiExtractor", transformer));
        PackManager.v().runPacks();

        File apkPathFile = new File(settings.apkPath);
        String apkName = apkPathFile.getName();
        try {
            PrintWriter out = new PrintWriter(String.format("%s/%s_apis.txt", settings.outputFolder, apkName));
            results.forEach(x-> out.println(x));
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
