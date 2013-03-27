<%@ page contentType="text/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="mum" tagdir="/WEB-INF/tags/mum" %>
<%@ taglib prefix="status" uri="https://my-tools.doit.wisc.edu/tags/status" %>
<!DOCTYPE html>
<html>
<head>
    <title>MUM Status</title>
    <c:if test="${! empty refresh}">
        <meta http-equiv="refresh" content="${refresh}">
    </c:if>
    
    <script type="text/javascript" language="javascript" src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/js/jquery.qtip-1.0-pre.js"></script>
    
    <link href="${pageContext.request.contextPath}/css/mumstatus.css" rel="stylesheet" type="text/css" />
    
    
    <script type="text/javascript">
        var msPerSecond = 1000;
        var msPerMinute = msPerSecond * 60;
        var msPerHour   = msPerMinute * 60;

        $(document).ready(function() {
            //Setup page load and age info
            $("#pageRenderInfo .dateAgingPair .date").html((new Date()).toString());
            updateDateAge($("#pageRenderInfo .dateAgingPair"));
            setInterval(function() {
                var dateAgingPair = $("#pageRenderInfo .dateAgingPair");
                var diff = updateDateAge(dateAgingPair);
                
                if (diff > 5 * msPerMinute) {
                    dateAgingPair.addClass('FAIL');
                    dateAgingPair.removeClass('WARN');
                }
                else if (diff > 1 * msPerMinute) {
                    dateAgingPair.addClass('WARN');
                    dateAgingPair.removeClass('FAIL');
                }
                else {
                    dateAgingPair.removeClass('WARN');
                    dateAgingPair.removeClass('FAIL');
                }
            }, 1000);

            //Define out custom tool tip styles to match the OK/WARN/FAIL CSS used for the table cells
            $.fn.qtip.styles.OK = {
                width: {
                    max: 500
                },
                background: 'lightgreen',
                color: 'black',
                border: {
                    width: 1,
                    radius: 2,
                    color: '#000000'
                },
                tip: 'topLeft',
                name: 'dark' // Inherit the rest of the attributes from the preset dark style
            };
            $.fn.qtip.styles.WARN = {
                background: 'yellow',
                name: 'OK' // Inherit the rest of the attributes from the OK style
            };
            $.fn.qtip.styles.FAIL = {
                background: 'red',
                color: 'white',
                name: 'OK' // Inherit the rest of the attributes from the OK style
            };

            //Add the tooltips
            $('.serverGroup .server td .tooltip').each(function() {
                var tipTarget = $(this).parent();
                
                var style = 'FAIL';
                if (tipTarget.hasClass('OK')) {
                    style = 'OK';
                }
                else if (tipTarget.hasClass('WARN')) {
                    style = 'WARN';
                }
                
                tipTarget.qtip({
                    content: $(this),
                    delay: 50,
                    style: {
                        name: style,
                        tip: 'topLeft'
                    },
                    api: {
                        beforeContentUpdate: function() {
                            //remove the old tip content before adding the new content
                            $(this.elements.content).find(".tooltip").remove();
                        },
                        beforeShow: function() {
                            var targetElement = $(this.elements.target);
                            
                            //Update the date age info before showing the tip
                            targetElement.find('.dateAgingPair').each(function() {
                                updateDateAge($(this));
                            });

                            //Update the tool tip content
                            this.updateContent(targetElement.find('.tooltip'));
                            return true;
                        }
                    }
                });
            });
        });

        function updateDateAge(dateAgingPair) {
            var dateStr = dateAgingPair.find('.date').html();
            var date = new Date(dateStr);
            var diff = dateDiff(date, new Date());
            var time = formateTime(diff);
            dateAgingPair.find('.age').html(time);

            return diff;
        }

        function dateDiff(date1, date2) {
            date1 = new Date(date1);
            date2 = new Date(date2);

            var timediff = date2 - date1;
            if (isNaN(timediff)) {
                return NaN;
            }

            return timediff;
        }

        function formateTime(timeInMs) {
            if (isNaN(timeInMs)) {
                return NaN;
            }

            var hours = Math.floor(timeInMs / msPerHour);
            timeInMs = timeInMs - (hours * msPerHour);

            var minutes = Math.floor(timeInMs / msPerMinute);
            timeInMs = timeInMs - (minutes * msPerMinute);

            var seconds = Math.floor(timeInMs / msPerSecond);

            return leftPad(hours, 2) + ":" + leftPad(minutes, 2) + ":" + leftPad(seconds, 2);
        }

        function leftPad(str, len) {
            str = str + "";
            while (str.length < len) {
                str = "0" + str;
            }
            return str;
        }
    </script>
</head>
<body>
<div id="header">
   <small>Refresh:</small> 
   <span class="refreshChoices">
      <c:choose>
          <c:when test="${! empty refresh}">
              <a href="${pageContext.request.contextPath}" class="refreshChoice">OFF</a>
          </c:when>
          <c:otherwise>
              <a class="refreshChosen">OFF</a>
          </c:otherwise>
      </c:choose>
      <c:choose>
          <c:when test="${refresh != '15'}">
              <a href="${pageContext.request.contextPath}?refresh=15" class="refreshChoice">15</a>
          </c:when>
          <c:otherwise>
              <a class="refreshChosen">15</a>
          </c:otherwise>
      </c:choose>
      <c:choose>
          <c:when test="${refresh != '30'}">
              <a href="${pageContext.request.contextPath}?refresh=30" class="refreshChoice">30</a>
          </c:when>
          <c:otherwise>
              <a class="refreshChosen">30</a>
          </c:otherwise>
      </c:choose>
      <c:choose>
          <c:when test="${refresh != '60'}">
              <a href="${pageContext.request.contextPath}?refresh=60" class="refreshChoice">60</a>
          </c:when>
          <c:otherwise>
              <a class="refreshChosen">60</a>
          </c:otherwise>
      </c:choose>
    </span>
    <small id="pageRenderInfo">
        Page rendered in ${renderTime}ms <span class="dateAgingPair">at <span class="date"></span> and is <span class="age"></span> old</span>
    </small>
</div>
<div id="container">
<div id="left">
<c:forEach var="serverGroup" items="${config.serverGroups}">
    <c:if test="${serverGroup.name == 'Madison VIPs' || serverGroup.name == 'System VIPs' || serverGroup.name == 'PROD'}">
        <mum:serverGroup serverGroup="${serverGroup}" />
    </c:if>
</c:forEach>
</div>
<div id="right">
<c:forEach var="serverGroup" items="${config.serverGroups}">
    <c:if test="${serverGroup.name != 'Madison VIPs' && serverGroup.name != 'System VIPs' && serverGroup.name != 'PROD'}">
        <mum:serverGroup serverGroup="${serverGroup}" />
    </c:if>
</c:forEach>
</div>
<div id="footer">
    Generally:<br/>
    200 - in L4<br/>
    404 - not in L4<br/>
    503 - tomcat down<br/>
    &nbsp;-2&nbsp; - unreachable (httpd or machine down)<br/>
    <br/>
    <br/>
</div>
</div>
</body>
</html>
