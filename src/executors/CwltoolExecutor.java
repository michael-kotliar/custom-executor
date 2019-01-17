package executors;


import java.io.File;
import org.apache.commons.cli.*;


public class CwltoolExecutor {

    public static void main(String[] commandLineArguments) {
        CommandLineParser commandLineParser = new DefaultParser();
        Options posixOptions = createOptions();
        try {
            CommandLine commandLine = commandLineParser.parse(posixOptions, commandLineArguments);
            if (commandLine.hasOption("h")) {
                System.out.println("Print usage here and exit");
                System.exit(0);
            }
            if (commandLine.hasOption("version")) {
                System.out.println("Print version here and exit");
                System.exit(0);
            }
            String app = commandLine.getArgList().get(0);
            String job = commandLine.getArgList().get(1);
            File job_file = new File(job);
            execute(new String[] {"cwltool", "--outdir", job_file.getParent(), app, job});

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
