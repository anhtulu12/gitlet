package gitlet;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/** Commit class for gitlet.
 * @author Anh-Tu Lu
 */
public class Commit implements Serializable {

    /** Creates commit object.
     * @param comMsg commit message
     * @param comTime commit time
     * @param parCo parent of commit
     * @param mapBlo map of blobs
     */
    public Commit(String comMsg, Date comTime, String parCo,
                  HashMap<String, String> mapBlo) {
        this.mapOfBlobs = new HashMap<String, String>();
        this.commitMsg = comMsg;
        this.commitTime = comTime;
        this.commitPar = parCo;
        for (String keyset : mapBlo.keySet()) {
            this.mapOfBlobs.put(keyset, mapBlo.get(keyset));
        }
    }

    /** @return commit message. */
    public String msgReturn() {
        return commitMsg;
    }

    /** @return parent of commit. */
    public String parReturn() {
        return commitPar;
    }

    /** @return sha of file.
     * @param name file name
     */
    public String shaReturnFile(String name) {
        return mapOfBlobs.get(name);
    }

    /** @return parent one. */
    public String mergedParRet1() {
        return parMergeOne;
    }

    /** @return parent two. */
    public String mergeParRet2() {
        return parMergeTwo;
    }

    /** @return map of blobs. */
    public HashMap<String, String> blobsGet() {
        return mapOfBlobs;
    }

    /** @return commit time. */
    public Date getTime() {
        return commitTime;
    }

    /** Commit message string. */
    private String commitMsg;
    /** Date for commit time. */
    private Date commitTime;
    /** Commit parent. */
    private String commitPar;
    /** Map of blobs. */
    private HashMap<String, String> mapOfBlobs;
    /** First parent of merge object. */
    private String parMergeOne = null;
    /** Second parent of merge object. */
    private String parMergeTwo = null;
}
