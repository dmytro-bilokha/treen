define([
  'knockout',
  'appConstants'
],
  function (ko, appConstants) {
    'use strict';

    class LoginManager {

      login(login, password, rememberMe) {
        return $.ajax({
          url: `${appConstants.CONTEXT_PATH}/auth/login`,
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify({ login: login, password: password, rememberMe: rememberMe})
        }).done(() => {
          this.userLogin(login);
        }).fail(() => {
          this.userLogin('');
        });
      }

      logout() {
        return $.ajax({
          url: `${appConstants.CONTEXT_PATH}/auth/logout`,
          type: 'POST'
        }).done(() => {
          this.userLogin('');
        });
      }

      init() {
        $.ajax({
          url: `${appConstants.CONTEXT_PATH}/api/user`,
        }).done((data) => {
          this.userLogin(data.login);
        }).fail(() => {
          this.userLogin('');
        });
      }

      registerFailedAuthorization() {
        this.userLogin('');
      }

      constructor() {
        this.userLogin = ko.observable('');
        this.login = this.login.bind(this);
        this.logout = this.logout.bind(this);
        this.init = this.init.bind(this);
        this.registerFailedAuthorization = this.registerFailedAuthorization.bind(this);
      }

    }

    return new LoginManager();
  });