# Gitlet Design Document

**Name**: Anh-Tu Lu

## Overview
Creating a data structure to hold all of the information 
regarding commits using HashMap of TreeMaps. 

## Classes and Data Structures
### Classes
1. Gitlet: Main class.   
   Instance Variables
   1. TreeCommit tree: tree of all commits.
   2. HashMap<String, Commit> mapBranches
   3. HashSet<String, List<Commit> mapCommit
   4. int idTracker: ID number for specific commit. 
   5. HashMap< File, boolean> mapRemoved
   
2. TreeCommit: Private class that is nested inside Gitlet to move around branches 
and make new branch.
3. Commit: Private class that is nested inside Gitlet.
4. Branch: Private class that is nested inside Gitlet.

### Necessary Data Structures

1. Field: HashMap < String, Commit > mapBranches:
A HashMap that stores all of the open branches and their branch name.

2. Field: HashMap < String, List< Commit>> mapCommit:
A HashMap to commit all commit messages.

3. Field: String commitCurr:
A String to save current active commit and which commit is sync with directory.

4. Structure: LinkedList:
LinkedList for commit objects.

5. Field: HashMap< File, boolean> mapAdded:
Map that tracks added files. This is emptied on commit.

6. Field: HashMap< File, boolean> mapRemoved:
Map that tracks the removal files. This is emptied on commit.


## Algorithms



## Persistence
We want to identifying which pieces of data are needed to satisfy one or 
more calls to Gitlet. This can be done by 
