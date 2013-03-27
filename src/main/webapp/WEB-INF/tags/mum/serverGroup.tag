<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="status" uri="https://my-tools.doit.wisc.edu/tags/status" %>

<%@ tag isELIgnored="false" %>

<%@ attribute name="serverGroup" required="true" type="edu.wisc.mum.status.xom.config.ServerGroupType" %>

<h3>${serverGroup.name}</h3>
<c:if test="${! empty serverGroup.l4Links}">
   L4 Stats for: 
    <c:forEach var="l4Link" items="${serverGroup.l4Links}" varStatus="l4LinkStatus">
        <a href="${l4Link.link}">${l4Link.name}</a><c:if test="${!l4LinkStatus.last}">&nbsp;&nbsp;</c:if>
    </c:forEach>
</c:if>
<table class="serverGroup">
    <caption align="bottom">
        <c:set var="portCount" value="0" />
        <c:forEach var="portInfo" items="${serverGroup.portInfos}" varStatus="portInfoStatus">
            ${portInfo.name}:
            <c:forEach var="port" items="${portInfo.ports}" varStatus="portStatus">
                <c:set var="portCount" value="${portCount + 1}" />
                ${port.value}<c:if test="${!portStatus.last}">/</c:if>
            </c:forEach>
            <c:if test="${!portInfoStatus.last}">&nbsp;&nbsp;&nbsp;</c:if>
        </c:forEach>
    </caption>
    <tr>
        <th></th>
        <th colspan="${portCount}"></th>
        <th colspan="2">Monitor</th>
        <th></th>
        <th></th>
        <th></th>
    </tr>
    <tr>
        <th>HOST</th>
        <c:forEach var="portInfo" items="${serverGroup.portInfos}">
            <c:forEach var="port" items="${portInfo.ports}">
                <th>${port.value}</th>
            </c:forEach>
        </c:forEach>
        <th>Time (ms)</th>
        <th>Status</th>
        <th></th>
        <th></th>
        <th>Ver.</th>
    </tr>
    <c:forEach var="server" items="${serverGroup.servers}">
        <tr class="server">
            <td><a href="http://${server.service}/">${fn:substringBefore(server.service, '.')}</a>/${fn:substringBefore(server.server, '.')}</td>
            <c:forEach var="portInfo" items="${serverGroup.portInfos}">
                <c:forEach var="port" items="${portInfo.ports}">
                    <c:set var="serverKey" value="${serverGroup.name}.${server.service}.${port.value}" />
                    <c:set var="serverResponse" value="${serverResponses[serverKey]}" />
                    <td class="httpResponse ${empty serverResponse ? 'FAIL' : serverResponse.status}">
                        <div class="tooltip">
                            <table>
                                <tr>
                                    <th>Checked:</th>
                                    <td class="dateAgingPair">
                                        <div class="date">${serverResponse.timestamp}</div>
                                        <div><span class="age"></span> old</div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Response Time:</th>
                                    <td>${serverResponse.duration}ms</td>
                                </tr>
                                <tr>
                                    <th>Message:</th>
                                    <td>${serverResponse.statusReasonPhrase}</td>
                                </tr>
                            </table>
                        </div>
                        ${serverResponse.statusCode}
                    </td>
                </c:forEach>
            </c:forEach>
            <c:set var="serverKey" value="${serverGroup.name}.${server.service}" />
            <c:set var="monitorStatus" value="${monitorStatuses[serverKey]}" />
            <c:set var="hostMonitorLog" value="${monitorLogs[serverKey]}" />
            <c:choose>
                <c:when test="${! empty monitorStatus}">
                    <td class="monitorStatus ${status:sand(monitorStatus.durationStatus, monitorLogsStatus[serverKey])}">
                        <c:if test="${! empty hostMonitorLog}">
                            <div class="tooltip">
                                <table>
                                    <tr>
                                        <th>Tab</th>
                                        <th>Time (ms)</th>
                                        <th>Okay</th>
                                        <th>Last Sample</th>
                                    </tr>
                                    <c:forEach var="monitorLog" items="${hostMonitorLog}">
                                        <tr>
                                            <th>${fn:substringAfter(monitorLog.label, ',')}</th>
                                            <td>${monitorLog.duration}</td>
                                            <td>${monitorLog.success}</td>
                                            <td class="dateAgingPair">
                                                <span class="date">${monitorLog.lastSample}</span> (<span><span class="age"></span> old)</span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </c:if>
                        ${monitorStatus.minimumDuration}/<fmt:formatNumber value="${monitorStatus.averageDuration}" pattern="0" />/${monitorStatus.maximumDuration}
                    </td>
                    <td class="monitorStatus ${monitorStatus.monitorStatus}">
                        ${monitorStatus.status}
                    </td>
                </c:when>
                <c:otherwise>
                    <td class="WARN"></td>
                    <td class="WARN"></td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${fn:contains(serverGroup.name, 'VIP')}">
                    <td/>
                    <td/>
                </c:when>
                <c:otherwise>
                    <td><a href="https://${server.service}/logs/">Logs</a></td>
                    <td><a href="https://orca.doit.wisc.edu/orca/${fn:substringBefore(server.server, '.')}/">Orca</a></td>
                </c:otherwise>
            </c:choose>
        </tr>
    </c:forEach>
</table>
