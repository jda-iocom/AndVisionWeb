# AndVisionWeb

Sample showing how to embed a Visionable WebRTC WebViewClient in an Android app, 
and connect the Javascript on the page to the native client. The file **visionable_webrtc.html** is the sample code as found
on the api/docs Visionable web site, with the addition of JavaScript code that gets info from the native client.


In order for the embedded Javascript to access methods in the Android Java, you need to 
add the @Javascript tag to the native Java code. For example:


    @JavascriptInterface
    public String GetName() { 
        return "Test User";
    }

In the Javascript, you can get at the @JavascriptInterface methods by referencing the object as 
named with the addJavascriptInteface method:

        wv.addJavascriptInterface(this,"plugin");

So, to call the above method in your Javascript, on the web page, just use the method on the '''plugin''' object:

    username = plugin.GetName()
    

The JavaScript in the included HTML file merely pre-populates the edit boxes with values fetched from ```MainActivity.java``` but you 
can, of course do whatever you want with the **values**. You must first, however, define the Hostname in the ```MainActivity.java``` file
before running this. Set this to the site where you have API access. Talk to the Visionable sales team for full API access. 

See [Visionable.com](https://www.visionable.com) for more information on our WebRTC JavaScript application.