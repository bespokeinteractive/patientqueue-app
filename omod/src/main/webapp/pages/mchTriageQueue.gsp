<%
    ui.decorateWith("appui", "standardEmrPage", [title: "Mch Triage Queue"])
    ui.includeCss("uicommons", "datatables/dataTables_jui.css")
    ui.includeCss("coreapps", "patientsearch/patientSearchWidget.css")
    ui.includeJavascript("patientqueueapp", "jquery.dataTables.min.js")
    ui.includeJavascript("patientqueueapp", "queue.js")
    ui.includeJavascript("patientqueueapp", "jquery.session.js")
%>
<script>
    function handlePatientRowSelection() {
        this.handle = function (row) {
            window.location = emr.pageLink("mchapp", "triage", { "patientId" : row.patientId, "queueId" : row.id })
        }
    }
    var handlePatientRowSelection =  new handlePatientRowSelection();
    var getPatientsFromQueue = function(){
        tableObject.find('td.dataTables_empty').html('<span><img class="search-spinner" src="'+emr.resourceLink('uicommons', 'images/spinner.gif')+'" /></span>');
        jq.getJSON(emr.fragmentActionLink("patientqueueapp", "patientQueue", "getPatientsInMchTriageQueue"),
                {
                    'mchConceptId': ${mchConceptId}
                })
                .success(function(results) {
                    updateMCHSearchResults(results.data);

                })
                .fail(function(xhr, status, err) {
                    updateMCHSearchResults([]);
                });
    };
	
	jq(function() {
		jq('#queueRoles input').click(function(){
			dTable.api().draw();
		});
	});
</script>
<style>
	.results {
		margin-top: 5px;
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
		margin-left: -25px;
		position: relative;
		right: 5px;
	}
	#patient-search-form input {
		display: inline;
		margin-top: 5px;
	}
	#breadcrumbs a, #breadcrumbs a:link, #breadcrumbs a:visited {
		text-decoration: none;
	}
	.new-patient-header .identifiers {
		margin-top: 5px;
	}
	#queue-choice-form{
		display: inline-block;
		width: 300px;
	}
	#patient-search-form {
		display: inline-block;
		width: 650px;
	}
	select:focus,
	input:focus {
		outline: 2px none #000!important;
	}
	form p, .form p {
		margin-bottom: 5px;
	}
	form label, .form label {
		color: #009384;
		padding-left: 5px;
	}
	#patient-search-clear-button {
		cursor: pointer;
	}
	.name{
		color: #f26522;
	}
	form select, .form select {
		min-width: 97%!important;
	}
	#queueRoles{
		padding: 0 10px 0 0;
	}
	#queueRoles span{
		border-left: 15px solid #363463;
		font-family: "OpenSansBold";
		font-size: 1em;
		padding-left: 5px;
	}
	#queueRoles label{
		background: lightyellow none repeat scroll 0 0;
		border: 1px solid lightgrey;
		border-radius: 4px;
		color: #363463;
		cursor: pointer;
		float: right;
		margin-left: 2px;
		padding: 1px 6px;
	}
</style>

<header>
</header>
<body>
<div class="clear"></div>
<div class="container">
    <div class="example">
        <ul id="breadcrumbs">
            <li>
                <a href="${ui.pageLink('referenceapplication','home')}">
                    <i class="icon-home small"></i></a>
            </li>

            <li>
                <i class="icon-chevron-right link"></i>
                <a>Mch Triage Queue</a>
            </li>

            <li>
                <i class="icon-chevron-right link"></i>
                Select Patient
            </li>
        </ul>
    </div>

    <div class="patient-header new-patient-header">
        <div class="demographics">
            <h1 class="name" style="border-bottom: 1px solid #ddd;">
                <span>MCH TRIAGE QUEUE &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span>
            </h1>
        </div>

        <div class="identifiers">
            <em>Current Time:</em>
            <span>${date}</span>
        </div>

        <div class="onerow" style="margin-top: 40px">
            <form style="width:100%; margin-bottom: 5px;" method="get" id="patient-search-form" onsubmit="return false">
                <p>
                    <label for="patient-search">Filter Patient</label>
                    <input style="width:99%" type="text" id="patient-search" placeholder="${ ui.message("coreapps.findPatient.search.placeholder") }"><i id="patient-search-clear-button" class="small icon-remove"></i>
                </p>
            </form>
        </div>
		
		<div class="clear"></div>		
		<div id="queueRoles">
			<% if (mchQueueRoles.size() == 0) { %>
				<span>NO ROLES ASSIGNED</span>
			<% } else { %>
				<span>SELECT QUEUE</span>
			<% } %>
			
			<% mchQueueRoles.each { role -> %>
				<label>
					<input type="checkbox" value="${role.uuid}" checked/>
					${role.description}					
				</label>
			<% } %>
			
		</div>
    </div>
</div>

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

            <th class="ui-state-default" style="width: 80px;">
                <div class="DataTables_sort_wrapper">Clinic<span class="DataTables_sort_icon"></span></div>
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
</body>

