package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/** Gitlet object for gitlet.
 * @author Anh-Tu Lu
 */
public class Gitlet implements Serializable {

    /** Initializes Gitlet object. */
    public Gitlet() {
        mapOfCommits = new ArrayList<String>();
        mapOfBranches = new HashMap<String, String>();
        curStage = new Stage();
        curBranch = "master";
        Commit first = new Commit("initial commit", new Date(0),
                null, new HashMap<String, String>());
        String stra = Utils.sha1(Utils.serialize(first));
        File f = new File(".gitlet/commits/" + stra);
        Utils.writeObject(f, first);
        curHeadCom = stra;
        curStage.headSetter(first);
        mapOfCommits.add(stra);
        mapOfBranches.put("master", stra);
    }

    /** Add command for gitlet.
     * @param name file name
     */
    public void add(String name) {
        curStage.add(name);
    }

    /** Remove command for gitlet.
     * @param name file name
     */
    public void rm(String name) {
        curStage.rm(name);
    }

    /** Commit command for gitlet.
     * @param mse commit message
     */
    public void commit(String mse) {
        Commit crt = curStage.commit(mse);
        if (crt == null) {
            System.exit(0);
        }
        String stra = Utils.sha1(Utils.serialize(crt));
        mapOfCommits.add(stra);
        File f = new File(".gitlet/commits/" + stra);
        Utils.writeObject(f, crt);
        curHeadCom = stra;
        mapOfBranches.put(curBranch, curHeadCom);
    }

    /** Log command for gitlet. */
    public void log() {
        String loga = curHeadCom;
        while (loga != null) {
            File fa = new File(".gitlet/commits/" + loga);
            Commit cr = Utils.readObject(fa, Commit.class);
            System.out.println("===");
            System.out.println("commit " + loga);
            if (cr.mergedParRet1() != null) {
                System.out.println("Merge: " + cr.mergedParRet1()
                        + " " + cr.mergeParRet2());
            }
            Date cTime = cr.getTime();
            System.out.println(String.format("Date: %1$ta %1$tb"
                    + " %1$te %1$tT %1$tY %1$tz", cTime));
            System.out.println(cr.msgReturn());
            System.out.println();
            loga = cr.parReturn();
        }
    }

    /** Global log command for gitlet. */
    public void logGlobal() {
        for (String stra : mapOfCommits) {
            File frr = new File(".gitlet/commits/" + stra);
            Commit cit = Utils.readObject(frr, Commit.class);
            System.out.println("===");
            System.out.println("commit " + stra);
            if (cit.mergedParRet1() != null) {
                System.out.println("Merge: " + cit.mergedParRet1()
                        + " " + cit.mergeParRet2());
            }
            Date cTime = cit.getTime();
            System.out.println(String.format("Date: %1$ta"
                    + " %1$tb %1$te %1$tT %1$tY %1$tz", cTime));
            System.out.println(cit.msgReturn());
            System.out.println();
        }
    }

    /** Find command for gitlet.
     * @param phrase phrase to find
     */
    public void find(String phrase) {
        boolean isThere = false;
        for (String stra : mapOfCommits) {
            File fr = new File(".gitlet/commits/" + stra);
            Commit cmtt = Utils.readObject(fr, Commit.class);
            if (cmtt.msgReturn().equals(phrase)) {
                System.out.println(stra);
                isThere = true;
            }
        }
        if (!isThere) {
            System.out.println("Found no commit with that message.");
        }
    }

    /** Checkout command for gitlet.
     * @param fName file name
     */
    public void checkout(String fName) {
        File commitF = new File(".gitlet/commits/" + curHeadCom);
        Commit commit = Utils.readObject(commitF, Commit.class);
        String stra = commit.shaReturnFile(fName);
        if (stra == null) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        File blobF = new File(".gitlet/blobs/" + stra);
        byte[] blurb = Utils.readContents(blobF);
        File file = new File(fName);
        Utils.writeContents(file, blurb);
    }

    /** Checkout command for gitlet.
     * @param id id string
     * @param name file name
     */
    public void checkout(String id, String name) {
        boolean isFind = false;
        for (String key: mapOfCommits) {
            if (key.substring(0, id.length()).equals(id)) {
                id = key;
                isFind = true;
                break;
            }
        }
        if (!isFind) {
            System.out.println("No commit with that id exists.");
            return;
        }
        File frrcommit = new File(".gitlet/commits/" + id);
        Commit crmt = Utils.readObject(frrcommit, Commit.class);
        String sha = crmt.shaReturnFile(name);
        if (sha == null) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        File frrblob = new File(".gitlet/blobs/" + sha);
        byte[] blurb = Utils.readContents(frrblob);
        File file = new File(name);
        Utils.writeContents(file, blurb);
    }

