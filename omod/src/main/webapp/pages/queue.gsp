<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("uicommons", "datatables/dataTables_jui.css")
    ui.includeCss("coreapps", "patientsearch/patientSearchWidget.css")
    ui.includeJavascript("patientqueueapp", "jquery.dataTables.min.js")
    ui.includeJavascript("patientqueueapp", "queue.js")
%>
<script type="text/javascript">
    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {label: "Patient Queue"}
    ]

    function handlePatientRowSelection() {
        this.handle = function (row) {
            console.log("Row status: " + row.status);
            location.href = '/' + OPENMRS_CONTEXT_PATH + emr.applyContextModel('${ ui.escapeJs(afterSelectedUrl) }', { patientId: row.patient.id, queueId: row.id, visitStatus: row.visitStatus,opdId: jq('#queue-choice').val()});
        }
    }

    var handlePatientRowSelection =  new handlePatientRowSelection();
</script>

<style>
.results {
    margin-top: 1em;
}
#patient-queue tbody tr:hover {
    background-color: #f26522;
    cursor: pointer;
}
#patient-queue tbody tr:hover {
    background: #007fff;
    cursor: pointer;
    color: white;
}
#patient-queue tbody tr td.dataTables_empty:hover {
    background: white;
    cursor: default;
    color: #363463;
}
#patient-search-clear-button {
    position: relative;
    right: 25px;
}
#patient-search-form input {
    display: inline;
    margin-top: 5px;
}
</style>

<h2>${title}</h2>

<form>
    <p>
        <label for="queue-choice">Select Queue</label>
        <select id="queue-choice">
            <option value="0">-- Please select --</option>
            <% listOPD.each { it -> %>
            <option value="${it.answerConcept.id}"> ${it.answerConcept.name} </option>
            <% } %>
        </select>
    </p>
</form>

<form method="get" id="patient-search-form" onsubmit="return false">
    <input type="text" id="patient-search" placeholder="${ ui.message("coreapps.findPatient.search.placeholder") }" /><i id="patient-search-clear-button" class="small icon-remove-sign"></i>
</form>

<div class="results">
    <table id="patient-queue" class="dataTable">
        <thead>
        <tr role="row">
            <th class="ui-state-default" style="width: 160px;">
                <div class="DataTables_sort_wrapper">Identifier<span class="DataTables_sort_icon"></span></div>
            </th>

            <th class="ui-state-default">
                <div class="DataTables_sort_wrapper">Name<span class="DataTables_sort_icon"></span></div>
            </th>

            <th class="ui-state-default" style="width: 80px;">
                <div class="DataTables_sort_wrapper">Age<span class="DataTables_sort_icon"></span></div>
            </th>

            <th class="ui-state-default" style="width: 65px;">
                <div class="DataTables_sort_wrapper">Gender<span class="DataTables_sort_icon"></span></div>
            </th>

            <th class="ui-state-default" style="width:95px;">
                <div class="DataTables_sort_wrapper">Visit Status<span class="DataTables_sort_icon"></span></div>
            </th>

            <th class="user-processing ui-state-default" style="width: 150px;">
                <div class="DataTables_sort_wrapper">Processing<span class="DataTables_sort_icon"></span></div>
            </th>
        </tr>
        </thead>

        <tbody>
        </tbody>
    </table>
</div>


