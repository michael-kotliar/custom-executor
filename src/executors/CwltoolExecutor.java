package executors;


import org.apache.commons.cli.*;
import java.util.Scanner;
import java.io.InputStream;
import java.io.PrintStream;


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
            String outdir = commandLine.getOptionValue("outdir");

            execute(new String[] {"cwltool", "--outdir", outdir, app, job}, commandLine.hasOption("enable-composer-logs"));

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private static Options createOptions() {
        Options options = new Options();
        options.addOption("h",  "help", false, "Print help message and exit");
        options.addOption(null, "version", false, "Print program version and exit");
        options.addOption(null, "enable-composer-logs", false, "Enable logging required by Composer");
        options.addOption(null, "outdir", true, "Set output directory to save results");
        return options;
    }

    private static void execute(String[] command, boolean enableComposerLogs) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();
            inheritIO(p.getInputStream(), System.out, enableComposerLogs);
            inheritIO(p.getErrorStream(), System.err, enableComposerLogs);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void inheritIO(final InputStream src, final PrintStream dest, boolean enableComposerLogs) {
        new Thread(new Runnable() {
            public void run() {
                Scanner sc = new Scanner(src);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    dest.println(line);
                    if (enableComposerLogs) {
                        printComposerLog(line);
                    }
                }
            }
        }).start();
    }

    private static void printComposerLog(String line){
        if (line.contains("[step")) {
            String stepID = line.substring(6, line.indexOf("]"));
            String stepStatus = line.substring(line.indexOf("]")+2);
            if (stepStatus.contains("start")) {
                stepStatus = "READY";
            }
            else if (stepStatus.contains("success")) {
                stepStatus = "COMPLETED";
            }
            else {
                stepStatus = "FAILED";
            };
            String composerStatus = "Composer: {\"status\": \"" + stepStatus + "\", \"stepId\": \"root." + stepID + "\"}";
            System.err.println(composerStatus);
        }
    }

}