    /** Branch checkout command for gitlet.
     * @param nameBra branch name.
     */
    public void branchCheckout(String nameBra) {
        String brrr = mapOfBranches.get(nameBra);
        if (brrr == null) {
            System.out.println("No such branch exists.");
            return;
        }
        if (nameBra.equals(curBranch)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        File fa = new File(".gitlet/commits/" + brrr);
        Commit crt = Utils.readObject(fa, Commit.class);
        HashMap<String, String> branchBlobs = crt.blobsGet();
        File farr = new File(".gitlet/commits/" + curHeadCom);
        Commit hea = Utils.readObject(farr, Commit.class);
        HashMap<String, String> headBlobs = hea.blobsGet();
        List<String> workingDirectoryFiles = Utils.plainFilenamesIn("./");
        if (trackedUntrack(crt)) {
            return;
        }

        HashMap<String, String> blobs = crt.blobsGet();
        for (String key: blobs.keySet()) {
            String blobID = blobs.get(key);
            File fblob = new File(".gitlet/blobs/" + blobID);
            byte[] blob = Utils.readContents(fblob);
            File file = new File(key);
            Utils.writeContents(file, blob);
        }

        for (String fileName: workingDirectoryFiles) {
            if (!blobs.keySet().contains(fileName)) {
                Utils.restrictedDelete(fileName);
            }
        }
        curStage.erase();
        curStage.headSetter(crt);
        curHeadCom = brrr;
        curBranch = nameBra;
    }

    /** Check branch command.
     * @param nameBra branch name
     */
    public void cheBranch(String nameBra) {
        if (mapOfBranches.get(nameBra) != null) {
            System.out.println("branch with that name already exists.");
            return;
        }
        mapOfBranches.put(nameBra, curHeadCom);
    }

    /** Remove branch command.
     * @param nameBra branch name
     */
    public void branchRm(String nameBra) {
        if (nameBra.equals(curBranch)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        if (mapOfBranches.remove(nameBra) == null) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
    }

    /** Track untracked files.
     * @param g commit object
     * @return if file is untracked
     */
    public boolean trackedUntrack(Commit g) {
        File far = new File(".gitlet/commits/" + curHeadCom);
        Commit curCom = Utils.readObject(far, Commit.class);
        HashMap blobHea = curCom.blobsGet();
        HashMap givenBlobs = g.blobsGet();
        List<String> dirFiles = Utils.plainFilenamesIn("./");
        for (String narm: dirFiles) {
            if (!blobHea.containsKey(narm)
                    && !curStage.retFileStaged().containsKey(narm)) {
                System.out.println("There is an untracked file in the way;"
                        + " delete it or add it first.");
                return true;
            }
        }
        return false;
    }

    /** Reset command for gitlet.
     * @param id of commit
     */
    public void resetCo(String id) {
        if (!mapOfCommits.contains(id)) {
            System.out.println("No commit with that ID exists.");
            return;
        } else {
            File fa = new File(".gitlet/commits/" + id);
            Commit crmt = Utils.readObject(fa, Commit.class);
            HashMap<String, String> blobBr = crmt.blobsGet();
            File far = new File(".gitlet/commits/" + curHeadCom);
            Commit hea = Utils.readObject(far, Commit.class);
            HashMap<String, String> headBlobs = hea.blobsGet();
            List<String> dirFiles = Utils.plainFilenamesIn("./");
            if (trackedUntrack(crmt)) {
                return;
            }
            HashMap<String, String> blobs = crmt.blobsGet();
            for (String keyset: blobs.keySet()) {
                String idB = blobs.get(keyset);
                File blobFr = new File(".gitlet/blobs/" + idB);
                byte[] blob = Utils.readContents(blobFr);
                File file = new File(keyset);
                Utils.writeContents(file, blob);
            }
            for (String name: dirFiles) {
                if (blobBr.get(name) == null) {
                    Utils.restrictedDelete(name);
                }
            }
            curStage.erase();
            curStage.headSetter(crmt);
            curHeadCom = id;
            mapOfBranches.put(curBranch, id);
        }
    }

    /** Map of commits. */
    private ArrayList<String> mapOfCommits;
    /** Map of branches. */
    private HashMap<String, String> mapOfBranches;
    /** Current stage of commits. */
    private Stage curStage;
    /** Current commit head. */
    private String curHeadCom;
    /** Current branch. */
    private String curBranch;
}
