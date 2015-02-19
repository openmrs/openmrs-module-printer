<%
    ui.decorateWith("appui", "standardEmrPage")

    def printHandlersDisplayMap = {}

    printHandlers.each {
        printHandlersDisplayMap[it.beanName] = ui.message(it.displayName);
    }

%>
<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}' },
        { label: "${ ui.message("printer.model.manage")}" }
    ];
</script>
<h3>${  ui.message("printer.model.manage") }</h3>
<div>
    <a href="/${ contextPath }/printer/printerModel.page" class="button">${ ui.message("printer.model.add") }</a>
</div>
<br/>
<table>
    <tr>
        <th>${ ui.message("printer.model.name") }</th>
        <th>${ ui.message("printer.type") }</th>
        <th>${ ui.message("printer.model.handler") }</th>
        <th>&nbsp;</th>
    </tr>

    <% if (!printerModels) { %>
    <tr><td colspan="4">${ ui.message("emr.none") }</td></tr>
    <% } %>
    <% printerModels.sort { it.name }.each {   %>
    <tr>
        <td>
            ${ ui.format(it.name) }
        </td>
        <td>
            ${ ui.message("printer." + it.type) }
        </td>
        <td>
            ${ printHandlersDisplayMap[it.printHandler] }
        </td>
        <td>
            <a href="/${ contextPath }/printer/printerModel.page?printerModelId=${ it.printerModelId }">
                <button>${ ui.message("emr.edit") }</button>
            </a>
        </td>
    </tr>
    <% } %>
</table>