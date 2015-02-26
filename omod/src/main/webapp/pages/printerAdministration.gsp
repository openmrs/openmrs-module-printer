<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("printer.administration") ])
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}' },
        { label: "${ ui.message("printer.administration")}" }
    ];
</script>

<div id="tasks">
    <a id="printer.managePrinters" href="${ ui.pageLink("printer", "managePrinters")}" class="button app big">
        <i class="icon-print"></i>
        ${ ui.message("printer.managePrinters") }
    </a>

    <a id="printer.managePrinterModels" href="${ ui.pageLink("printer", "managePrinterModels")}" class="button app big">
        <i class="icon-print"></i>
        ${ ui.message("printer.managePrinterModels") }
    </a>

    <a id="printer.defaultPrinters" href="${ ui.pageLink("printer", "defaultPrinters")}" class="button app big">
        <i class="icon-print"></i>
        ${ ui.message("printer.defaultPrinters") }
    </a>
</div>
