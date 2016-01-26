<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("uicommons", "datatables/dataTables_jui.css")
    ui.includeJavascript("uicommons", "datatables/jquery.dataTables.min.js")
    ui.includeJavascript("patientqueueui", "queue.js")
%>
<script type="text/javascript">
    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {label: "Patient Queue"}
    ]

    function handlePatientRowSelection() {
        this.handle = function (row) {
            console.log("Row status: " + row.status);
            location.href = '/' + OPENMRS_CONTEXT_PATH + emr.applyContextModel('${ ui.escapeJs(afterSelectedUrl) }', { patientId: row.patient.id, queueId: row.id, opdId: jq('#queue-choice').val()});
        }
    }

    var handlePatientRowSelection =  new handlePatientRowSelection();
</script>

<h2>Patient Queue</h2>

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

<form id="patient-search" style="display: none">
	<input type="text" id="search-query" placeholder="Search by ID or Name" />
</form>

<table id="patient-queue">
	<thead>
		<tr>
			<th>Identifier</th>
			<th>Name</th>
			<th>Age</th>
			<th>Gender</th>
			<th>Visit Status</th>
		</tr>
	</thead>
	<tbody></tbody>
</table>

<style>
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
</style>
