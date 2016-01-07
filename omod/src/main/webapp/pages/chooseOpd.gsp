<%
	ui.decorateWith("appui", "standardEmrPage")
	def props = ["patientIdentifier", "patientName", "birthDate", "sex", "status", "visitStatus"]
%>
<script type="text/javascript">
    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {label: "Patient Queue"}
    ]
</script>

<script type="text/javascript">
jq = jQuery;

jq(function() {
    jq("#patient-search").hide();
    jq("#opd-choice").val("0");
    jq('#opd-choice').change(function() {
        jq.getJSON('${ ui.actionLink("patientqueueui", "patientQueue", "getPatientsInQueue") }',
            {
              'opdId': jq(this).val()
            })
        .success(function(data) {
            updateQueueTable(data)
        })
        .error(function(xhr, status, err) {
            alert('AJAX error ' + err);
        });
    });
    
    jq("#search-query").change(function(){
        jq.getJSON('${ ui.actionLink("patientqueueui", "patientQueue", "getPatientsInQueue") }',
            {
              'opdId': jq(this).val(),
              'query': jq(this).val()
            })
        .success(function(data) {
            updateQueueTable(data)
        })
        .error(function(xhr, status, err) {
            alert('AJAX error ' + err);
        });
    });
    
    function updateQueueTable(data) {
        jq('#patient-queue > tbody > tr').remove();
        var tbody = jq('#patient-queue > tbody');
        for (index in data) {
            var item = data[index];
            var row = '<tr>';
            <% props.each { %>
                row += '<td>' + item.${ it } + '</td>';
            <% } %>
            row += '</tr>';
            tbody.append(row);
        }
    }
});
</script>

<h2>Patient Queue</h2>

<form>
	<p>
		<label for="opd-choice">Select Queue</label>
		<select id="opd-choice">
			<option value="0">-- Please select --</option>
		<% listOPD.each { it -> %>
			<option value="${it.answerConcept.id}"
			<% if(it.answerConcept.id == opdId) { %>
				selected="selected"
			<% } %>
			> ${it.answerConcept.name} </option>
		<% } %>
		</select>
	</p>
</form>

<form id="patient-search">
	<input type="text" id="search-query" placeholder="Search by ID or Name" />
</form>

<table id="patient-queue">
	<thead>
		<tr>
			<th>Identifier</th>
			<th>Name</th>
			<th>Age</th>
			<th>Gender</th>
			<th>Status</th>
			<th>Visit Status</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td colspan="6">No Patients in queue</td>
		</tr>
	</tbody>
</table>