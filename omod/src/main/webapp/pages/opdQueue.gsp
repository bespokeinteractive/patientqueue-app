<%
    ui.decorateWith("appui", "standardEmrPage", [title: "OPD Queue"])
    ui.includeCss("uicommons", "datatables/dataTables_jui.css")
    ui.includeCss("coreapps", "patientsearch/patientSearchWidget.css")
    ui.includeJavascript("patientqueueui", "jquery.dataTables.min.js")
    ui.includeJavascript("patientqueueui", "queue.js")
    ui.includeJavascript("patientqueueui", "jquery.session.js")
%>
<script type="text/javascript">
    function handlePatientRowSelection() {
        this.handle = function (row) {
            console.log("Row status: " + row.status);
            location.href = '/' + OPENMRS_CONTEXT_PATH + emr.applyContextModel('${ ui.escapeJs(afterSelectedUrl) }', { patientId: row.patient.id, queueId: row.id, opdId: jq('#queue-choice').val()});
        }
    }
	
	jq(document).ready(function () {
		jq('#search-in-db').change(function() {
			if(this.checked) {
				jq('#advanced').addClass('advancedcolor');
			}
			else {
				jq('#advanced').removeClass('advancedcolor');
			}
		});
	});

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
		display: inline-block;
		padding-left: 5px;
	}
	#patient-search-clear-button {
		cursor: pointer;
	}
	.name {
		color: #f26522;
	}
	#for-search-in-db{
		padding-right: 10px;
		cursor: pointer;
		float: right;
		color: #363463;
	}
	#for-search-in-db input{
		cursor: pointer;
	}
	form .advanced {
		background: #888 none repeat scroll 0 0;
		border-color: #dddddd;
		border-style: solid;
		border-width: 1px;
		color: #fff;
		cursor: pointer;
		float: right;
		padding: 5px 0;
		text-align: center;
		width: 35%;
	}
	.advancedcolor{
		background: #363463 none repeat scroll 0 0!important;
	}
	
	form .advanced i {
		font-size: 22px;
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
					<a>OPD Queue</a>
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
					<span>OPD PATIENT QUEUE &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span>
				</h1>
			</div>

			<div class="identifiers">
				<em>Current Time:</em>
				<span>${date}</span>
			</div>
			
			<div class="onerow" style="margin-top: 40px">
				<form id="queue-choice-form">
					
						<label for="queue-choice">Select Queue</label>
						<select id="queue-choice">
							<option value="0">-- Please select --</option>
							<% listOPD.each { it -> %>
							<option value="${it.answerConcept.id}"> ${it.answerConcept.name} </option>
							<% } %>
						</select>
					
				</form>
				
				<form method="get" id="patient-search-form" onsubmit="return false">
					
						<label for="patient-search">Filter Queue</label>
						<div onclick="ShowDashboard();" class="advanced" id="advanced">
							<i class="icon-filter"></i>ADVANCED SEARCH
						</div>
						
						<label for="search-in-db" id="for-search-in-db">
							<input type="checkbox" name="search-in-db" id="search-in-db">Search In Database
						</label>
						
						<input type="text" id="patient-search" placeholder="${ ui.message("coreapps.findPatient.search.placeholder") }" style="width: 96.6%;" /><i id="patient-search-clear-button" class="small icon-remove-sign"></i>
					
				</form>
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




