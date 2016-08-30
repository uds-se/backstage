package st.cs.uni.saarland.de.stat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * Created by avdiienko on 08/08/16.
 */
public class StatBuilder {

    private static Set<String> nonEnglishApps = new HashSet<>();
    private static Set<String> lackOfLayoutsApps = new HashSet<>();
    private static Set<String> brokenApps = new HashSet<>();
    private static Set<String> timeoutedApps = new HashSet<>();
    private static Set<String> normalRun = new HashSet<>();

    public static void main(String[] args){
        String folder = args[0];
        File processedDir = new File(folder);


        if(folder != null && processedDir.exists()){
            for(File logFile : processedDir.listFiles()){
                System.out.println("Processing "+ logFile.getName());
                processLogFile(logFile);
            }
        }

        System.out.println(String.format("Non-english apps: %s", nonEnglishApps.size()));
        System.out.println(String.format("Lack of layout apps: %s", lackOfLayoutsApps.size()));
        System.out.println(String.format("brokenApps apps: %s", brokenApps.size()));
        System.out.println(String.format("Normal apps apps: %s", normalRun.size()));
        System.out.println(String.format("timeoutedApps apps: %s", timeoutedApps.size()));

        //implement saving of names of apks to a file
        writeToFile("brokenApps.txt", brokenApps);
        writeToFile("timeoutedApps.txt", timeoutedApps);

    }

    private static void writeToFile(String name, Set<String> data){
        try {
            FileWriter fw = new FileWriter(name);
            data.forEach(x->{
                try {
                    fw.write(x+"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processLogFile(File logFile) {
        String apkName = logFile.getName().replace("_log.txt", "");

        try(Stream<String> stream = Files.lines(Paths.get(logFile.getPath()))){
            final AtomicBoolean conditionMet = new AtomicBoolean(false);
            final AtomicBoolean savingResultsOccured = new AtomicBoolean(false);
            final AtomicBoolean appFinished = new AtomicBoolean(false);
            final AtomicBoolean phantomClassesOnly = new AtomicBoolean(false);
            final AtomicBoolean noCallbacks = new AtomicBoolean(false);
            final AtomicBoolean titleCrash = new AtomicBoolean(false);
            stream.forEach(line->{
                if(line.contains("Non english language detected.")){
                    nonEnglishApps.add(apkName);
                    conditionMet.set(true);
                    return;
                }
                if(line.contains("App has less than 70% of XML Layouts.")){
                    lackOfLayoutsApps.add(apkName);
                    conditionMet.set(true);
                    return;
                }
                if(line.startsWith("Found") && line.endsWith("partitions")){
                    savingResultsOccured.set(true);
                    return;
                }
                if(line.contains("App Analysis has run for")){
                    appFinished.set(true);
                    conditionMet.set(true);
                    return;
                }
                if(line.contains("Only phantom classes loaded, skipping analysis...")){
                    phantomClassesOnly.set(true);
                    conditionMet.set(true);
                    return;
                }
                if(line.endsWith("callback methods in overall") && line.startsWith("Found")){
                    noCallbacks.set(true);
                    conditionMet.set(true);
                    return;
                }
                if(line.contains("at st.cs.uni.saarland.de.uiAnalysis.ActivityTitleFinder.run")){
                    titleCrash.set(true);
                    conditionMet.set(true);
                    return;
                }
            });
            if(!conditionMet.get() || titleCrash.get()){
                timeoutedApps.add(apkName);
            }
            else if(noCallbacks.get() || phantomClassesOnly.get() || (appFinished.get() && savingResultsOccured.get())) {
                normalRun.add(apkName);
            }
            else if(appFinished.get() && !savingResultsOccured.get()){
                brokenApps.add(apkName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
