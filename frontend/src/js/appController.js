define([
  'ojs/ojcontext',
  'ojs/ojmodule-element-utils',
  'ojs/ojresponsiveutils',
  'ojs/ojresponsiveknockoututils',
  'knockout',
  'loginManager',
  'ojs/ojarraydataprovider',
  'ojs/ojknockout',
  'ojs/ojmessages'
],
  function (Context, ModuleElementUtils, ResponsiveUtils, ResponsiveKnockoutUtils, ko, loginManager, ArrayDataProvider) {
    'use strict';

    class ControllerViewModel {

      constructor() {
        // Media queries for repsonsive layouts
        const smQuery = ResponsiveUtils.getFrameworkQuery(ResponsiveUtils.FRAMEWORK_QUERY_KEY.SM_ONLY);
        this.smScreen = ResponsiveKnockoutUtils.createMediaQueryObservable(smQuery);

        // Header
        // Application Name used in Branding Area
        this.appName = ko.observable("Treen");
        // User Info used in Global Navigation area
        this.userLogin = loginManager.userLogin;
        // Action menu in Global Navigation area
        this.userMenuAction = (event) => {
          if (event.detail.selectedValue !== 'logout') {
            return;
          }
          loginManager.logout();
        };
        // Module config to show
        this.moduleConfig = ModuleElementUtils.createConfig({ name: 'login' });
        this.applicationMessages = ko.observableArray([{
          severity: 'error',
          summary: 'Error summary here',
          detail: 'Error details',
        }]);
        this.dataProvider = new ArrayDataProvider(this.applicationMessages);
      }

    }

    // release the application bootstrap busy state
    Context.getPageContext().getBusyContext().applicationBootstrapComplete();
    return new ControllerViewModel();
  }
);
