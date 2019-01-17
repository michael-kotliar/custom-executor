package executors;


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

            if (commandLine.hasOption("enable-composer-logs")) {
                System.out.println("Should enable composer logs");
            }

            String app = commandLine.getArgList().get(0);
            String job = commandLine.getArgList().get(1);
            String outdir = commandLine.getOptionValue("outdir");

            execute(new String[] {"cwltool", "--outdir", outdir, app, job});

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
