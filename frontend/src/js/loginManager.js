define([
  'knockout'
],
  function (ko) {
    'use strict';

    class LoginManager {

      login(login, password, rememberMe) {
        return $.ajax({
          url: '/auth/login',
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
          url: '/auth/logout',
        }).done(() => {
          this.userLogin('');
        });
      }

      init() {
        $.ajax({
          url: '/api/user',
        }).done((data) => {
          this.userLogin(data.login);
        }).fail(() => {
          this.userLogin('');
        });
      }

      constructor() {
        this.userLogin = ko.observable('');
        this.login = this.login.bind(this);
        this.logout = this.logout.bind(this);
        this.init = this.init.bind(this);
      }

    }

    return new LoginManager();
  });