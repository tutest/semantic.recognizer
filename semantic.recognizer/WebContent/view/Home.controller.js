sap.ui.core.mvc.Controller.extend("semantic.recognizer.view.Home", {

	MODELS : {
		analysis : "analysis"
	},

	CONTROLS : null,

	onInit : function() {
		this._loadControls();
		
		var model = new sap.ui.model.json.JSONModel();
		this.getView().setModel(model, this.MODELS.analysis);
	},

	_loadControls : function() {
		this.CONTROLS = {
			searchField : this.byId("searchField"),
			analysisTable : this.byId("analysisTable"),
			langDropdown : this.byId("langDropdown")
		};
	},
	
	onAfterRendering : function() {
	},

	onAnalyze : function(evnt) {
		this._setBusy(true);
		
		var fDoneCallback = function(textId){
			this._loadAnalyzes(textId);
		}
		
		var fFailCallback = function(){
			alert("Failed to post the sentece.");
		}
		var sentence = this.CONTROLS.searchField.getValue();
		var data = {
				text : sentence,
				language : this.CONTROLS.langDropdown.getSelectedKey()
		};
		semantic.recognizer.util.Helper.asynchPostJSON(this, "/analyzes", data, fDoneCallback, fFailCallback, function(){this._setBusy(false);});
	},
	
	_loadAnalyzes : function(textId){
		var fDoneCallback = function(response){
			this.getView().getModel(this.MODELS.analysis).setData(response);
		}
		
		var fFailCallback = function(){
			alert("Failed to get text analyzes.");
		}
		this._clearTheFields();
		semantic.recognizer.util.Helper.asynchGetJSON(this, "/analyzes?textId=" + textId, fDoneCallback, fFailCallback);
	},
	
	_loadContextsModel : function(context) {
		var data = training.courses.management.util.ContextUtil.loadContexts(context);
		var ctxModel = new sap.ui.model.json.JSONModel();
		ctxModel.setData(data);
		this.getView().setModel(ctxModel, this.MODELS.context);
	},

	_clearTheFields : function() {
		//this.CONTROLS.searchField.setValue(null);
		this.CONTROLS.analysisTable.setSelectedIndex(undefined);
		this.getView().getModel(this.MODELS.analysis).setData([]);
	},
	
	_setBusy : function(bValue) {
		this.getView().setBusyIndicatorDelay(1);
		this.getView().setBusy(bValue);
	}
});