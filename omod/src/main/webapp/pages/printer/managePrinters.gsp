<%
    ui.decorateWith("appui", "standardEmrPage")
%>
<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}' },
        { label: "${ ui.message("printer.managePrinters")}" }
    ];
</script>
<h3>${  ui.message("printer.managePrinters") }</h3>
<div>
    <a href="/${ contextPath }/emr/printer/printer.page" class="button">${ ui.message("printer.add") }</a>
</div>
<br/>
<table>
    <tr>
        <th>${ ui.message("printer.type") }</th>
        <th>${ ui.message("printer.physicalLocation") }</th>
        <th>${ ui.message("printer.name") }</th>
        <th>${ ui.message("printer.ipAddress") }</th>
        <th>${ ui.message("printer.port") }</th>
        <th>&nbsp;</th>
    </tr>

    <% if (!printers) { %>
        <tr><td colspan="6">${ ui.message("emr.none") }</td></tr>
    <% } %>
    <% printers.sort { it.name }.each {   %>
    <tr>
        <td>
            ${ ui.message("printer." + it.type) }
        </td>
        <td>
            ${ ui.format(it.physicalLocation) ?: '&nbsp;'}
        </td>
        <td>
            ${ ui.format(it.name) }
        </td>
        <td>
            ${ ui.format(it.ipAddress) }
        </td>
        <td>
            ${ ui.format(it.port) }
        </td>
        <td>
            <a href="/${ contextPath }/emr/printer/printer.page?printerId=${ it.printerId }">
                <button>${ ui.message("emr.edit") }</button>
            </a>
        </td>
    </tr>
    <% } %>
</table>