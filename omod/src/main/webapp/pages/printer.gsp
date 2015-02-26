<%
    ui.decorateWith("appui", "standardEmrPage")

    def typeOptions = []

     printerTypes.each {
         typeOptions.push([ label: ui.message("printer." + it), value:it ])
     }

    def locationOptions = []

    locations.each {
        locationOptions.push([ label: ui.format(it), value: it.id ])
    }

    def printerModelOptions = []

    printerModels.each {
        printerModelOptions.push([ label: ui.format(it.name), value: it.id ])
    }
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}' },
        { label: "${ ui.message("printer.managePrinters")}" , link: '${ui.pageLink("printer", "managePrinters")}' },
        { label: "${ ui.message("printer.add")}" }
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

<form method="post" action="printer.page">

    <h3>${ ui.message("printer.edit") }</h3>

    <fieldset>
        ${ ui.includeFragment("uicommons", "field/radioButtons", [ label: ui.message("printer.type"), formFieldName: "type", initialValue: (printer.type ?: ''), options: typeOptions ])}
        ${ ui.includeFragment("uicommons", "field/dropDown", [ label: ui.message("printer.physicalLocation"), formFieldName: "physicalLocation", initialValue: (printer.physicalLocation?.id ?: ''), options: locationOptions ])}
        ${ ui.includeFragment("uicommons", "field/text", [ label: ui.message("printer.name"), formFieldName: "name", initialValue: (printer.name ?: '') ])}
        ${ ui.includeFragment("uicommons", "field/dropDown", [ label: ui.message("printer.model"), formFieldName: "model", initialValue: (printer.model?.id ?: ''), options: printerModelOptions ])}
        ${ ui.includeFragment("uicommons", "field/text", [ label: ui.message("printer.ipAddress"), formFieldName: "ipAddress", initialValue: (printer.ipAddress ?: '') ])}
        ${ ui.includeFragment("uicommons", "field/text", [ label: ui.message("printer.port"), formFieldName: "port",initialValue: (printer.port ?: '') ])}
    </fieldset>

    <div>
        <input type="button" class="cancel" value="${ ui.message("emr.cancel") }" onclick="javascript:window.location='${ ui.pageLink("printer", "managePrinters") }'" />
        <input type="submit" class="confirm" value="${ ui.message("emr.save") }" />
    </div>

    <input type="hidden" name="printerId" value="${ printer.id ?: ''}" />
    <input type="hidden" name="uuid" value="${ printer.uuid ?: ''}" />

</form>