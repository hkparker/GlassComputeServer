GlassComputeServer
==================
You'll need the following libs to compile:
* commons-codec-1.6.jar
* commons-logging-1.1.3.jar
* gson-2.2.4.jar 
* httpclient-4.3.1.jar
* httpcore-4.3.jar
* WolframAlpha-1.1.jar (can be found on WolframAlpha developer's site)

Server will run on port 8080. If you want to use GlassCompute with your own server, you'll have to update the URL in the GlassCompute ComputeActivity.java

You'll also need to update the WolframAlphaResults.java appid variable with your own WolframAlpha Developer key.

**To help enhance parsing**, take a look at the ResponseServer.java's public String parseQuery(String query) {} method. Basic string parsing must be conducted here and the result will be sent to WAs servers.

**Original project page:** http://aaka.sh/patel/projects/wolframalpha-for-google-glass/
