# TPTeam: a collaborative testing using the Eclipse OSGi framework and XMPP chat protocol, embedded in Tomcat 
The aim of this project is to create an exemplary collaborative testing tool called TPTeam by using the Eclipse Equinox and Communication Framework (ECF) projects. This goal is achieved by the development of three components:

* TPBuddy™, a client GUI utilizing the Eclipse Rich Client Platform (RCP)
* TPBridge™, an event-driven plug-in that bridges communication across Java Virtual Machines (JVMs) using the freely available infrastructure of Google Talk.
* TPManager™, a server-side test management plug-in.
All three components utilize a novel service-oriented architecture derived from Equinox and distributed events to achieve collaborative unit testing. Out-of-JVM communication is done via instant messaging through Google Talk servers and the open Extensible Messaging and Presence Protocol (XMPP). Tests are executed in the form of Eclipse Test and Performance Tools Platform (TPTP) JUnit testsuites, which allow for server-side management. The work represents a practical first step in utilizing newly available Eclipse open source projects and public infrastructure for the creation of collaborative tools.

# News
## April 5, 2015
Ported the project source code from the now defunct Google code to GitHub.  Google did not export over the project artifacts and automatically deleted the entire project immediatedly after export.
Thank Google! <Shakes Fist>

## September 22, 2007
Added the TPTeam Harvard Master's degree thesis to the downloads section. This thesis would be a good read for anyone interested in developing an Eclipse RCP app, connecting it to Hibenate, or embedding an Eclipse instance within a servlet container.

The thesis describes:

The state-of-the-art
Problem statement
General use cases
Domain model and design patterns used
User Manual
Lessons Learned

## June 10, 2007
The first release, v0.1, is ready for download! Go to the downloads tab for more details. Each archive in the downloads section has a README that explains how to install, configure, and run.
