define([
  'ojs/ojcontext',
  'ojs/ojmodule-element-utils',
  'ojs/ojresponsiveutils',
  'ojs/ojresponsiveknockoututils',
  'knockout',
  'loginManager',
  'notificationManager',
  'ojs/ojarraydataprovider',
  'ojs/ojknockout',
  'ojs/ojmessages'
],
  function (Context, ModuleElementUtils, ResponsiveUtils, ResponsiveKnockoutUtils, ko, loginManager, notificationManager, ArrayDataProvider) {
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
          notificationManager.removeAllNotificationsOfType('login');
          loginManager
            .logout()
            .fail((jqXHR, textStatus, errorThrown) => {
              notificationManager.addNotification({
                severity: 'error',
                summary: 'Logout failed',
                detail: `${textStatus} - ${errorThrown}`,
                type: 'login'
              });
            });
        };
        // Module config to show
        //this.moduleConfig = ModuleElementUtils.createConfig({ name: 'login' });
        this.moduleConfig = ModuleElementUtils.createConfig({ name: 'notebook' });
        this.ModuleElementUtils = ModuleElementUtils;
        this.dataProvider = new ArrayDataProvider(notificationManager.notifications);
      }

    }

    loginManager.init();
    // release the application bootstrap busy state
    Context.getPageContext().getBusyContext().applicationBootstrapComplete();
    return new ControllerViewModel();
  }
);
