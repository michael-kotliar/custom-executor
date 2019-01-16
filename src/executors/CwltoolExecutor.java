package executors;


import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.*;


public class CwltoolExecutor {

    public static void main(String[] commandLineArguments) {

        final CommandLineParser commandLineParser = new DefaultParser();
        final Options posixOptions = createOptions();

        CommandLine commandLine;
        List<String> commandLineArray = Arrays.asList(commandLineArguments);
        String[] inputArguments = null;

        if (commandLineArray.contains("--")) {
            commandLineArguments = commandLineArray.subList(0, commandLineArray.indexOf("--")).toArray(new String[0]);
            inputArguments = commandLineArray.subList(commandLineArray.indexOf("--") + 1, commandLineArray.size()).toArray(new String[0]);
        }

        try {
            commandLine = commandLineParser.parse(posixOptions, commandLineArguments);

            if (commandLine.hasOption("h")) {
                System.out.println("Print usage here and exit");
                System.exit(0);
            }
            if (commandLine.hasOption("version")) {
                System.out.println("Print version here and exit");
                System.exit(0);
            }
            System.out.println(commandLine.getArgList());

            String app = commandLine.getArgList().get(0);
            String job = commandLine.getArgList().get(1);

            execute(new String[] {"cwltool", app, job});

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private static Options createOptions() {
        Options options = new Options();
        options.addOption("v", "verbose", false, "print more information on the standard output");
        options.addOption("b", "basedir", true, "execution directory");
        options.addOption("c", "configuration-dir", true, "configuration directory");
        options.addOption("r", "resolve-app", false, "resolve all referenced fragments and print application as a single JSON document");
        options.addOption(null, "cache-dir", true, "basic tool result caching (experimental)");
        options.addOption(null, "no-container", false, "don't use containers");
        options.addOption(null, "tmp-outdir-prefix", true, "legacy compatibility parameter, doesn't do anything");
        options.addOption(null, "tmpdir-prefix", true, "legacy compatibility parameter, doesn't do anything");
        options.addOption(null, "outdir", true, "legacy compatibility parameter, doesn't do anything");
        options.addOption(null, "quiet", false, "don't print anything except final result on standard output");
        options.addOption(null, "tes-url", true, "url of the ga4gh task execution server instance (experimental)");
        options.addOption(null, "tes-storage", true, "path to the storage used by the ga4gh tes server (currently supports locall dirs and google storage cloud paths)");
        options.addOption(null, "enable-composer-logs", false, "enable additional logging required by Composer");
        options.addOption(null, "version", false, "print program version and exit");
        options.addOption("h", "help", false, "print this help message and exit");
        return options;
    }

    private static void execute(String[] command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
