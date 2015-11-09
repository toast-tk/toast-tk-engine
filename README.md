[![Build Status](https://jenkins.synaptix-labs.com/buildStatus/icon?job=Toast-tk)](https://jenkins.synaptix-labs.com/job/Toast-tk/)

Build not working, requires xvbf plugin on jenkins to support X11.

# Toast Tk

Toast Toolkit - Test Automation Toolkit.

# What is Toast TK ?
***

Toast Toolkit aims to ease collaboration between Business Analysts 
and Developpers to describe and test an application behavior. 

<div style="text-align:center" width="100%">
<img src="https://gitlab.synaptix-labs.com/synaptix/toast-tk/uploads/76234d43d51527bfe7264f8c0b9d9843/collaborative_cycle.png" alt="collaborative_cycle" 
width="600px" height="400px" >
</div>

Toast provides:
- [Strongly Typed Tests](https://gitlab.synaptix-labs.com/synaptix/toast-tk/wikis/toast-strongly-typed-tests)
- [Test Refactoring](https://gitlab.synaptix-labs.com/synaptix/toast-tk/wikis/toast-test-refactoring)
- [Collaborative Apporach](https://gitlab.synaptix-labs.com/synaptix/toast-tk/wikis/toast-collaborative-approach)
- [Test Campaign Reporting](https://gitlab.synaptix-labs.com/synaptix/toast-tk/wikis/toast-campaign-reporting)


Toast is a set of tools for recording, replaying test actions for:
- Web Applications.
- Java GUI programs developed using Swing Components. 
- Backend Applications.

Toast consists of an editor (Toast TK WebApp), a recorder and a player (Toast TK Studio). 
Toast records the test cases in a natural language (human readable).
The test cases can be run either through the UI or in batch mode through Eclipse (ref. how to eclipse, eclipse archetype).

# Building Toast TK
***

## Toast TK Engine
You need to clone this repository to compile Toast using maven.

``` java
$ git clone https://gitlab.synaptix-labs.com/synaptix/toast-tk.git
$ cd toast-tk
$ mvn clean install
```

The packages are now installed in your local maven repository.

## Toast TK Studio
${version} = __1.3-rc3__

### JNLP Mode
1. cd toast-tk/toast-tk-studio/target/toast-studio-${version}
2. open app.jnlp and Enjoy !

### IDE Mode (Eclipse)
1. Clone the repository.
2. Install egit/jgit plugins. 
3. Import the projects as maven projects from the cloned repository. 
4. Build All
5. Copy toast-tk/toast-tk-studio/target/toast-studio-${version}/agent-lib to ${user.home}/.toast/plugins
6. Launch __AgentBoot__ as a Java Application and Enjoy !

## Toast TK WebApp
* 1- Clone the webapp repository and compile it using sbt.

```
$ git clone https://gitlab.synaptix-labs.com/sallah-kokaina/toast-tk-play-webapp.git
$ cd toast-tk-play-webapp
$ sbt
$ dist
$ unzip target/universal/toast-tk-webapp-${version}.zip
$ cd toast-tk-webapp-${version}/bin
$ ./toast-tk-webapp
```
* 2- Install and Launch a local Mongo Database
* 3- Open http://localhost:9000 in Google Chrome and Enjoy !

# More Information 
***

[wiki](https://gitlab.synaptix-labs.com/synaptix/toast-tk/wikis/home)