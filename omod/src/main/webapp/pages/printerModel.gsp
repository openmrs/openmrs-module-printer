<%
    ui.decorateWith("appui", "standardEmrPage")

    def typeOptions = []

    printerTypes.each {
        typeOptions.push([ label: ui.message("printer." + it), value:it ])
    }

    def handlerOptions = []

    printHandlers.each {
        handlerOptions.push([ label: ui.message(it.displayName), value: it.beanName])
    }

%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}' },
        { label: "${ ui.message("printer.managePrinterModels")}" , link: '${ui.pageLink("printer", "managePrinterModels")}' },
        { label: "${ ui.message("printer.model.add")}" }
    ];

    jq(function(){
        jq('input.confirm').click(function(){

            if (!jq(this).attr("disabled")) {
                jq(this).closest("form").submit();
            }

            jq(this).attr('disabled', 'disabled');
            jq(this).addClass("disabled");

        });
    });

</script>

<form method="post" action="printerModel.page">

    <h3>${ ui.message("printer.model.edit") }</h3>

    <fieldset>
        ${ ui.includeFragment("uicommons", "field/radioButtons", [ label: ui.message("printer.type"), formFieldName: "type", initialValue: (printerModel.type ?: ''), options: typeOptions ])}
        ${ ui.includeFragment("uicommons", "field/text", [ label: ui.message("printer.model.name"), formFieldName: "name", initialValue: (printerModel.name ?: '') ])}
        ${ ui.includeFragment("uicommons", "field/dropDown", [ label: ui.message("printer.model.handler"), formFieldName: "printHandler", initialValue: (printerModel.printHandler ?: ''), options: handlerOptions ])}

    </fieldset>

    <div>
        <input type="button" class="cancel" value="${ ui.message("emr.cancel") }" onclick="javascript:window.location='${ ui.pageLink("printer", "managePrinterModels") }'" />
        <input type="submit" class="confirm" value="${ ui.message("emr.save") }" />
    </div>

    <input type="hidden" name="printerModelId" value="${ printerModel.id ?: ''}" />
    <input type="hidden" name="uuid" value="${ printerModel.uuid ?: ''}" />

</form>