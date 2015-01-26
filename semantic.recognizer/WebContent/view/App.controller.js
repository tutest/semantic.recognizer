sap.ui.core.mvc.Controller.extend("semantic.recognizer.view.App", {

	USER_ROLE_SERVICE_PATH : "rest/api/v1/user/isUserInRole",

	currentView : undefined,

	onInit : function() {
		var i18nModel = new sap.ui.model.resource.ResourceModel({
			bundleUrl : [jQuery.sap.getModulePath("semantic.recognizer"), "i18n/messageBundle.properties"].join("/")
		});
		this.getView().setModel(i18nModel, "i18n");

		var nav = jQuery.sap.syncGetJSON("model/nav.json").data;

		var model = new sap.ui.model.json.JSONModel();
		model.setData(nav);
		this.getView().setModel(model, "nav");

		var navItem = this.byId("shellId").getWorksetItems()[0];
		var shortName = navItem.getBindingContext("nav").getProperty("shortViewName");
		this.showView(shortName);
	},

	onAfterRendering : function() {
	},

	selectItem : function(oEvent) {
		var navItem = oEvent.getParameter("item");
		var shortName = navItem.getBindingContext("nav").getProperty("shortViewName");
		this.showView(shortName);
	},

	showView : function(shortName) {
		var viewName = "semantic.recognizer.view." + shortName;
		var viewId = shortName + "Id";
		this.currentView = sap.ui.getCore().byId(viewId) || sap.ui.htmlview(viewId, viewName); // get existing or create
		// new
		this.byId("shellId").setContent(this.currentView);
	},

	setSelectedTab : function(tabIndex) {
		var shell = this.byId("shellId");
		shell.setSelectedWorksetItem(shell.getWorksetItems()[tabIndex]);
	},

	localize : function(key) {
		return this.getView().getModel("i18n").getProperty(key);
	},

	onLogutPressed : function() {
		document.location.href = "/logout.jsp";
	}
});
