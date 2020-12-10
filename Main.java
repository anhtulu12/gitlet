package gitlet;

import java.io.File;

/** Main class for control-version system Gitlet.
 * @author Anh-Tu Lu
 */
public class Main {

    /** Main method for program and executing command(s).
     * @param args for command(s). */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        if (args[0].equals("init")) {
            init();
        } else {
            File git = new File(".gitlet/gitlet");
            if (!git.exists()) {
                System.out.println("Not in an initialized"
                        + " Gitlet directory.");
                System.exit(0);
            }
            Gitlet word = Utils.readObject(git, Gitlet.class);
            switch (args[0]) {
            case "add":
                comAdd(word, args);
                break;
            case "commit":
                comCom(word, args);
                break;
            case "rm":
                comRm(word, args);
                break;
            case "log":
                comLog(word, args);
                break;
            case "global-log":
                comLogGlo(word, args);
                break;
            case "find":
                findCo(word, args);
                break;
            case "checkout":
                comCheckout(word, args);
                break;
            case "branch":
                comBranch(word, args);
                break;
            case "rm-branch":
                comRmBranch(word, args);
                break;
            case "reset":
                comReset(word, args);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
                break;
            }
            File fr = new File(".gitlet/gitlet");
            Utils.writeObject(fr, word);
        }
    }

    /** Add command for gitlet.
     * @param word gitlet object
     * @param args arguments that has command
     */
    static void comAdd(Gitlet word, String... args) {
        if (args.length == 2) {
            word.add(args[1]);
        } else {
            System.out.println(opsMsg);
            System.exit(0);
        }

    }

    /** Initializes directories for gitlet. */
    public static void init() {
        File gFile = new File(".gitlet/");
        if (!gFile.exists()) {
            gFile.mkdirs();
            File cFile = new File(".gitlet/commits/");
            cFile.mkdirs();
            File bFile = new File(".gitlet/blobs/");
            bFile.mkdirs();
            Gitlet word = new Gitlet();
            File aa = new File(".gitlet/gitlet");
            Utils.writeObject(aa, word);
        } else {
            System.out.println("A Gitlet version-control system"
                    + "already exists in the current directory.");
            System.exit(0);
        }
    }

    /** Commit command for gitlet.
     * @param word gitlet object
     * @param args  arguments that has command
     */
    static void comCom(Gitlet word, String...args) {
        if (args.length == 1) {
            System.out.println(commitMsg);
            System.exit(0);
        } else if (args.length > 2) {
            System.out.println(opsMsg);
            System.exit(0);
        }
        word.commit(args[1]);
    }

    /** Remove command for gitlet.
     * @param word gitlet object
     * @param args arguments that has command
     */
    static void comRm(Gitlet word, String...args) {
        if (args.length == 2) {
            word.rm(args[1]);
        } else {
            System.out.println(opsMsg);
            System.exit(0);
        }

    }

    /** Log command for gitlet.
     * @param word gitlet object
     * @param args arguments that has command
     */
    static void comLog(Gitlet word, String...args) {
        if (args.length == 1) {
            word.log();
        } else {
            System.out.println(opsMsg);
            System.exit(0);
        }
    }

    /** Global log command for gitlet.
     * @param word gitlet object
     * @param args arguments that has command
     */
    static void comLogGlo(Gitlet word, String...args) {
        if (args.length == 1) {
            word.logGlobal();
        } else {
            System.out.println(opsMsg);
            System.exit(0);
        }
    }

    /** Find command for gitlet.
     * @param word gitlet object
     * @param args arguments that has command
     */
    static void findCo(Gitlet word, String...args) {
        if (args.length == 2) {
            word.find(args[1]);
        } else {
            System.out.println(opsMsg);
            System.exit(0);
        }
    }

    /** Branch command for gitlet.
     * @param word gitlet object
     * @param args arguments that has command
     */
    static void comBranch(Gitlet word, String... args) {
        if (args.length == 2) {
            word.cheBranch(args[1]);
        } else {
            System.out.println(opsMsg);
            System.exit(0);
        }
    }

    /** Checkout command for gitlet.
     * @param word gitlet object
     * @param args arguments that has command
     */
    static void comCheckout(Gitlet word, String... args) {
        if (args.length == 2) {
            word.branchCheckout(args[1]);
        } else if (args.length == 3) {
            if (!args[1].equals("--")) {
                System.out.println(opsMsg);
                System.exit(0);
            }
            word.checkout(args[2]);
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println(opsMsg);
                System.exit(0);
            }
            word.checkout(args[1], args[3]);
        } else {
            System.out.println(opsMsg);
        }
    }

    /** Remove branch command for gitlet.
     * @param word gitlet object
     * @param args arguments that has command
     */
    static void comRmBranch(Gitlet word, String...args) {
        if (args.length == 2) {
            word.branchRm(args[1]);
        } else {
            System.out.println(opsMsg);
            System.exit(0);
        }
    }

    /** Reset command for gitlet.
     * @param word gitlet obj
     * @param args arguments
     */
    static void comReset(Gitlet word, String...args) {
        if (args.length == 2) {
            word.resetCo(args[1]);
        } else {
            System.out.println(opsMsg);
            System.exit(0);
        }
    }

    /** @return accessor method for opsMsg. */
    private String getOpsMsg() {
        return opsMsg;
    }

    /** @return accessor method for commitMsg. */
    private String getCommitMsg() {
        return commitMsg;
    }

    /** Message for incorrect operands. */
    private static String opsMsg = "Incorrect operands.";
    /** Message for empty commit message. */
    private static String commitMsg = "Please enter a commit message.";
}
