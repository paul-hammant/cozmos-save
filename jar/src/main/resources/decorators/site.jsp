<#assign page=JspTaglibs["http://www.opensymphony.com/sitemesh/page"]>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>
    ${title}
</title>
${head}
<style type="text/css">
body {
    background: #FFF url( '${base}/style/images/back8.jpg' ) no-repeat top left fixed;
    margin: 0 0 20px 0;
    padding: 0;
    font-family: Verdana, sans-serif;
    font-size: 0.95em;
    color: #000;
    line-height: 1.5em;
}

#header {
    width: 80%;
    background: transparent;
    margin: 0 auto;
    padding: 0;
    height: 101px;

}

#header h1 {
    padding: 30px 0;
    color: #999;
    font-size: 1.4em;
    letter-spacing: 3px;
    margin: 0;
    text-align: right;
}

#nav {
    position: absolute;
    top: 85px;
    left: 10%;
    width: auto;
    padding: 0;
    margin: 0 auto;
    font: bold 11px Verdana, sans-serif;
    height: 25px;

}

#nav li {
    list-style: none;
    margin: 0;
    padding: 0;
    display: inline;
}

#nav li a {
    padding: 3px;
    margin: 0;
    border: 1px solid #B78E96;
    border-bottom: none;
    background: transparent url( '${base}/style/images/trans6.gif' ) repeat;
    text-decoration: none;

}

#nav li a:link {
    color: #000;
}

#nav li a:visited {
    color: #fff;
}

#nav li a:hover {
    color: #fff;
    background: transparent url( '${base}/style/images/trans7.gif ) repeat;
}

#nav li a#current {
    background: transparent url( '${base}/style/images/trans7.gif' ) repeat;
    border-bottom: 1px solid #999;
}

#container {
    background: transparent url( '${base}/style/images/trans5.gif' ) repeat;
    width: 80%;
    margin: 0 auto;
    padding: 0;
}

#content {
    margin: 0;
    padding: 40px;

}

#footer {
    background: transparent;
    padding: 5px 10px 2px 0;
    text-align: right;
    font-size: 0.75em;
    clear: both;
    line-height: 1.15em;
}

a, a:visited {
    background: transparent;
    color: #333;
    border-bottom: 1px solid #333;
    text-decoration: none;
    font-weight: bold;
}

a:hover, a:active {
    background: transparent;
    color: #666;
}

h2 {
    color: #666;
    border-bottom: 1px solid #666;
    margin: 0;
    padding: 10px 0 1px 0;
    font-size: 1.2em;
}
</style>
</head>
<body>
<div id="header">
    <h1>Cozmos</h1>
</div>
<ul id="nav">
    <!-- you may want some Javascript to change "current" depending on URL -->
    <li><a href="${base}/index.html" id="current">Home</a></li>
    <li><a href="${base}/index.html">Tab One</a></li>
    <li><a href="${base}/index.html">Tab Two</a></li>
    <li><a href="${base}/index.html">Tab Three</a></li>
    <li><a href="${base}/index.html">Tab Four</a></li>
</ul>
<div id="container">
    <div id="content">
        ${body}
    </div>
    <div id="footer">
        <p>Copyright&copy; 2006 Your Company <br>
            |&nbsp;Template by <a href="http://www.karenblundell.com" target="_blank">arwen54</a>&nbsp;|&nbsp;<a
                href="http://validator.w3.org/check?uri=referer" target="_blank">Valid XHTML</a>&nbsp;|&nbsp;<a
                href="http://jigsaw.w3.org/css-validator/" target="_blank">Valid CSS</a>&nbsp;|</p>
    </div>
</div>
</body>
</html>
