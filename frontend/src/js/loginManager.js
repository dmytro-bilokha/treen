define([
  'knockout'
],
  function (ko) {
    'use strict';

    class LoginManager {

      login(login, password) {
        return $.ajax({
          url: '/auth/login',
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify({ login: login, password: password })
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
          url: '/auth/user',
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
      }

    }

    return new LoginManager();
  });